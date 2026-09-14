package repositorio

import produto.Servico
import java.sql.SQLException

/**
 * Repositório CRUD para Serviços de Instalação de Caixas d'Água.
 * Permite cadastrar, listar e gerenciar serviços de instalação e manutenção.
 */
class CRUDServico : InterfaceJPA<Servico>, ConexaoPostgres() {

    override fun salvar(item: Servico) {
        try {
            if (!conectar()) return

            val sql = "INSERT INTO servico (descricao, preco, instalador_id) VALUES (?, ?, ?)"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.descricao)
            stmt.setBigDecimal(2, item.preco)

            if (item.instalador != null) {
                stmt.setInt(3, item.instalador.id)
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER)
            }

            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Serviço '${item.descricao}' cadastrado com sucesso!")
        } catch (e: SQLException) {
            println("[ERRO AO SALVAR SERVIÇO] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun listar() {
        try {
            if (!conectar()) return

            val sql = """
                SELECT s.id, s.descricao, s.preco, p.nome AS instalador_nome
                FROM servico s
                LEFT JOIN pessoa p ON s.instalador_id = p.id
                ORDER BY s.id ASC
            """.trimIndent()

            val stmt = c!!.createStatement()
            val rs = stmt.executeQuery(sql)

            println("\n================ SERVIÇOS DE INSTALAÇÃO ================")
            while (rs.next()) {
                val id = rs.getInt("id")
                val desc = rs.getString("descricao")
                val preco = rs.getBigDecimal("preco")
                val inst = rs.getString("instalador_nome") ?: "Não atribuído"

                println("ID: $id | Descrição: $desc | Preço: R$ $preco | Instalador Responsável: $inst")
            }
            println("========================================================\n")
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO LISTAR SERVIÇOS] ${e.message}")
        } finally {
            fechar()
        }
    }

    /**
     * Busca um serviço pelo ID.
     */
    fun buscarPorId(id: Int): Servico? {
        try {
            if (!conectar()) return null

            val sql = "SELECT * FROM servico WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val desc = rs.getString("descricao")
                val preco = rs.getBigDecimal("preco")

                stmt.close()
                return Servico(id = id, descricao = desc, preco = preco, instalador = null)
            }
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO BUSCAR SERVIÇO POR ID] ${e.message}")
        } finally {
            fechar()
        }
        return null
    }

    override fun editar(item: Servico, id: Int) {
        try {
            if (!conectar()) return

            val sql = "UPDATE servico SET descricao = ?, preco = ? WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.descricao)
            stmt.setBigDecimal(2, item.preco)
            stmt.setInt(3, id)
            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Serviço ID $id editado com sucesso.")
        } catch (e: SQLException) {
            println("[ERRO AO EDITAR SERVIÇO] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun excluir(id: Int) {
        try {
            if (!conectar()) return

            val sql = "DELETE FROM servico WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Serviço ID $id excluído.")
        } catch (e: SQLException) {
            println("[ERRO AO EXCLUIR SERVIÇO] ${e.message}")
        } finally {
            fechar()
        }
    }
}

package repositorio

import enumeradores.Cor
import enumeradores.Material
import produto.CaixaDaAgua
import java.sql.SQLException

/**
 * Repositório CRUD para Caixa d'Água (Produtos).
 * Permite inserção, edição, remoção, listagem e controle de estoque de caixas d'água no PostgreSQL.
 */
class CRUDCaixaDAgua : InterfaceJPA<CaixaDaAgua>, ConexaoPostgres() {

    override fun salvar(item: CaixaDaAgua) {
        try {
            if (!conectar()) return

            val sql = """
                INSERT INTO caixa_da_agua (marca, modelo, largura, altura, profundidade, cor, material, formato, preco, estoque_qtd)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.marca)
            stmt.setString(2, item.modelo)
            stmt.setDouble(3, item.dimensao[0])
            stmt.setDouble(4, item.dimensao[1])
            stmt.setDouble(5, item.dimensao[2])
            stmt.setString(6, item.cor.name)
            stmt.setString(7, item.material.name)
            stmt.setString(8, item.formato)
            stmt.setBigDecimal(9, item.preco)
            stmt.setInt(10, item.estoqueQtd)

            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Caixa d'Água ${item.marca} - ${item.modelo} salva no banco de dados!")
        } catch (e: SQLException) {
            println("[ERRO AO SALVAR CAIXA D'ÁGUA] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun listar() {
        try {
            if (!conectar()) return

            val sql = "SELECT * FROM caixa_da_agua ORDER BY id ASC"
            val stmt = c!!.createStatement()
            val rs = stmt.executeQuery(sql)

            println("\n================ CATÁLOGO DE CAIXAS D'ÁGUA ================")
            while (rs.next()) {
                val id = rs.getInt("id")
                val marca = rs.getString("marca")
                val modelo = rs.getString("modelo")
                val larg = rs.getDouble("largura")
                val alt = rs.getDouble("altura")
                val prof = rs.getDouble("profundidade")
                val cor = rs.getString("cor")
                val mat = rs.getString("material")
                val formato = rs.getString("formato")
                val preco = rs.getBigDecimal("preco")
                val estoque = rs.getInt("estoque_qtd")

                println("ID: $id | Marca: $marca | Modelo: $modelo | Formato: $formato")
                println("   Dimensões (LxAxP): ${larg}m x ${alt}m x ${prof}m | Cor: $cor | Material: $mat")
                println("   Preço: R$ $preco | Estoque Disponível: $estoque unidades")
                println("------------------------------------------------------------------")
            }
            println("=========================================================\n")
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO LISTAR CAIXAS D'ÁGUA] ${e.message}")
        } finally {
            fechar()
        }
    }

    /**
     * Busca uma Caixa d'Água específica pelo seu ID.
     */
    fun buscarPorId(id: Int): CaixaDaAgua? {
        try {
            if (!conectar()) return null

            val sql = "SELECT * FROM caixa_da_agua WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val marca = rs.getString("marca")
                val modelo = rs.getString("modelo")
                val dim = mutableListOf(rs.getDouble("largura"), rs.getDouble("altura"), rs.getDouble("profundidade"))
                val cor = Cor.valueOf(rs.getString("cor"))
                val mat = Material.valueOf(rs.getString("material"))
                val formato = rs.getString("formato")
                val preco = rs.getBigDecimal("preco")
                val estoque = rs.getInt("estoque_qtd")

                stmt.close()
                return CaixaDaAgua(id = id, marca = marca, modelo = modelo, dimensao = dim, cor = cor, material = mat, formato = formato, preco = preco, estoqueQtd = estoque)
            }
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO BUSCAR CAIXA D'ÁGUA POR ID] ${e.message}")
        } finally {
            fechar()
        }
        return null
    }

    /**
     * Atualiza a quantidade disponível em estoque de uma caixa d'água.
     */
    fun atualizarEstoque(id: Int, novaQtd: Int): Boolean {
        try {
            if (!conectar()) return false

            val sql = "UPDATE caixa_da_agua SET estoque_qtd = ? WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, novaQtd)
            stmt.setInt(2, id)
            val linhasAfetadas = stmt.executeUpdate()
            stmt.close()

            return linhasAfetadas > 0
        } catch (e: SQLException) {
            println("[ERRO AO ATUALIZAR ESTOQUE] ${e.message}")
            return false
        } finally {
            fechar()
        }
    }

    override fun editar(item: CaixaDaAgua, id: Int) {
        try {
            if (!conectar()) return

            val sql = """
                UPDATE caixa_da_agua 
                SET marca = ?, modelo = ?, largura = ?, altura = ?, profundidade = ?, cor = ?, material = ?, formato = ?, preco = ?, estoque_qtd = ?
                WHERE id = ?
            """.trimIndent()

            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.marca)
            stmt.setString(2, item.modelo)
            stmt.setDouble(3, item.dimensao[0])
            stmt.setDouble(4, item.dimensao[1])
            stmt.setDouble(5, item.dimensao[2])
            stmt.setString(6, item.cor.name)
            stmt.setString(7, item.material.name)
            stmt.setString(8, item.formato)
            stmt.setBigDecimal(9, item.preco)
            stmt.setInt(10, item.estoqueQtd)
            stmt.setInt(11, id)

            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Caixa d'Água ID $id atualizada no banco de dados.")
        } catch (e: SQLException) {
            println("[ERRO AO EDITAR CAIXA D'ÁGUA] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun excluir(id: Int) {
        try {
            if (!conectar()) return

            val sql = "DELETE FROM caixa_da_agua WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            val afetadas = stmt.executeUpdate()
            stmt.close()

            if (afetadas > 0) {
                println("[SUCESSO] Caixa d'Água ID $id removida com sucesso.")
            } else {
                println("[AVISO] Nenhuma caixa d'água encontrada com o ID $id.")
            }
        } catch (e: SQLException) {
            println("[ERRO AO EXCLUIR CAIXA D'ÁGUA] ${e.message}")
        } finally {
            fechar()
        }
    }
}

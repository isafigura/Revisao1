package repositorio

import enumeradores.CargoUsuario
import enumeradores.Habilidade
import enumeradores.TipoPessoa
import enumeradores.Turno
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Instalador
import pessoas.Pessoa
import java.sql.SQLException

/**
 * Repositório CRUD para Pessoas (Funcionários, Clientes, Fornecedores, Auditores).
 * Permite cadastrar, listar e buscar pessoas por ID ou CPF no banco PostgreSQL.
 */
class CRUDPessoa : InterfaceJPA<Pessoa>, ConexaoPostgres() {

    override fun salvar(item: Pessoa) {
        try {
            if (!conectar()) return

            val sql = """
                INSERT INTO pessoa (nome, cpf_cnpj, idade, tipo, cargo, salario, turno, habilidade, dividas_abertas)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.nome)
            stmt.setString(2, item.cpf)
            stmt.setInt(3, item.idade)
            stmt.setString(4, item.tipo.name)

            if (item is Funcionario) {
                stmt.setString(5, item.cargo.name)
                stmt.setBigDecimal(6, item.salario)
                if (item is Instalador) {
                    stmt.setString(7, item.turno.name)
                    stmt.setString(8, item.habilidade.name)
                } else {
                    stmt.setNull(7, java.sql.Types.VARCHAR)
                    stmt.setNull(8, java.sql.Types.VARCHAR)
                }
                stmt.setBoolean(9, false)
            } else if (item is Cliente) {
                stmt.setNull(5, java.sql.Types.VARCHAR)
                stmt.setNull(6, java.sql.Types.NUMERIC)
                stmt.setNull(7, java.sql.Types.VARCHAR)
                stmt.setNull(8, java.sql.Types.VARCHAR)
                stmt.setBoolean(9, item.dividasAbertas)
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR)
                stmt.setNull(6, java.sql.Types.NUMERIC)
                stmt.setNull(7, java.sql.Types.VARCHAR)
                stmt.setNull(8, java.sql.Types.VARCHAR)
                stmt.setBoolean(9, false)
            }

            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Pessoa '${item.nome}' cadastrada com sucesso.")
        } catch (e: SQLException) {
            println("[ERRO AO SALVAR PESSOA] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun listar() {
        listarPorTipo(null)
    }

    /**
     * Lista pessoas cadastradas filtrando por Tipo (ou exibe todas se for nulo).
     */
    fun listarPorTipo(filtroTipo: TipoPessoa? = null) {
        try {
            if (!conectar()) return

            val sql = if (filtroTipo != null) {
                "SELECT * FROM pessoa WHERE tipo = '${filtroTipo.name}' ORDER BY id ASC"
            } else {
                "SELECT * FROM pessoa ORDER BY id ASC"
            }

            val stmt = c!!.createStatement()
            val rs = stmt.executeQuery(sql)

            println("\n================ LISTA DE PESSOAS ================")
            while (rs.next()) {
                val id = rs.getInt("id")
                val nome = rs.getString("nome")
                val cpf = rs.getString("cpf_cnpj")
                val idade = rs.getInt("idade")
                val tipo = rs.getString("tipo")
                val cargo = rs.getString("cargo")
                val salario = rs.getBigDecimal("salario")

                print("ID: $id | Nome: $nome | CPF: $cpf | Tipo: $tipo")
                if (cargo != null) print(" | Cargo: $cargo")
                if (salario != null) print(" | Salário: R$ $salario")
                println()
            }
            println("==================================================\n")
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO LISTAR PESSOAS] ${e.message}")
        } finally {
            fechar()
        }
    }

    /**
     * Busca um funcionário pelo ID.
     */
    fun buscarFuncionarioPorId(id: Int): Funcionario? {
        try {
            if (!conectar()) return null

            val sql = "SELECT * FROM pessoa WHERE id = ? AND tipo = 'FUNCIONARIO'"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val nome = rs.getString("nome")
                val cpf = rs.getString("cpf_cnpj")
                val idade = rs.getInt("idade")
                val cargoStr = rs.getString("cargo") ?: "VENDEDOR"
                val cargo = try { CargoUsuario.valueOf(cargoStr) } catch (e: Exception) { CargoUsuario.VENDEDOR }
                val salario = rs.getBigDecimal("salario") ?: java.math.BigDecimal.ZERO

                stmt.close()
                return Funcionario(id = id, nome = nome, cpf = cpf, idade = idade, cargo = cargo, salario = salario)
            }
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO BUSCAR FUNCIONÁRIO POR ID] ${e.message}")
        } finally {
            fechar()
        }
        return null
    }

    /**
     * Busca um cliente pelo ID.
     */
    fun buscarClientePorId(id: Int): Cliente? {
        try {
            if (!conectar()) return null

            val sql = "SELECT * FROM pessoa WHERE id = ? AND tipo = 'CLIENTE'"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val nome = rs.getString("nome")
                val cpf = rs.getString("cpf_cnpj")
                val idade = rs.getInt("idade")
                val dividas = rs.getBoolean("dividas_abertas")

                stmt.close()
                return Cliente(id = id, nomeCliente = nome, cpfCliente = cpf, idadeCliente = idade, dividasAbertas = dividas)
            }
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO BUSCAR CLIENTE POR ID] ${e.message}")
        } finally {
            fechar()
        }
        return null
    }

    override fun editar(item: Pessoa, id: Int) {
        try {
            if (!conectar()) return
            val sql = "UPDATE pessoa SET nome = ?, idade = ? WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.nome)
            stmt.setInt(2, item.idade)
            stmt.setInt(3, id)
            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Cadastro da pessoa ID $id atualizado.")
        } catch (e: SQLException) {
            println("[ERRO AO EDITAR PESSOA] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun excluir(id: Int) {
        try {
            if (!conectar()) return
            val sql = "DELETE FROM pessoa WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Pessoa ID $id excluída com sucesso.")
        } catch (e: SQLException) {
            println("[ERRO AO EXCLUIR PESSOA] ${e.message}")
        } finally {
            fechar()
        }
    }
}

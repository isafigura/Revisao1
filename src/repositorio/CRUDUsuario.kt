package repositorio

import enumeradores.CargoUsuario
import pessoas.Funcionario
import pessoas.Usuario
import java.sql.SQLException

/**
 * Repositório CRUD para Usuários (Controle de Login e Senha).
 * Responsável por autenticar acessos e gerenciar contas de acesso ao console.
 */
class CRUDUsuario : InterfaceJPA<Usuario>, ConexaoPostgres() {

    /**
     * Valida as credenciais de login (username e senha).
     * Retorna a entidade Usuario com seu cargo e funcionário associado, ou null se inválido.
     */
    fun autenticar(usernameEntrada: String, senhaEntrada: String): Usuario? {
        try {
            if (!conectar()) return null

            val sql = """
                SELECT u.id AS user_id, u.username, u.senha, u.cargo AS user_cargo,
                       p.id AS func_id, p.nome, p.cpf_cnpj, p.idade, p.salario
                FROM usuario u
                JOIN pessoa p ON u.funcionario_id = p.id
                WHERE u.username = ? AND u.senha = ?
            """.trimIndent()

            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, usernameEntrada)
            stmt.setString(2, senhaEntrada)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val userId = rs.getInt("user_id")
                val username = rs.getString("username")
                val senha = rs.getString("senha")
                val cargoStr = rs.getString("user_cargo")
                val cargo = try { CargoUsuario.valueOf(cargoStr) } catch (e: Exception) { CargoUsuario.VENDEDOR }

                val funcId = rs.getInt("func_id")
                val nome = rs.getString("nome")
                val cpf = rs.getString("cpf_cnpj")
                val idade = rs.getInt("idade")
                val salario = rs.getBigDecimal("salario") ?: java.math.BigDecimal.ZERO

                val funcionario = Funcionario(id = funcId, nome = nome, cpf = cpf, idade = idade, cargo = cargo, salario = salario)
                val usuario = Usuario(id = userId, username = username, senha = senha, cargo = cargo, funcionario = funcionario)

                stmt.close()
                return usuario
            }
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO DE AUTENTICAÇÃO] ${e.message}")
        } finally {
            fechar()
        }
        return null
    }

    override fun salvar(item: Usuario) {
        try {
            if (!conectar()) return

            val sql = "INSERT INTO usuario (username, senha, cargo, funcionario_id) VALUES (?, ?, ?, ?)"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.username)
            stmt.setString(2, item.senha)
            stmt.setString(3, item.cargo.name)
            stmt.setInt(4, item.funcionario.id)

            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Usuário de login '${item.username}' criado com sucesso para o cargo ${item.cargo}.")
        } catch (e: SQLException) {
            println("[ERRO AO SALVAR USUÁRIO] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun listar() {
        try {
            if (!conectar()) return

            val sql = """
                SELECT u.id, u.username, u.cargo, p.nome
                FROM usuario u
                JOIN pessoa p ON u.funcionario_id = p.id
                ORDER BY u.id ASC
            """.trimIndent()

            val stmt = c!!.createStatement()
            val rs = stmt.executeQuery(sql)

            println("\n================ USUÁRIOS DE SISTEMA ================")
            while (rs.next()) {
                val id = rs.getInt("id")
                val username = rs.getString("username")
                val cargo = rs.getString("cargo")
                val funcionarioNome = rs.getString("nome")
                println("ID: $id | Login: $username | Grupo/Cargo: $cargo | Funcionario: $funcionarioNome")
            }
            println("=====================================================\n")
            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO LISTAR USUÁRIOS] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun editar(item: Usuario, id: Int) {
        try {
            if (!conectar()) return
            val sql = "UPDATE usuario SET senha = ?, cargo = ? WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, item.senha)
            stmt.setString(2, item.cargo.name)
            stmt.setInt(3, id)
            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Dados de acesso do usuário ID $id atualizados.")
        } catch (e: SQLException) {
            println("[ERRO AO EDITAR USUÁRIO] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun excluir(id: Int) {
        try {
            if (!conectar()) return
            val sql = "DELETE FROM usuario WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()
            stmt.close()
            println("[SUCESSO] Usuário ID $id excluído.")
        } catch (e: SQLException) {
            println("[ERRO AO EXCLUIR USUÁRIO] ${e.message}")
        } finally {
            fechar()
        }
    }
}

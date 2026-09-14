package pessoas

import enumeradores.CargoUsuario

/**
 * Entidade de Autenticação do Usuário no Console.
 * Associa as credenciais (username/senha) e o cargo (permissões) a um Funcionário cadastrado.
 */
data class Usuario(
    var id: Int = 0,
    val username: String,
    val senha: String,
    val cargo: CargoUsuario,
    val funcionario: Funcionario
)

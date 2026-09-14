package sistema

import pessoas.Usuario
import repositorio.CRUDUsuario
import util.Validador

/**
 * Módulo de Autenticação do Usuário via Console.
 * Solicita credenciais (usuário e senha) e retorna o Usuário autenticado contendo seu grupo/cargo.
 */
object Login {

    /**
     * Executa o loop de login interativo até que um usuário válido seja autenticado.
     */
    fun realizarLogin(): Usuario {
        val crudUsuario = CRUDUsuario()

        println("\n=======================================================")
        println("   SISTEMA DE GESTÃO DE CAIXAS D'ÁGUA E SERVIÇOS")
        println("               AUTENTICAÇÃO DE ACESSO")
        println("=======================================================")

        while (true) {
            val username = Validador.lerTexto("Usuário de login: ", "usuário")
            print("Senha: ")
            val senha = readlnOrNull()?.trim() ?: ""

            val usuarioAutenticado = crudUsuario.autenticar(username, senha)

            if (usuarioAutenticado != null) {
                println("\n[BEM-VINDO] Olá, ${usuarioAutenticado.funcionario.nome}!")
                println("[PERFIL LOGADO] Grupo: ${usuarioAutenticado.cargo}")
                return usuarioAutenticado
            } else {
                println("[ERRO] Usuário ou senha incorretos! Tente novamente.\n")
            }
        }
    }
}

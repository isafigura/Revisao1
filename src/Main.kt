import repositorio.ConexaoPostgres
import sistema.menuInicial

/**
 * Ponto de entrada principal da aplicação Kotlin.
 * Inicializa a conexão com o PostgreSQL, garante a existência do banco/tabelas e abre o menu de login.
 */
fun main() {
    println("Inicializando o Sistema de Gestão de Caixas d'Água e Serviços...")

    // 1. Inicializa o banco de dados e cria as tabelas automaticamente se não existirem
    val initDB = object : ConexaoPostgres() {}
    initDB.inicializarBancoDeDados()

    // 2. Abre o menu interativo via console
    menuInicial()
}

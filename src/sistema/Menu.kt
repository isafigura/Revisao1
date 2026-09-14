package sistema

import enumeradores.CargoUsuario
import pessoas.Usuario
import repositorio.CRUDCaixaDAgua
import repositorio.CRUDMovimentacao
import repositorio.CRUDServico
import sistema.caixadaagua.cadastrarNovaCaixa
import sistema.caixadaagua.editarCaixa
import sistema.caixadaagua.excluirCaixa
import sistema.caixadaagua.listarCaixa
import sistema.pagamentos.pagar
import sistema.pessoas.gerenciarPessoasMenu
import sistema.servicos.gerenciarServicosMenu
import sistema.vendas.registrarVenda
import util.Validador

/**
 * Menu Principal Dinâmico da Aplicação Console.
 * Direciona o usuário para as funcionalidades autorizadas de acordo com seu Cargo/Grupo de Login.
 */
fun menuInicial() {
    val usuarioLogado = Login.realizarLogin()

    when (usuarioLogado.cargo) {
        CargoUsuario.ADMINISTRADOR -> menuAdministrador(usuarioLogado)
        CargoUsuario.FINANCEIRO -> menuFinanceiro(usuarioLogado)
        CargoUsuario.VENDEDOR -> menuVendedor(usuarioLogado)
        CargoUsuario.INSTALADOR -> menuVendedor(usuarioLogado) // Vendedor/Instalador usam menu operacional
    }
}

/**
 * Menu Completo para o perfil ADMINISTRADOR.
 */
private fun menuAdministrador(usuario: Usuario) {
    val crudMovimentacao = CRUDMovimentacao()

    do {
        println("\n================ MENU ADMINISTRATIVO ================")
        println(" Logado como: ${usuario.funcionario.nome} [ADMINISTRADOR]")
        println("-----------------------------------------------------")
        println("1 - Registrar Nova Venda (Caixa d'Água / Instalação)")
        println("2 - Gerenciar Produtos (Cadastrar, Editar, Listar, Excluir Caixas d'Água)")
        println("3 - Gerenciar Serviços de Instalação")
        println("4 - Gerenciar Pessoas (Funcionários, Logins, Clientes, Fornecedores)")
        println("5 - Efetuar Pagamento / Despesa de Caixa")
        println("6 - Visualizar Relatório de Fluxo de Caixa (Movimentações)")
        println("0 - Sair / Fazer Logout")

        val op = Validador.lerOpcaoInt("Escolha uma opção: ", 0, 6)

        when (op) {
            1 -> registrarVenda(usuario)
            2 -> menuProdutos()
            3 -> gerenciarServicosMenu()
            4 -> gerenciarPessoasMenu()
            5 -> pagar(usuario)
            6 -> crudMovimentacao.listar()
            0 -> {
                println("Sessão encerrada com sucesso.")
                break
            }
        }
    } while (true)
}

/**
 * Menu Específico para o perfil FINANCEIRO.
 */
private fun menuFinanceiro(usuario: Usuario) {
    val crudMovimentacao = CRUDMovimentacao()
    val crudCaixa = CRUDCaixaDAgua()

    do {
        println("\n================ MENU FINANCEIRO ================")
        println(" Logado como: ${usuario.funcionario.nome} [FINANCEIRO]")
        println("-------------------------------------------------")
        println("1 - Visualizar Relatório de Fluxo de Caixa (Movimentações)")
        println("2 - Efetuar Pagamento de Salários / Fornecedores")
        println("3 - Consultar Estoque de Caixas d'Água")
        println("0 - Sair / Fazer Logout")

        val op = Validador.lerOpcaoInt("Escolha uma opção: ", 0, 3)

        when (op) {
            1 -> crudMovimentacao.listar()
            2 -> pagar(usuario)
            3 -> crudCaixa.listar()
            0 -> {
                println("Sessão encerrada.")
                break
            }
        }
    } while (true)
}

/**
 * Menu Específico para o perfil VENDEDOR.
 */
private fun menuVendedor(usuario: Usuario) {
    val crudCaixa = CRUDCaixaDAgua()
    val crudServico = CRUDServico()

    do {
        println("\n================ MENU DE VENDAS ================")
        println(" Logado como: ${usuario.funcionario.nome} [VENDEDOR]")
        println("------------------------------------------------")
        println("1 - Registrar Venda de Caixa d'Água / Serviço de Instalação")
        println("2 - Consultar Estoque Disponível de Caixas d'Água")
        println("3 - Consultar Catálogo de Serviços de Instalação")
        println("0 - Sair / Fazer Logout")

        val op = Validador.lerOpcaoInt("Escolha uma opção: ", 0, 3)

        when (op) {
            1 -> registrarVenda(usuario)
            2 -> crudCaixa.listar()
            3 -> crudServico.listar()
            0 -> {
                println("Sessão encerrada.")
                break
            }
        }
    } while (true)
}

/**
 * Submenu para gestão do catálogo de Caixas d'Água.
 */
private fun menuProdutos() {
    do {
        println("\n--- GESTÃO DE CAIXAS D'ÁGUA ---")
        println("1 - Cadastrar Nova Caixa d'Água")
        println("2 - Editar Caixa d'Água")
        println("3 - Listar Caixas d'Água e Estoque")
        println("4 - Excluir Caixa d'Água")
        println("0 - Voltar ao Menu Principal")

        val op = Validador.lerOpcaoInt("Escolha uma opção: ", 0, 4)

        when (op) {
            1 -> cadastrarNovaCaixa()
            2 -> editarCaixa()
            3 -> listarCaixa()
            4 -> excluirCaixa()
            0 -> break
        }
    } while (true)
}

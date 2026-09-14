package sistema.servicos

import produto.Servico
import repositorio.CRUDServico
import util.Validador

/**
 * Módulo de Gerenciamento do Catálogo de Serviços de Instalação.
 */
fun gerenciarServicosMenu() {
    val crudServico = CRUDServico()

    while (true) {
        println("\n================ GESTÃO DE SERVIÇOS DE INSTALAÇÃO ================")
        println("1 - Listar Serviços de Instalação")
        println("2 - Cadastrar Novo Serviço")
        println("3 - Excluir Serviço")
        println("0 - Voltar ao Menu Principal")

        val op = Validador.lerOpcaoInt("Escolha uma opção: ", 0, 3)

        when (op) {
            1 -> crudServico.listar()
            2 -> {
                println("\n--- CADASTRO DE NOVO SERVIÇO ---")
                val desc = Validador.lerTexto("Descrição do Serviço (ex: Instalação de Caixa 1000L): ", "descrição")
                val preco = Validador.lerBigDecimal("Preço cobrado pelo serviço (R$): ")

                val servico = Servico(descricao = desc, preco = preco)
                crudServico.salvar(servico)
            }
            3 -> {
                crudServico.listar()
                val id = Validador.lerIntPositivo("ID do Serviço a excluir: ")
                crudServico.excluir(id)
            }
            0 -> break
        }
    }
}

package sistema.vendas

import Financeiro.Movimentacao
import enumeradores.TipoMovimentacao
import pessoas.Cliente
import pessoas.Usuario
import repositorio.CRUDCaixaDAgua
import repositorio.CRUDMovimentacao
import repositorio.CRUDPessoa
import repositorio.CRUDServico
import util.Validador
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Módulo de Registro de Venda de Produtos e Serviços.
 * Atende ao requisito do usuário:
 * "vendedor (q registra venda de caixa de agua). quero vender caixa de agua e servico de instalçao apenas."
 * 
 * Atualiza o estoque no banco de dados e gera a movimentação financeira correspondente.
 */
fun registrarVenda(usuarioLogado: Usuario) {
    val crudCaixa = CRUDCaixaDAgua()
    val crudServico = CRUDServico()
    val crudPessoa = CRUDPessoa()
    val crudMovimentacao = CRUDMovimentacao()

    println("\n================ REGISTRO DE NOVA VENDA ================")

    // 1. Seleção ou Cadastro do Cliente
    crudPessoa.listarPorTipo(enumeradores.TipoPessoa.CLIENTE)
    var clienteId = Validador.lerIntPositivo("Digite o ID do Cliente (ou 0 para cadastrar um novo): ")

    var cliente: Cliente? = null
    if (clienteId == 0) {
        println("\n--- Cadastro Rápido de Cliente ---")
        val nome = Validador.lerTexto("Nome do Cliente: ", "nome")
        val cpf = Validador.lerCPF("CPF do Cliente: ")
        val idade = Validador.lerIntPositivo("Idade do Cliente: ")

        val novoCliente = Cliente(nomeCliente = nome, cpfCliente = cpf, idadeCliente = idade)
        crudPessoa.salvar(novoCliente)

        // Rebusca cliente pelo CPF
        crudPessoa.listarPorTipo(enumeradores.TipoPessoa.CLIENTE)
        clienteId = Validador.lerIntPositivo("Confirme o ID do cliente recém-cadastrado: ")
        cliente = crudPessoa.buscarClientePorId(clienteId)
    } else {
        cliente = crudPessoa.buscarClientePorId(clienteId)
    }

    if (cliente == null) {
        println("[ERRO] Cliente não encontrado! Operação de venda cancelada.")
        return
    }

    var totalVenda = BigDecimal.ZERO
    val descricaoItens = StringBuilder()

    // 2. Venda da Caixa d'Água (Produto)
    crudCaixa.listar()
    val caixaId = Validador.lerIntPositivo("Digite o ID da Caixa d'Água a vender (ou 0 para pular produto): ")

    if (caixaId > 0) {
        val caixa = crudCaixa.buscarPorId(caixaId)
        if (caixa == null) {
            println("[ERRO] Caixa d'água não encontrada!")
            return
        }

        if (caixa.estoqueQtd <= 0) {
            println("[ERRO] Caixa d'água '${caixa.marca} ${caixa.modelo}' está fora de estoque (Qtd: 0).")
            return
        }

        val quantidade = Validador.lerIntPositivo("Quantidade a vender (Disponível: ${caixa.estoqueQtd}): ", aceitarZero = false)
        if (quantidade > caixa.estoqueQtd) {
            println("[ERRO] Quantidade desejada maior do que o saldo em estoque!")
            return
        }

        val subtotalCaixa = caixa.preco.multiply(quantidade.toBigDecimal())
        totalVenda = totalVenda.add(subtotalCaixa)

        // Atualiza estoque no banco
        val novoEstoque = caixa.estoqueQtd - quantidade
        crudCaixa.atualizarEstoque(caixa.id, novoEstoque)

        descricaoItens.append("Venda de $quantidade x Caixa d'Água (${caixa.marca} - ${caixa.modelo}) [R$ $subtotalCaixa]; ")
    }

    // 3. Adicionar Serviço de Instalação
    println("\nDeseja incluir Serviço de Instalação?")
    println("1 - Sim")
    println("2 - Não")
    val opcaoServico = Validador.lerOpcaoInt("Escolha: ", 1, 2)

    if (opcaoServico == 1) {
        crudServico.listar()
        val servicoId = Validador.lerIntPositivo("Digite o ID do Serviço de Instalação (ou 0 para cancelar serviço): ")
        if (servicoId > 0) {
            val servico = crudServico.buscarPorId(servicoId)
            if (servico != null) {
                totalVenda = totalVenda.add(servico.preco)
                descricaoItens.append("Serviço de Instalação (${servico.descricao}) [R$ ${servico.preco}]; ")
            } else {
                println("[AVISO] Serviço ID $servicoId não encontrado. Prosseguindo sem serviço.")
            }
        }
    }

    if (totalVenda <= BigDecimal.ZERO) {
        println("[AVISO] Nenhum item ou serviço foi selecionado. Venda cancelada.")
        return
    }

    // 4. Registro da Movimentação Financeira no Banco de Dados
    val movimentacao = Movimentacao(
        valor = totalVenda,
        tipo = TipoMovimentacao.ENTRADA,
        pagador = cliente.nome,
        recebedor = "Empresa Caixas D'Água S.A.",
        dataHora = LocalDateTime.now(),
        descricao = descricaoItens.toString(),
        responsavel = usuarioLogado.funcionario
    )

    crudMovimentacao.salvar(movimentacao)

    println("\n================ VENDA CONCLUÍDA COM SUCESSO ================")
    println(" Cliente: ${cliente.nome}")
    println(" Total Pago: R$ $totalVenda")
    println(" Vendedor Responsável: ${usuarioLogado.funcionario.nome}")
    println("=============================================================\n")
}

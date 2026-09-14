package sistema.pagamentos

import Financeiro.Movimentacao
import enumeradores.TipoMovimentacao
import pessoas.Usuario
import repositorio.CRUDMovimentacao
import util.Validador
import java.time.LocalDateTime

/**
 * Módulo de Pagamento e Despesas de Caixa.
 * Permite efetuar pagamentos de funcionários (salários), fornecedores e despesas operacionais.
 * Salva a movimentação no banco de dados com os 6 parâmetros obrigatórios.
 */
fun pagar(usuarioLogado: Usuario) {
    val crudMovimentacao = CRUDMovimentacao()

    println("\n================ REGISTRO DE PAGAMENTO / DESPESA ================")
    println("Selecione o tipo de pagamento:")
    println("1 - Pagamento de Salário a Funcionário")
    println("2 - Pagamento a Fornecedor / Conta Operacional")
    val tipoOpcao = Validador.lerOpcaoInt("Escolha: ", 1, 2)

    val recebedor = if (tipoOpcao == 1) {
        Validador.lerTexto("Nome do Funcionário Recebedor: ", "recebedor")
    } else {
        Validador.lerTexto("Nome do Fornecedor ou Beneficiário: ", "recebedor")
    }

    val contexto = Validador.lerTexto("Motivo / Descrição do Pagamento: ", "descrição")
    val valor = Validador.lerBigDecimal("Valor do Pagamento (R$): ")

    val movimentacao = Movimentacao(
        valor = valor,
        tipo = TipoMovimentacao.SAIDA,
        pagador = "Empresa Caixas D'Água S.A.",
        recebedor = recebedor,
        dataHora = LocalDateTime.now(),
        descricao = contexto,
        responsavel = usuarioLogado.funcionario
    )

    crudMovimentacao.salvar(movimentacao)

    println("[SUCESSO] Pagamento de R$ $valor para '$recebedor' efetuado com sucesso!")
}
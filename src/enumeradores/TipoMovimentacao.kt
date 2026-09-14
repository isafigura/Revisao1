package enumeradores

/**
 * Enumerador que indica a direção do fluxo financeiro nas movimentações de caixa.
 */
enum class TipoMovimentacao {
    /**
     * Entrada de dinheiro no caixa (ex: recebimento por venda de produto ou prestação de serviço).
     */
    ENTRADA,

    /**
     * Saída de dinheiro do caixa (ex: pagamento de salário, pagamento a fornecedor, contas).
     */
    SAIDA
}

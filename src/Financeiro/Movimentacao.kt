package Financeiro

import enumeradores.TipoMovimentacao
import pessoas.Funcionario
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Entidade Movimentacao (Fluxo de Caixa).
 * Respeita estritamente os 6 atributos exigidos no requisito:
 * 1. Quanto dinheiro foi usado (valor)
 * 2. Quem foi o pagador (pagador)
 * 3. Quem recebeu (recebedor)
 * 4. A data e a hora (dataHora)
 * 5. O motivo/descrição (descricao)
 * 6. Responsável pela transação (responsavel)
 */
data class Movimentacao(
    var id: Int = 0,
    val valor: BigDecimal,
    val tipo: TipoMovimentacao,
    val pagador: String,
    val recebedor: String,
    val dataHora: LocalDateTime = LocalDateTime.now(),
    val descricao: String,
    val responsavel: Funcionario
)
package Financeiro

import java.math.BigDecimal

/**
 * Controle de Caixa Encapsulado.
 * Aplica os conceitos de encapsulamento protegendo a mutabilidade direta do saldo.
 * Alterações no saldo somente ocorrem por métodos validados de entrada (receita) e saída (despesa).
 */
class Caixa private constructor(saldoInicial: BigDecimal) {

    // Saldo encapsulado (private setter)
    var saldo: BigDecimal = saldoInicial
        private set

    /**
     * Registra uma entrada (receita) no saldo do caixa de forma segura.
     */
    fun registrarEntrada(valor: BigDecimal) {
        require(valor > BigDecimal.ZERO) { "O valor de entrada deve ser maior que zero." }
        this.saldo = this.saldo.add(valor)
    }

    /**
     * Registra uma saída (despesa) do saldo do caixa de forma segura.
     */
    fun registrarSaida(valor: BigDecimal) {
        require(valor > BigDecimal.ZERO) { "O valor de saída deve ser maior que zero." }
        this.saldo = this.saldo.subtract(valor)
    }

    companion object {
        private var instancia: Caixa? = null

        /**
         * Padrão Singleton para manter uma instância única do Caixa durante a execução.
         */
        fun getInstancia(saldoInicial: BigDecimal = BigDecimal.ZERO): Caixa {
            if (instancia == null) {
                instancia = Caixa(saldoInicial)
            }
            return instancia!!
        }
    }
}
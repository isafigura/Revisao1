package util

import java.math.BigDecimal

/**
 * Utilitário de Validação de Entradas no Console.
 * Implementa validações robustas utilizando expressões regulares (REGEX),
 * blocos de tratamento de exceções (TRY/CATCH), tratamento de nulos (NULLABLE)
 * e o operador Elvis (?:) para evitar travamento da aplicação por falhas humanas.
 */
object Validador {

    /** Expressão regular para validação de CPF ou CNPJ formatado ou apenas dígitos */
    private val REGEX_CPF_CNPJ = Regex("""^(\d{3}\.?\d{3}\.?\d{3}-?\d{2}|\d{2}\.?\d{3}\.?\d{3}/?\d{4}-?\d{2})$""")

    /** Expressão regular para validar se o texto possui apenas dígitos */
    private val REGEX_APENAS_NUMEROS = Regex("""^\d+$""")

    /**
     * Lê um valor inteiro seguro dentro de um intervalo [min, max].
     * Trata o caso de entrada nula ou conversão inválida utilizando toIntOrNull() e operador Elvis (?:).
     */
    fun lerOpcaoInt(mensagem: String, min: Int, max: Int): Int {
        while (true) {
            print(mensagem)
            val entrada = readlnOrNull()?.trim()
            val opcao = entrada?.toIntOrNull()

            if (opcao != null && opcao in min..max) {
                return opcao
            }
            println("[ERRO] Opção inválida! Digite um número inteiro entre $min e $max.")
        }
    }

    /**
     * Lê uma String não nula e não vazia digitada pelo usuário.
     */
    fun lerTexto(mensagem: String, nomeCampo: String = "campo"): String {
        while (true) {
            print(mensagem)
            val entrada = readlnOrNull()?.trim()
            if (!entrada.isNullOrEmpty()) {
                return entrada
            }
            println("[ERRO] O $nomeCampo não pode ficar em branco.")
        }
    }

    /**
     * Lê e valida um documento CPF/CNPJ usando Regex.
     */
    fun lerCPF(mensagem: String): String {
        while (true) {
            print(mensagem)
            val documento = readlnOrNull()?.trim() ?: ""
            if (REGEX_CPF_CNPJ.matches(documento)) {
                return documento
            }
            println("[ERRO] CPF/CNPJ inválido! Digite no formato 000.000.000-00 ou apenas 11 dígitos.")
        }
    }

    /**
     * Lê um valor monetário seguro do console (BigDecimal).
     * Utiliza try/catch e toBigDecimalOrNull() com operador Elvis para evitar NumberFormatException.
     */
    fun lerBigDecimal(mensagem: String): BigDecimal {
        while (true) {
            print(mensagem)
            val entrada = readlnOrNull()?.trim()?.replace(",", ".") ?: "0"
            try {
                val valor = entrada.toBigDecimalOrNull()
                if (valor != null && valor >= BigDecimal.ZERO) {
                    return valor
                }
            } catch (e: Exception) {
                // Captura falhas inesperadas na conversão
            }
            println("[ERRO] Valor numérico/monetário inválido! Digite um valor maior ou igual a zero (ex: 150.00).")
        }
    }

    /**
     * Lê um valor Double positivo (ex: para dimensões de largura, altura, profundidade).
     */
    fun lerDoublePositivo(mensagem: String): Double {
        while (true) {
            print(mensagem)
            val entrada = readlnOrNull()?.trim()?.replace(",", ".") ?: "0"
            val valor = entrada.toDoubleOrNull()
            if (valor != null && valor > 0.0) {
                return valor
            }
            println("[ERRO] Digite um número válido maior que 0 (ex: 1.5).")
        }
    }

    /**
     * Lê um valor inteiro positivo (ex: para quantidade de itens no estoque ou IDs).
     */
    fun lerIntPositivo(mensagem: String, aceitarZero: Boolean = true): Int {
        while (true) {
            print(mensagem)
            val entrada = readlnOrNull()?.trim()
            val valor = entrada?.toIntOrNull()
            if (valor != null && (if (aceitarZero) valor >= 0 else valor > 0)) {
                return valor
            }
            println("[ERRO] Digite um número inteiro ${if (aceitarZero) "maior ou igual a 0" else "maior que 0"}.")
        }
    }
}

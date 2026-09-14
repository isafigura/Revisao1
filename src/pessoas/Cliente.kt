package pessoas

import enumeradores.TipoPessoa

/**
 * Subclasse que representa os Clientes da empresa.
 * Herda de Pessoa e controla a situação de débitos/dívidas abertas.
 */
class Cliente(
    id: Int = 0,
    nomeCliente: String,
    cpfCliente: String,
    idadeCliente: Int,
    val dividasAbertas: Boolean = false
) : Pessoa(
    id = id,
    nome = nomeCliente,
    cpf = cpfCliente,
    idade = idadeCliente,
    tipo = TipoPessoa.CLIENTE
)

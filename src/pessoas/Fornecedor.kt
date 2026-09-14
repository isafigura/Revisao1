package pessoas

import enumeradores.TipoPessoa

/**
 * Subclasse que representa Fornecedores de produtos/caixas d'água e materiais.
 */
class Fornecedor(
    id: Int = 0,
    nome: String,
    cpfCnpj: String,
    idade: Int = 0
) : Pessoa(
    id = id,
    nome = nome,
    cpf = cpfCnpj,
    idade = idade,
    tipo = TipoPessoa.FORNECEDOR
)

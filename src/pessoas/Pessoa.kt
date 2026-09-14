package pessoas

import enumeradores.TipoPessoa

/**
 * Classe base (mãe) para representar qualquer indivíduo no sistema.
 * Atende ao requisito de abstração e herança para gerenciar as pessoas envolvidas no negócio.
 */
open class Pessoa(
    var id: Int = 0,
    val nome: String,
    val cpf: String,
    val idade: Int,
    val tipo: TipoPessoa
)

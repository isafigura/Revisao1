package produto

import enumeradores.Cor
import enumeradores.Material
import java.math.BigDecimal

/**
 * Entidade Produto: Caixa d'Água.
 * Contém especificações físicas, preço e quantidade em estoque.
 */
class CaixaDaAgua(
    var id: Int = 0,
    val marca: String,
    val modelo: String,
    val dimensao: MutableList<Double>, // [largura, altura, profundidade]
    val cor: Cor,
    val material: Material,
    val formato: String,
    val preco: BigDecimal,
    var estoqueQtd: Int = 0
)

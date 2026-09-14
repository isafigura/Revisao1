package produto

import pessoas.Instalador
import java.math.BigDecimal

/**
 * Entidade Serviço: Serviço de Instalação/Manutenção de Caixa d'Água.
 * Representa os serviços oferecidos aos clientes com associação ao instalador responsável.
 */
class Servico(
    var id: Int = 0,
    val descricao: String,
    val preco: BigDecimal,
    val instalador: Instalador? = null
)
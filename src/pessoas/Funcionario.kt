package pessoas

import enumeradores.CargoUsuario
import enumeradores.TipoPessoa
import java.math.BigDecimal

/**
 * Classe que representa os Funcionários da empresa.
 * Herda de Pessoa e estende com informações funcionais como cargo e salário.
 */
open class Funcionario(
    id: Int = 0,
    nome: String,
    cpf: String,
    idade: Int,
    val cargo: CargoUsuario,
    val salario: BigDecimal
) : Pessoa(
    id = id,
    nome = nome,
    cpf = cpf,
    idade = idade,
    tipo = TipoPessoa.FUNCIONARIO
)

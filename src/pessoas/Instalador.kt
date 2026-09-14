package pessoas

import enumeradores.CargoUsuario
import enumeradores.Habilidade
import enumeradores.Turno
import java.math.BigDecimal

/**
 * Subclasse especialista que representa o Instalador Técnico de Caixas d'Água.
 * Herda de Funcionario, contendo turno e habilidades técnicas.
 */
class Instalador(
    id: Int = 0,
    nome: String,
    cpf: String,
    idade: Int,
    salario: BigDecimal,
    var turno: Turno,
    var habilidade: Habilidade
) : Funcionario(
    id = id,
    nome = nome,
    cpf = cpf,
    idade = idade,
    cargo = CargoUsuario.INSTALADOR,
    salario = salario
)

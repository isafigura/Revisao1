package sistema.caixadaagua

import enumeradores.Cor
import enumeradores.Material
import produto.CaixaDaAgua
import repositorio.CRUDCaixaDAgua
import util.Validador

/**
 * Menu interativo para alterar os dados de uma Caixa d'Água cadastrada no banco.
 */
fun editarCaixa() {
    val crudCaixaDAgua = CRUDCaixaDAgua()
    crudCaixaDAgua.listar()

    val id = Validador.lerIntPositivo("Digite o ID da caixa que deseja alterar (ou 0 para cancelar): ")
    if (id == 0) return

    val caixaExistente = crudCaixaDAgua.buscarPorId(id)
    if (caixaExistente == null) {
        println("[ERRO] Nenhuma caixa d'água encontrada com o ID $id.")
        return
    }

    println("\n--- ALTERAR DADOS DA CAIXA ID $id ---")
    val marca = Validador.lerTexto("Digite a nova marca (${caixaExistente.marca}): ", "marca")
    val modelo = Validador.lerTexto("Digite o novo modelo (${caixaExistente.modelo}): ", "modelo")
    val formato = Validador.lerTexto("Digite o novo formato (${caixaExistente.formato}): ", "formato")

    val largura = Validador.lerDoublePositivo("Digite a nova largura (metros): ")
    val altura = Validador.lerDoublePositivo("Digite a nova altura (metros): ")
    val profundidade = Validador.lerDoublePositivo("Digite a nova profundidade (metros): ")
    val dimensao = mutableListOf(largura, altura, profundidade)

    println("\nEscolha a nova cor:")
    Cor.entries.forEach { cor ->
        println("${cor.ordinal} - ${cor.name}")
    }
    val corIndex = Validador.lerOpcaoInt("Opção de cor: ", 0, Cor.entries.size - 1)

    println("\nEscolha o novo material:")
    Material.entries.forEach { mat ->
        println("${mat.ordinal} - ${mat.name}")
    }
    val matIndex = Validador.lerOpcaoInt("Opção de material: ", 0, Material.entries.size - 1)

    val preco = Validador.lerBigDecimal("Digite o novo preço (R$): ")
    val estoque = Validador.lerIntPositivo("Digite a nova quantidade em estoque: ")

    crudCaixaDAgua.editar(
        CaixaDaAgua(
            id = id,
            marca = marca,
            modelo = modelo,
            formato = formato,
            dimensao = dimensao,
            preco = preco,
            cor = Cor.entries[corIndex],
            material = Material.entries[matIndex],
            estoqueQtd = estoque
        ),
        id
    )
}

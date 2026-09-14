package sistema.caixadaagua

import enumeradores.Cor
import enumeradores.Material
import produto.CaixaDaAgua
import repositorio.CRUDCaixaDAgua
import util.Validador

/**
 * Menu interativo para cadastro de uma nova Caixa d'Água no banco de dados.
 * Aplica validações de formulário via utilitário a prova de falhas humanas.
 */
fun cadastrarNovaCaixa() {
    println("\n--- CADASTRAR NOVA CAIXA D'ÁGUA ---")
    val marca = Validador.lerTexto("Digite a marca: ", "marca")
    val modelo = Validador.lerTexto("Digite o modelo: ", "modelo")
    val largura = Validador.lerDoublePositivo("Digite a largura (metros): ")
    val altura = Validador.lerDoublePositivo("Digite a altura (metros): ")
    val profundidade = Validador.lerDoublePositivo("Digite a profundidade (metros): ")
    val dimensao = mutableListOf(largura, altura, profundidade)

    println("\nEscolha a cor:")
    Cor.entries.forEach { cor ->
        println("${cor.ordinal} - ${cor.name}")
    }
    val corIndex = Validador.lerOpcaoInt("Opção de cor: ", 0, Cor.entries.size - 1)
    val cor = Cor.entries[corIndex]

    println("\nEscolha o material:")
    Material.entries.forEach { mat ->
        println("${mat.ordinal} - ${mat.name}")
    }
    val matIndex = Validador.lerOpcaoInt("Opção de material: ", 0, Material.entries.size - 1)
    val material = Material.entries[matIndex]

    val formato = Validador.lerTexto("Qual é o formato (ex: Cilíndrica, Retangular)? ", "formato")
    val preco = Validador.lerBigDecimal("Qual é o preço (R$)? ")
    val estoqueQtd = Validador.lerIntPositivo("Quantidade inicial em estoque: ")

    val conexao = CRUDCaixaDAgua()
    conexao.salvar(
        CaixaDaAgua(
            marca = marca,
            modelo = modelo,
            dimensao = dimensao,
            cor = cor,
            material = material,
            formato = formato,
            preco = preco,
            estoqueQtd = estoqueQtd
        )
    )
}
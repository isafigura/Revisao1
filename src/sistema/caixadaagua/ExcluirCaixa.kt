package sistema.caixadaagua

import repositorio.CRUDCaixaDAgua
import util.Validador

/**
 * Menu interativo para remoção de uma caixa d'água do banco de dados por ID.
 */
fun excluirCaixa() {
    val crudCaixaDAgua = CRUDCaixaDAgua()
    crudCaixaDAgua.listar()

    val id = Validador.lerIntPositivo("Digite o ID da caixa d'água a excluir (ou 0 para cancelar): ")
    if (id == 0) return

    print("Tem certeza que deseja excluir a caixa d'água ID $id? (S/N): ")
    val confirma = readlnOrNull()?.trim()?.uppercase()
    if (confirma == "S" || confirma == "SIM") {
        crudCaixaDAgua.excluir(id)
    } else {
        println("[CANCELADO] Operação de exclusão cancelada pelo usuário.")
    }
}

package sistema.caixadaagua

import repositorio.CRUDCaixaDAgua

/**
 * Exibe no console todas as caixas d'água cadastradas e seu saldo de estoque.
 */
fun listarCaixa() {
    val crudCaixaDAgua = CRUDCaixaDAgua()
    crudCaixaDAgua.listar()
}
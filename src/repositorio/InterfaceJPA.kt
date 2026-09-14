package repositorio

/**
 * Interface Genérica (Contrato de Repositorio CRUD).
 * Define as operações básicas de persistência de dados (Salvar, Listar, Editar, Excluir).
 */
interface InterfaceJPA<T> {
    /**
     * Salva ou insere uma nova entidade no banco de dados.
     */
    fun salvar(item: T)

    /**
     * Lista todos os registros salvos da entidade no console.
     */
    fun listar()

    /**
     * Edita um registro existente pelo seu identificador (ID).
     */
    fun editar(item: T, id: Int)

    /**
     * Remove um registro do banco de dados pelo seu identificador (ID).
     */
    fun excluir(id: Int)
}
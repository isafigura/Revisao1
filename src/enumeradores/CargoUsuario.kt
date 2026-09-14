package enumeradores

/**
 * Enumerador que representa os diferentes cargos/grupos de login dos usuários no sistema.
 * Utilizado para controle de permissões nos menus interativos.
 */
enum class CargoUsuario {
    /**
     * Possui acesso completo a todas as funcionalidades: cadastros, relatórios, estoque e fluxo financeiro.
     */
    ADMINISTRADOR,

    /**
     * Possui acesso a relatórios financeiros, lançamentos de pagamentos/despesas e histórico de movimentações.
     */
    FINANCEIRO,

    /**
     * Possui acesso ao registro de vendas de caixas d'água, contratação de serviços de instalação e consulta de estoque.
     */
    VENDEDOR,

    /**
     * Funcionário responsável pelas instalações técnicas dos produtos.
     */
    INSTALADOR
}

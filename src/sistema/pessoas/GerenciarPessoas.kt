package sistema.pessoas

import enumeradores.CargoUsuario
import enumeradores.TipoPessoa
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import pessoas.Usuario
import repositorio.CRUDPessoa
import repositorio.CRUDUsuario
import util.Validador

/**
 * Módulo de Gestão de Pessoas (Funcionários, Clientes, Fornecedores e Auditores).
 * Permite cadastro e listagem, associando credenciais de login para funcionários.
 */
fun gerenciarPessoasMenu() {
    val crudPessoa = CRUDPessoa()
    val crudUsuario = CRUDUsuario()

    while (true) {
        println("\n================ GESTÃO DE PESSOAS ================")
        println("1 - Listar Todas as Pessoas")
        println("2 - Cadastrar Funcionário (+ Criar Login de Acesso)")
        println("3 - Cadastrar Cliente")
        println("4 - Cadastrar Fornecedor")
        println("5 - Cadastrar Auditor")
        println("0 - Voltar ao Menu Principal")

        val op = Validador.lerOpcaoInt("Escolha uma opção: ", 0, 5)

        when (op) {
            1 -> crudPessoa.listar()
            2 -> {
                println("\n--- CADASTRO DE FUNCIONÁRIO ---")
                val nome = Validador.lerTexto("Nome Completo: ", "nome")
                val cpf = Validador.lerCPF("CPF: ")
                val idade = Validador.lerIntPositivo("Idade: ")

                println("Selecione o Cargo do Funcionário:")
                CargoUsuario.entries.forEach { cargo ->
                    println("${cargo.ordinal} - ${cargo.name}")
                }
                val idxCargo = Validador.lerOpcaoInt("Opção de Cargo: ", 0, CargoUsuario.entries.size - 1)
                val cargo = CargoUsuario.entries[idxCargo]

                val salario = Validador.lerBigDecimal("Salário (R$): ")

                val func = Funcionario(nome = nome, cpf = cpf, idade = idade, cargo = cargo, salario = salario)
                crudPessoa.salvar(func)

                // Busca o ID gerado para criar o login
                crudPessoa.listarPorTipo(TipoPessoa.FUNCIONARIO)
                val funcId = Validador.lerIntPositivo("Confirme o ID do funcionário criado para atribuir o login: ")
                val funcSalvo = crudPessoa.buscarFuncionarioPorId(funcId)

                if (funcSalvo != null) {
                    println("\n--- Criar Login de Acesso para o Console ---")
                    val username = Validador.lerTexto("Nome de usuário (Login): ", "usuário")
                    val senha = Validador.lerTexto("Senha: ", "senha")

                    val usuario = Usuario(username = username, senha = senha, cargo = cargo, funcionario = funcSalvo)
                    crudUsuario.salvar(usuario)
                }
            }
            3 -> {
                println("\n--- CADASTRO DE CLIENTE ---")
                val nome = Validador.lerTexto("Nome do Cliente: ", "nome")
                val cpf = Validador.lerCPF("CPF: ")
                val idade = Validador.lerIntPositivo("Idade: ")

                val cliente = Cliente(nomeCliente = nome, cpfCliente = cpf, idadeCliente = idade)
                crudPessoa.salvar(cliente)
            }
            4 -> {
                println("\n--- CADASTRO DE FORNECEDOR ---")
                val nome = Validador.lerTexto("Nome da Empresa / Fornecedor: ", "nome")
                val cnpj = Validador.lerCPF("CNPJ / CPF do Fornecedor: ")

                val fornecedor = Fornecedor(nome = nome, cpfCnpj = cnpj)
                crudPessoa.salvar(fornecedor)
            }
            5 -> {
                println("\n--- CADASTRO DE AUDITOR ---")
                val nome = Validador.lerTexto("Nome do Auditor: ", "nome")
                val cpf = Validador.lerCPF("CPF: ")
                val idade = Validador.lerIntPositivo("Idade: ")

                val auditor = Pessoa(nome = nome, cpf = cpf, idade = idade, tipo = TipoPessoa.AUDITOR)
                crudPessoa.salvar(auditor)
            }
            0 -> break
        }
    }
}

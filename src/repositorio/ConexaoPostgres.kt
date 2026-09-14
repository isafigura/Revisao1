package repositorio

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Classe abstrata base para Conexão JDBC com o Banco de Dados PostgreSQL.
 * Fornece métodos para abrir/fechar conexão e executar o script de inicialização do banco.
 */
abstract class ConexaoPostgres(
    val user: String = "postgres",
    val senha: String = "postgres",
    val url: String = "jdbc:postgresql://localhost:5432/caixaDaAgua",
    var c: Connection? = null
) {

    /**
     * Estabelece a conexão JDBC com o servidor PostgreSQL.
     */
    fun conectar(): Boolean {
        return try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager.getConnection(url, user, senha)
            true
        } catch (e: ClassNotFoundException) {
            println("[ERRO] Driver JDBC do PostgreSQL não foi encontrado no classpath.")
            false
        } catch (e: SQLException) {
            println("[ERRO DE CONEXÃO POSTGRESQL] ${e.message}")
            false
        }
    }

    /**
     * Encerra a conexão com o banco de dados de forma segura.
     */
    fun fechar() {
        try {
            if (c != null && !c!!.isClosed) {
                c!!.close()
            }
        } catch (e: SQLException) {
            println("[ERRO AO FECHAR CONEXÃO] ${e.message}")
        }
    }

    /**
     * Executa a inicialização do esquema do banco de dados (tabelas e usuário admin padrão)
     * a partir do arquivo schema.sql ou via comandos DDL diretos.
     */
    fun inicializarBancoDeDados() {
        if (!conectar()) return

        try {
            val ddlScript = """
                CREATE TABLE IF NOT EXISTS pessoa (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    cpf_cnpj VARCHAR(20) UNIQUE NOT NULL,
                    idade INT,
                    tipo VARCHAR(20) NOT NULL,
                    cargo VARCHAR(50),
                    salario NUMERIC(10, 2),
                    turno VARCHAR(20),
                    habilidade VARCHAR(50),
                    dividas_abertas BOOLEAN DEFAULT FALSE
                );

                CREATE TABLE IF NOT EXISTS usuario (
                    id SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    senha VARCHAR(100) NOT NULL,
                    cargo VARCHAR(20) NOT NULL,
                    funcionario_id INT REFERENCES pessoa(id) ON DELETE CASCADE
                );

                CREATE TABLE IF NOT EXISTS caixa_da_agua (
                    id SERIAL PRIMARY KEY,
                    marca VARCHAR(50) NOT NULL,
                    modelo VARCHAR(50) NOT NULL,
                    largura DOUBLE PRECISION NOT NULL,
                    altura DOUBLE PRECISION NOT NULL,
                    profundidade DOUBLE PRECISION NOT NULL,
                    cor VARCHAR(30) NOT NULL,
                    material VARCHAR(30) NOT NULL,
                    formato VARCHAR(30) NOT NULL,
                    preco NUMERIC(10, 2) NOT NULL,
                    estoque_qtd INT NOT NULL DEFAULT 0
                );

                CREATE TABLE IF NOT EXISTS servico (
                    id SERIAL PRIMARY KEY,
                    descricao VARCHAR(100) NOT NULL,
                    preco NUMERIC(10, 2) NOT NULL,
                    instalador_id INT REFERENCES pessoa(id) ON DELETE SET NULL
                );

                CREATE TABLE IF NOT EXISTS movimentacao (
                    id SERIAL PRIMARY KEY,
                    valor NUMERIC(10, 2) NOT NULL,
                    tipo VARCHAR(10) NOT NULL,
                    pagador VARCHAR(100) NOT NULL,
                    recebedor VARCHAR(100) NOT NULL,
                    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    descricao TEXT NOT NULL,
                    responsavel_id INT REFERENCES pessoa(id) NOT NULL
                );

                INSERT INTO pessoa (nome, cpf_cnpj, idade, tipo, cargo, salario)
                VALUES ('Administrador Geral', '000.000.000-00', 35, 'FUNCIONARIO', 'ADMINISTRADOR', 5000.00)
                ON CONFLICT (cpf_cnpj) DO NOTHING;

                INSERT INTO usuario (username, senha, cargo, funcionario_id)
                SELECT 'admin', '123', 'ADMINISTRADOR', id FROM pessoa WHERE cpf_cnpj = '000.000.000-00'
                ON CONFLICT (username) DO NOTHING;
            """.trimIndent()

            val stmt = c!!.createStatement()
            stmt.executeUpdate(ddlScript)
            stmt.close()
            println("[BANCO DE DADOS] Tabelas e carga inicial verificadas com sucesso.")
        } catch (e: SQLException) {
            println("[ERRO NA CRIAÇÃO DAS TABELAS] ${e.message}")
        } finally {
            fechar()
        }
    }
}
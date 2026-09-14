-- Script de Inicialização do Banco de Dados PostgreSQL (caixaDaAgua)

-- 1. Tabela Unificada de Pessoas (Funcionários, Clientes, Fornecedores, Auditores)
CREATE TABLE IF NOT EXISTS pessoa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE NOT NULL,
    idade INT,
    tipo VARCHAR(20) NOT NULL, -- 'FUNCIONARIO', 'CLIENTE', 'FORNECEDOR', 'AUDITOR'
    cargo VARCHAR(50),      -- 'ADMINISTRADOR', 'FINANCEIRO', 'VENDEDOR', 'INSTALADOR'
    salario NUMERIC(10, 2),
    turno VARCHAR(20),      -- 'MATUTINO', 'VESPERTINO', 'NOTURNO'
    habilidade VARCHAR(50), -- 'INSTALACAO', 'VENDAS', etc.
    dividas_abertas BOOLEAN DEFAULT FALSE
);

-- 2. Tabela de Usuários para Autenticação (Login com Usuário e Senha)
CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    cargo VARCHAR(20) NOT NULL, -- 'ADMINISTRADOR', 'FINANCEIRO', 'VENDEDOR'
    funcionario_id INT REFERENCES pessoa(id) ON DELETE CASCADE
);

-- 3. Tabela de Produtos (Caixa d'Água)
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

-- 4. Tabela de Serviços (Serviço de Instalação)
CREATE TABLE IF NOT EXISTS servico (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    instalador_id INT REFERENCES pessoa(id) ON DELETE SET NULL
);

-- 5. Tabela de Movimentações Financeiras (Fluxo de Caixa)
CREATE TABLE IF NOT EXISTS movimentacao (
    id SERIAL PRIMARY KEY,
    valor NUMERIC(10, 2) NOT NULL,
    tipo VARCHAR(10) NOT NULL, -- 'ENTRADA' ou 'SAIDA'
    pagador VARCHAR(100) NOT NULL,
    recebedor VARCHAR(100) NOT NULL,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descricao TEXT NOT NULL,
    responsavel_id INT REFERENCES pessoa(id) NOT NULL
);

-- Inserção de um Funcionário Admin Inicial (se não existir)
INSERT INTO pessoa (nome, cpf_cnpj, idade, tipo, cargo, salario)
VALUES ('Administrador Geral', '000.000.000-00', 35, 'FUNCIONARIO', 'ADMINISTRADOR', 5000.00)
ON CONFLICT (cpf_cnpj) DO NOTHING;

-- Inserção do Login Admin Inicial (usuario: admin, senha: 123)
INSERT INTO usuario (username, senha, cargo, funcionario_id)
SELECT 'admin', '123', 'ADMINISTRADOR', id FROM pessoa WHERE cpf_cnpj = '000.000.000-00'
ON CONFLICT (username) DO NOTHING;

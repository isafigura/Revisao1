package repositorio

import Financeiro.Caixa
import Financeiro.Movimentacao
import enumeradores.TipoMovimentacao
import java.sql.SQLException
import java.sql.Timestamp

/**
 * Repositório CRUD para Movimentações Financeiras (Fluxo de Caixa).
 * Salva e lista as transações contendo estritamente os 6 campos obrigatórios:
 * 1. Quanto dinheiro foi usado (valor)
 * 2. Quem foi o pagador (pagador)
 * 3. Quem recebeu (recebedor)
 * 4. Data e Hora da transação (data_hora)
 * 5. Motivo / Descrição (descricao)
 * 6. Responsável pela transação (responsavel_id)
 */
class CRUDMovimentacao : InterfaceJPA<Movimentacao>, ConexaoPostgres() {

    override fun salvar(item: Movimentacao) {
        try {
            if (!conectar()) return

            val sql = """
                INSERT INTO movimentacao (valor, tipo, pagador, recebedor, data_hora, descricao, responsavel_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

            val stmt = c!!.prepareStatement(sql)
            stmt.setBigDecimal(1, item.valor)
            stmt.setString(2, item.tipo.name)
            stmt.setString(3, item.pagador)
            stmt.setString(4, item.recebedor)
            stmt.setTimestamp(5, Timestamp.valueOf(item.dataHora))
            stmt.setString(6, item.descricao)
            stmt.setInt(7, item.responsavel.id)

            stmt.executeUpdate()
            stmt.close()

            // Atualiza o saldo encapsulado do Caixa
            val caixa = Caixa.getInstancia()
            if (item.tipo == TipoMovimentacao.ENTRADA) {
                caixa.registrarEntrada(item.valor)
            } else {
                caixa.registrarSaida(item.valor)
            }

            println("[FLUXO DE CAIXA] Movimentação registrada: R$ ${item.valor} (${item.tipo.name})")
        } catch (e: SQLException) {
            println("[ERRO AO SALVAR MOVIMENTAÇÃO] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun listar() {
        try {
            if (!conectar()) return

            val sql = """
                SELECT m.id, m.valor, m.tipo, m.pagador, m.recebedor, m.data_hora, m.descricao, p.nome AS responsavel_nome
                FROM movimentacao m
                JOIN pessoa p ON m.responsavel_id = p.id
                ORDER BY m.data_hora DESC
            """.trimIndent()

            val stmt = c!!.createStatement()
            val rs = stmt.executeQuery(sql)

            println("\n================ RELATÓRIO DE FLUXO DE CAIXA ================")
            while (rs.next()) {
                val id = rs.getInt("id")
                val valor = rs.getBigDecimal("valor")
                val tipo = rs.getString("tipo")
                val pagador = rs.getString("pagador")
                val recebedor = rs.getString("recebedor")
                val dataHora = rs.getTimestamp("data_hora")
                val desc = rs.getString("descricao")
                val resp = rs.getString("responsavel_nome")

                println("ID: #$id | Tipo: $tipo | Valor: R$ $valor | Data/Hora: $dataHora")
                println("   Pagador: $pagador | Recebedor: $recebedor")
                println("   Motivo/Descrição: $desc")
                println("   Responsável que Efetuou: $resp")
                println("------------------------------------------------------------------------")
            }
            val saldoAtual = Caixa.getInstancia().saldo
            println(" SALDO ATUAL DO CAIXA EM MEMÓRIA: R$ $saldoAtual")
            println("========================================================================\n")

            stmt.close()
        } catch (e: SQLException) {
            println("[ERRO AO LISTAR MOVIMENTAÇÕES] ${e.message}")
        } finally {
            fechar()
        }
    }

    override fun editar(item: Movimentacao, id: Int) {
        println("[AVISO] Por razões de auditoria contábil, movimentações financeiras salvas não podem ser alteradas.")
    }

    override fun excluir(id: Int) {
        println("[AVISO] Por razões de auditoria contábil, movimentações financeiras não podem ser excluídas.")
    }
}

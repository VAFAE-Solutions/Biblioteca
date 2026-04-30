package org.example.demo.service;

import org.example.demo.dao.MultaDAO;
import org.example.demo.model.Emprestimo;
import org.example.demo.model.Multa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MultaService {

    private static final BigDecimal VALOR_DIARIO = new BigDecimal("2.00"); // R$ 2,00 por dia

    private final MultaDAO multaDAO;
    private final EmprestimoService emprestimoService;

    public MultaService() {
        this.multaDAO = new MultaDAO();
        this.emprestimoService = new EmprestimoService();
    }

    public boolean gerarMulta(int emprestimoId) {
        if (emprestimoId <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Emprestimo emprestimo = emprestimoService.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado.");
        }
        if (emprestimo.getStatus() != Emprestimo.Status.ATRASADO) {
            throw new IllegalStateException("Multa só pode ser gerada para empréstimos atrasados.");
        }

        // Verifica se já existe multa
        if (multaDAO.existeMultaPorEmprestimo(emprestimoId)) {
            throw new IllegalStateException("Já existe multa gerada para este empréstimo.");
        }

        // Calcula dias de atraso e valor
        long diasAtraso = ChronoUnit.DAYS.between(
                emprestimo.getDataDevolucaoPrevista(),
                LocalDate.now()
        );
        BigDecimal valor = VALOR_DIARIO.multiply(BigDecimal.valueOf(diasAtraso));

        Multa multa = new Multa(
                emprestimoId,
                emprestimo.getUsuarioId(),
                valor,
                LocalDate.now()
        );

        return multaDAO.inserir(multa);
    }

    public boolean registrarPagamento(int multaId) {
        if (multaId <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Multa multa = multaDAO.buscarPorId(multaId);
        if (multa == null) {
            throw new IllegalArgumentException("Multa não encontrada.");
        }
        if (multa.isPago()) {
            throw new IllegalStateException("Multa já foi paga.");
        }

        multa.setPago(true);
        multa.setDataPagamento(LocalDate.now());

        return multaDAO.atualizar(multa);
    }

    public List<Multa> buscarPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }
        return multaDAO.buscarPorUsuario(usuarioId);
    }

    public List<Multa> buscarPendentes(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }
        return multaDAO.buscarPendentesPorUsuario(usuarioId);
    }

    public boolean usuarioPossuiMultaPendente(int usuarioId) {
        return !buscarPendentes(usuarioId).isEmpty();
    }

    // ✅ Calcula valor total de multas pendentes — aproveitado do projeto dela
    public double calcularTotalMultasPendentes(int usuarioId) {
        return buscarPendentes(usuarioId).stream()
                .mapToDouble(m -> m.getValor().doubleValue())
                .sum();
    }
}
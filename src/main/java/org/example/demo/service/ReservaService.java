package org.example.demo.service;

import org.example.demo.dao.ReservaDAO;
import org.example.demo.model.FilaReserva;
import org.example.demo.model.Reserva;

import java.time.LocalDate;
import java.util.List;

public class ReservaService {

    private final ReservaDAO reservaDAO;
    private final MultaService multaService;

    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.multaService = new MultaService();
    }

    public boolean realizarReserva(int livroId, int usuarioId, int unidadeId) {
        if (livroId <= 0 || usuarioId <= 0 || unidadeId <= 0) {
            throw new IllegalArgumentException("IDs inválidos.");
        }

        // Usuário com multa pendente não pode reservar
        if (multaService.usuarioPossuiMultaPendente(usuarioId)) {
            throw new IllegalStateException("Usuário possui multa pendente. Quite antes de reservar.");
        }

        // Verifica se usuário já está na fila
        FilaReserva fila = reservaDAO.buscarFilaPorLivroEUnidade(livroId, unidadeId);
        if (fila.usuarioNaFila(usuarioId)) {
            throw new IllegalStateException("Usuário já possui reserva ativa para este livro nesta unidade.");
        }

        Reserva reserva = new Reserva(
                livroId,
                usuarioId,
                unidadeId,
                LocalDate.now()
        );
        reserva.setPosicaoFila(fila.proximaPosicao());

        return reservaDAO.inserir(reserva);
    }

    public boolean cancelarReserva(int reservaId) {
        if (reservaId <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Reserva reserva = reservaDAO.buscarPorId(reservaId);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não encontrada.");
        }
        if (reserva.getStatus() != Reserva.Status.AGUARDANDO) {
            throw new IllegalStateException("Apenas reservas aguardando podem ser canceladas.");
        }

        reserva.setStatus(Reserva.Status.CANCELADA);
        return reservaDAO.atualizar(reserva);
    }

    public boolean atenderProximaReserva(int livroId, int unidadeId) {
        FilaReserva fila = reservaDAO.buscarFilaPorLivroEUnidade(livroId, unidadeId);

        if (fila.isEmpty()) {
            return false;
        }

        Reserva proxima = fila.proximaReserva();
        proxima.setStatus(Reserva.Status.ATENDIDA);

        return reservaDAO.atualizar(proxima);
    }

    public FilaReserva buscarFila(int livroId, int unidadeId) {
        if (livroId <= 0 || unidadeId <= 0) {
            throw new IllegalArgumentException("IDs inválidos.");
        }
        return reservaDAO.buscarFilaPorLivroEUnidade(livroId, unidadeId);
    }

    public List<Reserva> buscarPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }
        return reservaDAO.buscarPorUsuario(usuarioId);
    }
}

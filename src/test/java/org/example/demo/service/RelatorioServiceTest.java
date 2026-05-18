package org.example.demo.service;

import org.example.demo.dao.RelatorioDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock private RelatorioDAO relatorioDAO;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    void livrosMaisEmprestados_deveRetornarLista() {
        when(relatorioDAO.livrosMaisEmprestados(10))
                .thenReturn(List.of(Map.of("titulo", "Clean Code", "total", 5)));

        var resultado = relatorioService.livrosMaisEmprestados();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(relatorioDAO).livrosMaisEmprestados(10);
    }

    @Test
    void livrosMaisEmprestados_semDados_deveRetornarListaVazia() {
        when(relatorioDAO.livrosMaisEmprestados(10)).thenReturn(List.of());

        var resultado = relatorioService.livrosMaisEmprestados();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void taxaAtraso_deveRetornarMapa() {
        when(relatorioDAO.taxaAtraso())
                .thenReturn(Map.of("total", 10, "atrasados", 2, "taxa", 0.2));

        var resultado = relatorioService.taxaAtraso();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(relatorioDAO).taxaAtraso();
    }

    @Test
    void taxaAtraso_semDados_deveRetornarMapaVazio() {
        when(relatorioDAO.taxaAtraso()).thenReturn(Map.of());

        var resultado = relatorioService.taxaAtraso();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void emprestimosPorUsuario_deveRetornarLista() {
        when(relatorioDAO.emprestimosPorUsuario(10))
                .thenReturn(List.of(Map.of("usuario", "Carlos Lima", "total", 3)));

        var resultado = relatorioService.emprestimosPorUsuario();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(relatorioDAO).emprestimosPorUsuario(10);
    }

    @Test
    void emprestimosPorUsuario_semDados_deveRetornarListaVazia() {
        when(relatorioDAO.emprestimosPorUsuario(10)).thenReturn(List.of());

        var resultado = relatorioService.emprestimosPorUsuario();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void livrosAtrasados_deveRetornarLista() {
        when(relatorioDAO.livrosAtrasados())
                .thenReturn(List.of(Map.of("titulo", "Clean Code", "diasAtraso", 3)));

        var resultado = relatorioService.livrosAtrasados();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(relatorioDAO).livrosAtrasados();
    }

    @Test
    void livrosAtrasados_semAtrasados_deveRetornarListaVazia() {
        when(relatorioDAO.livrosAtrasados()).thenReturn(List.of());

        var resultado = relatorioService.livrosAtrasados();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
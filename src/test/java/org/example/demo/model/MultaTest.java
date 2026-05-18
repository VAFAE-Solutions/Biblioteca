package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MultaTest {

    private Multa multa;

    private static final BigDecimal VALOR_10  = new BigDecimal("10.00");
    private static final BigDecimal VALOR_2   = new BigDecimal("2.00");
    private static final LocalDate  HOJE      = LocalDate.now();
    private static final LocalDate  PAGAMENTO = LocalDate.now().plusDays(3);

    @BeforeEach
    void setUp() {
        multa = new Multa(1, 1, VALOR_10, HOJE);
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        Multa m = new Multa(1, 2, VALOR_10, HOJE);

        assertEquals(1, m.getEmprestimoId());
        assertEquals(2, m.getUsuarioId());
        assertEquals(VALOR_10, m.getValor());
        assertEquals(HOJE, m.getDataGeracao());
        assertFalse(m.isPago(), "Multa nova deve estar pendente");
        assertNull(m.getDataPagamento(),
                "Data pagamento deve ser nula ao criar");
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        Multa m = new Multa(
                10, 1, 2, VALOR_10, true, HOJE, PAGAMENTO);

        assertEquals(10, m.getId());
        assertEquals(1, m.getEmprestimoId());
        assertEquals(2, m.getUsuarioId());
        assertEquals(VALOR_10, m.getValor());
        assertTrue(m.isPago());
        assertEquals(HOJE, m.getDataGeracao());
        assertEquals(PAGAMENTO, m.getDataPagamento());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Multa m = new Multa();

        assertNotNull(m);
        assertFalse(m.isPago(), "Multa nova deve estar pendente");
    }

    // ================================================================
    // Regra de negocio — valor diario
    // ================================================================

    @Test
    void valor_1Dia_deveSer2Reais() {
        // ✅ Documentado: R$2,00 por dia de atraso
        Multa m = new Multa(1, 1, VALOR_2, HOJE);
        assertEquals(new BigDecimal("2.00"), m.getValor());
    }

    @Test
    void valor_5Dias_deveSer10Reais() {
        // ✅ 5 dias * R$2,00 = R$10,00
        BigDecimal valorEsperado = VALOR_2.multiply(BigDecimal.valueOf(5));
        Multa m = new Multa(1, 1, valorEsperado, HOJE);
        assertEquals(new BigDecimal("10.00"), m.getValor());
    }

    @Test
    void valor_10Dias_deveSer20Reais() {
        // ✅ 10 dias * R$2,00 = R$20,00
        BigDecimal valorEsperado = VALOR_2.multiply(BigDecimal.valueOf(10));
        Multa m = new Multa(1, 1, valorEsperado, HOJE);
        assertEquals(new BigDecimal("20.00"), m.getValor());
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        multa.setId(42);
        assertEquals(42, multa.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        multa.setId(99);
        assertEquals(99, multa.getId());
    }

    // ================================================================
    // emprestimoId
    // ================================================================

    @Test
    void getEmprestimoId_deveRetornarIdDefinido() {
        assertEquals(1, multa.getEmprestimoId());
    }

    @Test
    void setEmprestimoId_deveAtualizarId() {
        multa.setEmprestimoId(5);
        assertEquals(5, multa.getEmprestimoId());
    }

    // ================================================================
    // usuarioId
    // ================================================================

    @Test
    void getUsuarioId_deveRetornarIdDefinido() {
        assertEquals(1, multa.getUsuarioId());
    }

    @Test
    void setUsuarioId_deveAtualizarId() {
        multa.setUsuarioId(7);
        assertEquals(7, multa.getUsuarioId());
    }

    // ================================================================
    // valor
    // ================================================================

    @Test
    void getValor_deveRetornarValorDefinido() {
        assertEquals(VALOR_10, multa.getValor());
    }

    @Test
    void setValor_deveAtualizarValor() {
        multa.setValor(new BigDecimal("20.00"));
        assertEquals(new BigDecimal("20.00"), multa.getValor());
    }

    // ================================================================
    // pago
    // ================================================================

    @Test
    void isPago_inicialmente_deveSerFalse() {
        assertFalse(multa.isPago(),
                "Multa nova deve estar pendente");
    }

    @Test
    void setPago_true_deveMarcarComoPaga() {
        multa.setPago(true);
        assertTrue(multa.isPago());
    }

    @Test
    void setPago_false_deveMarcarComoPendente() {
        multa.setPago(true);
        multa.setPago(false);
        assertFalse(multa.isPago());
    }

    // ================================================================
    // dataGeracao
    // ================================================================

    @Test
    void getDataGeracao_deveRetornarDataDefinida() {
        assertEquals(HOJE, multa.getDataGeracao());
    }

    @Test
    void setDataGeracao_deveAtualizarData() {
        LocalDate ontem = LocalDate.now().minusDays(1);
        multa.setDataGeracao(ontem);
        assertEquals(ontem, multa.getDataGeracao());
    }

    // ================================================================
    // dataPagamento
    // ================================================================

    @Test
    void getDataPagamento_inicialmente_deveSerNula() {
        assertNull(multa.getDataPagamento(),
                "Data pagamento deve ser nula enquanto nao paga");
    }

    @Test
    void setDataPagamento_deveAtualizarData() {
        multa.setDataPagamento(PAGAMENTO);
        assertEquals(PAGAMENTO, multa.getDataPagamento());
    }

    @Test
    void registrarPagamento_deveDefinirPagoEDataPagamento() {
        multa.setPago(true);
        multa.setDataPagamento(HOJE);

        assertTrue(multa.isPago());
        assertNotNull(multa.getDataPagamento());
        assertEquals(HOJE, multa.getDataPagamento());
    }

    // ================================================================
    // emprestimo
    // ================================================================

    @Test
    void getEmprestimo_inicialmente_deveSerNulo() {
        assertNull(multa.getEmprestimo());
    }

    @Test
    void setEmprestimo_deveAtualizarEmprestimoEEmprestimoId() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(5);

        multa.setEmprestimo(emprestimo);

        assertEquals(emprestimo, multa.getEmprestimo());
        assertEquals(5, multa.getEmprestimoId(),
                "EmprestimoId deve ser sincronizado ao setar emprestimo");
    }

    // ================================================================
    // usuario
    // ================================================================

    @Test
    void getUsuario_inicialmente_deveSerNulo() {
        assertNull(multa.getUsuario());
    }

    @Test
    void setUsuario_deveAtualizarUsuarioEUsuarioId() {
        Usuario usuario = new Usuario();
        usuario.setId(3);
        usuario.setNome("Carlos Lima");

        multa.setUsuario(usuario);

        assertEquals(usuario, multa.getUsuario());
        assertEquals(3, multa.getUsuarioId(),
                "UsuarioId deve ser sincronizado ao setar usuario");
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterCamposEssenciais() {
        multa.setId(1);

        String str = multa.toString();

        assertTrue(str.contains("10.00"), "Deve conter valor");
        assertTrue(str.contains("false"), "Deve conter pago");
    }
}
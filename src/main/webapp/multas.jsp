<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Minhas Multas - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; margin-top: 56px; }
        .topbar { background-color: #40c4d4; padding: 10px 20px; display: flex; align-items: center; justify-content: space-between; color: white; position: fixed; top: 0; left: 0; right: 0; z-index: 1000; height: 56px; }
        .sidebar { width: 220px; height: 100vh; background: #fff; border-right: 1px solid #ccc; position: fixed; top: 56px; padding-top: 20px; overflow-y: auto; }
        .sidebar a { display: block; padding: 15px; color: #333; text-decoration: none; border-bottom: 1px solid #ddd; }
        .sidebar a:hover { background-color: #f0f0f0; color: #40c4d4; font-weight: bold; }
        .sidebar a.active { background-color: #e8f7f8; color: #40c4d4; font-weight: bold; border-left: 4px solid #40c4d4; }
        .sidebar a.link-home { color: #40c4d4; font-size: 13px; }
        .content { margin-left: 240px; padding: 30px; }
        .table-container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }

        @media (max-width: 1024px) {
            .sidebar { width: 180px; top: 56px; }
            .content { margin-left: 195px; }
        }
        @media (max-width: 768px) {
            body { margin-top: 0 !important; }
            .topbar { flex-wrap: wrap !important; height: auto !important; gap: 8px; padding: 8px !important; }
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
                top: 0;
                display: flex;
                flex-wrap: wrap;
                border-right: none;
                border-bottom: 1px solid #ccc;
            }
            .sidebar a { padding: 8px 12px; font-size: 13px; border-bottom: none; border-right: 1px solid #ddd; }
            .content { margin-left: 0; padding: 15px; }
        }
        @media (max-width: 480px) {
            .sidebar a { width: 100%; font-size: 12px; }
            table { min-width: 500px; }
            .table-responsive { overflow-x: auto; }
        }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>
    <div>
        <span class="me-3"><strong>${usuarioLogado.nome}</strong></span>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Sair</a>
    </div>
</div>

<div class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard">🏠 Catálogo</a>
    <a href="${pageContext.request.contextPath}/perfil">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/meus-emprestimos">📖 Meus Empréstimos</a>
    <a href="${pageContext.request.contextPath}/minhas-reservas">🔖 Minhas Reservas</a>
    <a href="${pageContext.request.contextPath}/multas" class="active">💰 Multas</a>
    <a href="${pageContext.request.contextPath}/home" class="link-home">🌐 Página Inicial</a>
    <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;">🚪 Sair</a>
</div>

<div class="content">
    <div class="table-container">

        <h2 class="fw-bold text-primary mb-4">💰 Minhas Multas</h2>

        <c:if test="${totalMulta > 0}">
            <div class="alert alert-danger shadow-sm mb-4"
                 style="border-left: 5px solid #dc3545;">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h5 class="alert-heading mb-1">⚠️ Você possui multas pendentes</h5>
                        <span>Quite suas multas para realizar novos empréstimos e reservas.</span>
                    </div>
                    <div class="text-end">
                        <span class="fs-4 fw-bold">
                            Total: R$ <fmt:formatNumber value="${totalMulta}"
                                minFractionDigits="2" maxFractionDigits="2"/>
                        </span>
                    </div>
                </div>
                <%-- ✅ Aviso de pagamento presencial --%>
                <hr>
                <p class="mb-0 small">
                    🏛️ <strong>Como pagar:</strong>
                    O pagamento é realizado <strong>exclusivamente de forma presencial</strong>
                    nas unidades da biblioteca. Dirija-se ao balcão com seu documento,
                    efetue o pagamento e o bibliotecário registrará a quitação no sistema.
                    <a href="${pageContext.request.contextPath}/localizacao" class="alert-link">
                        Ver endereços das unidades →
                    </a>
                </p>
            </div>
        </c:if>

        <c:if test="${totalMulta == 0}">
            <div class="alert alert-success shadow-sm mb-4">
                ✅ Você não possui multas pendentes!
            </div>
        </c:if>

        <table class="table table-hover">
            <thead class="table-light">
            <tr>
                <th>Livro</th>
                <th>Data de Geração</th>
                <th>Valor</th>
                <th>Status</th>
                <th>Data de Pagamento</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="multa" items="${multas}">
                <tr class="${multa.pago ? '' : 'table-danger'}">
                    <td><strong>${multa.emprestimo.exemplar.livro.titulo}</strong></td>
                    <td>${multa.dataGeracao}</td>
                    <td>R$ <fmt:formatNumber value="${multa.valor}"
                            minFractionDigits="2" maxFractionDigits="2"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${multa.pago}">
                                <span class="badge bg-success">PAGO</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger">PENDENTE</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${not empty multa.dataPagamento ? multa.dataPagamento : '-'}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty multas}">
                <tr>
                    <td colspan="5" class="text-center py-5 text-muted">
                        Você não possui multas registradas.
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
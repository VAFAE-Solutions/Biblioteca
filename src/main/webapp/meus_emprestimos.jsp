<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Meus Empréstimos - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; margin-top: 56px; }
        .topbar {
            background-color: #40c4d4;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            color: white;
            position: fixed;
            top: 0; left: 0; right: 0;
            z-index: 1000;
            height: 56px;
        }
        .sidebar {
            width: 220px;
            height: 100vh;
            background: #fff;
            border-right: 1px solid #ccc;
            position: fixed;
            top: 56px;
            padding-top: 20px;
            overflow-y: auto;
        }
        .sidebar a { display: block; padding: 15px; color: #333; text-decoration: none; border-bottom: 1px solid #ddd; }
        .sidebar a:hover { background-color: #f0f0f0; color: #40c4d4; font-weight: bold; }
        .sidebar a.active { background-color: #e8f7f8; color: #40c4d4; font-weight: bold; border-left: 4px solid #40c4d4; }
        .sidebar a.link-home { color: #40c4d4; font-size: 13px; }
        .content { margin-left: 240px; padding: 30px; }
        .table-container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
        .alert-multa { border-left: 5px solid #dc3545; }
        .alert-atraso { border-left: 5px solid #fd7e14; }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>
    <div>
        <span class="me-3"><strong>${usuarioLogado.nome}</strong></span>
        <a href="${pageContext.request.contextPath}/logout"
           class="btn btn-danger btn-sm">Sair</a>
    </div>
</div>

<div class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard">🏠 Catálogo</a>
    <a href="${pageContext.request.contextPath}/perfil">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/meus-emprestimos" class="active">📖 Meus Empréstimos</a>
    <a href="${pageContext.request.contextPath}/minhas-reservas">🔖 Minhas Reservas</a>
    <a href="${pageContext.request.contextPath}/multas">💰 Multas</a>
    <a href="${pageContext.request.contextPath}/home" class="link-home">🌐 Página Inicial</a>
    <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;">🚪 Sair</a>
</div>

<div class="content">
    <div class="table-container">

        <h2 class="fw-bold text-primary mb-4">📖 Meus Empréstimos</h2>

        <c:if test="${temAtrasado}">
            <div class="alert alert-warning alert-atraso shadow-sm mb-4">
                <h5 class="alert-heading mb-1">⏰ Atenção! Devolução em Atraso</h5>
                <span>Você possui livros com prazo de devolução vencido.
                      Devolva o quanto antes para evitar multas adicionais.</span>
            </div>
        </c:if>

        <c:if test="${totalMulta > 0}">
            <div class="alert alert-danger alert-multa shadow-sm mb-4">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h5 class="alert-heading mb-1">⚠️ Multas por Atraso</h5>
                        <span>Você possui débitos pendentes referentes a livros não entregues no prazo.</span>
                    </div>
                    <div class="text-end">
                        <span class="fs-4 fw-bold">
                            R$ <fmt:formatNumber value="${totalMulta}"
                                minFractionDigits="2" maxFractionDigits="2"/>
                        </span>
                    </div>
                </div>
                <hr>
                <p class="mb-0 small">
                    🏛️ <strong>Pagamento presencial:</strong>
                    O pagamento de multas é realizado exclusivamente nas unidades da biblioteca.
                    Após quitar o débito no balcão, o bibliotecário registrará a quitação no sistema.
                    <a href="${pageContext.request.contextPath}/localizacao" class="alert-link">
                        Ver unidades →
                    </a>
                </p>
            </div>
        </c:if>

        <table class="table table-hover">
            <thead class="table-light">
            <tr>
                <th>Livro</th>
                <th>Data de Retirada</th>
                <th>Devolução Prevista</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="emp" items="${emprestimos}">
                <tr class="${emp.status == 'ATRASADO' ? 'table-danger' : ''}">
                    <td><strong>${emp.exemplar.livro.titulo}</strong></td>
                    <td>${emp.dataEmprestimo}</td>
                    <td class="${emp.status == 'ATRASADO' ? 'text-danger fw-bold' : ''}">
                        ${emp.dataDevolucaoPrevista}
                        <c:if test="${emp.status == 'ATRASADO'}">
                            <span class="badge bg-danger ms-1">VENCIDO</span>
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${emp.status == 'ATIVO'}">
                                <span class="badge bg-success">ATIVO</span>
                            </c:when>
                            <c:when test="${emp.status == 'ATRASADO'}">
                                <span class="badge bg-danger">ATRASADO</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">FINALIZADO</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty emprestimos}">
                <tr>
                    <td colspan="4" class="text-center py-5 text-muted">
                        Você não possui nenhum empréstimo ativo no momento.
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Minhas Reservas - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; }
        .topbar {
            background-color: #40c4d4;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            color: white;
        }
        .sidebar {
            width: 220px;
            height: 100vh;
            background: #fff;
            border-right: 1px solid #ccc;
            position: fixed;
            padding-top: 20px;
        }
        .sidebar a {
            display: block;
            padding: 15px;
            color: #333;
            text-decoration: none;
            border-bottom: 1px solid #ddd;
        }
        .sidebar a:hover {
            background-color: #f0f0f0;
            color: #40c4d4;
            font-weight: bold;
        }
        .sidebar a.active {
            background-color: #e8f7f8;
            color: #40c4d4;
            font-weight: bold;
            border-left: 4px solid #40c4d4;
        }
        .content { margin-left: 240px; padding: 30px; }
        .table-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
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
    <a href="${pageContext.request.contextPath}/meus-emprestimos">📖 Meus Empréstimos</a>
    <a href="${pageContext.request.contextPath}/minhas-reservas" class="active">🔖 Minhas Reservas</a>
    <a href="${pageContext.request.contextPath}/multas">💰 Multas</a>
    <a href="${pageContext.request.contextPath}/logout"
       style="color: #dc3545;">🚪 Sair</a>
</div>

<div class="content">
    <div class="table-container">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-primary">🔖 Minhas Reservas</h2>
            <a href="${pageContext.request.contextPath}/dashboard"
               class="btn btn-outline-secondary">Voltar ao Catálogo</a>
        </div>

        <%-- Feedback cancelamento --%>
        <c:if test="${param.cancelamento == 'sucesso'}">
            <div class="alert alert-success shadow-sm mb-4">
                ✅ Reserva cancelada com sucesso!
            </div>
        </c:if>

        <c:if test="${param.cancelamento == 'erro'}">
            <div class="alert alert-danger shadow-sm mb-4">
                ❌ Erro ao cancelar reserva. Tente novamente.
            </div>
        </c:if>

        <table class="table table-hover">
            <thead class="table-light">
            <tr>
                <th>Livro</th>
                <th>Data da Reserva</th>
                <th>Posição na Fila</th>
                <th>Status</th>
                <th>Ação</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="reserva" items="${reservas}">
                <tr>
                    <%-- ✅ Corrigido — mostra título do livro --%>
                    <td><strong>${reserva.livro.titulo}</strong></td>

                    <td>${reserva.dataReserva}</td>
                    <td>
                        <span class="badge bg-info text-white">
                            ${reserva.posicaoFila}º na fila
                        </span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${reserva.status == 'AGUARDANDO'}">
                                <span class="badge bg-warning text-dark">AGUARDANDO</span>
                            </c:when>
                            <c:when test="${reserva.status == 'ATENDIDA'}">
                                <span class="badge bg-success">ATENDIDA</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">CANCELADA</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${reserva.status == 'AGUARDANDO'}">
                            <form action="${pageContext.request.contextPath}/minhas-reservas"
                                  method="post">
                                <input type="hidden" name="id" value="${reserva.id}">
                                <input type="hidden" name="acao" value="cancelar">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    Cancelar
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty reservas}">
                <tr>
                    <td colspan="5" class="text-center py-5 text-muted">
                        Você não possui reservas ativas no momento.
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
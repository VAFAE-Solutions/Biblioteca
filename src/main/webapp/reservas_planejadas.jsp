<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Reservas Planejadas - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 20px; }

        @media (max-width: 1024px) {
            .sidebar-admin { width: 200px; }
            .main-content { margin-left: 210px; }
        }
        @media (max-width: 768px) {
            .sidebar-admin {
                width: 100%;
                min-height: auto;
                position: relative;
                display: flex;
                flex-wrap: wrap;
                padding: 10px;
            }
            .sidebar-admin .p-4 { width: 100%; padding: 10px !important; }
            .sidebar-admin a { padding: 8px 12px; font-size: 13px; }
            .main-content { margin-left: 0; padding: 15px; }
        }
        @media (max-width: 480px) {
            .sidebar-admin a { width: 100%; font-size: 12px; }
            table { min-width: 500px; }
            .table-responsive { overflow-x: auto; }
        }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center">
        <h5>📚
            <c:choose>
                <c:when test="${usuarioLogado.tipo == 'ADMIN'}">Admin Painel</c:when>
                <c:otherwise>Bibliotecário Painel</c:otherwise>
            </c:choose>
        </h5>
        <hr>
    </div>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/perfil">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/admin/emprestimos">📅 Empréstimos Ativos</a>
    <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
        <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    </c:if>
    <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios-unidade">👥 Usuários da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/estoque">📦 Estoque da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/reservas" class="active">🔖 Reservas Planejadas</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/admin/notificacoes">
        🔔 Notificações
        <c:if test="${totalNaoLidas > 0}">
            <span class="badge bg-danger ms-1">${totalNaoLidas}</span>
        </c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">

    <h2 class="mb-4">🔖 Reservas Planejadas</h2>

    <c:if test="${param.acao == 'cancelado'}">
        <div class="alert alert-success">✅ Reserva cancelada com sucesso!</div>
    </c:if>
    <c:if test="${param.acao == 'atendido'}">
        <div class="alert alert-success">✅ Reserva atendida com sucesso!</div>
    </c:if>
    <c:if test="${param.acao == 'erro'}">
        <div class="alert alert-danger">❌ Erro ao processar reserva. Tente novamente.</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <strong>Fila de Reservas Aguardando (${reservas.size()})</strong>
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th>Posição</th>
                    <th>Livro</th>
                    <th>Usuário ID</th>
                    <th>Data da Reserva</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${reservas}">
                    <tr>
                        <td>
                            <span class="badge bg-info text-white">
                                ${r.posicaoFila}º
                            </span>
                        </td>
                        <td><strong>${r.livro.titulo}</strong></td>
                        <td>${r.usuarioId}</td>
                        <td>${r.dataReserva}</td>
                        <td>
                            <span class="badge bg-warning text-dark">AGUARDANDO</span>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/reservas"
                                  method="post" class="d-inline">
                                <input type="hidden" name="id" value="${r.id}">
                                <input type="hidden" name="acao" value="atender">
                                <button type="submit" class="btn btn-sm btn-success me-1">
                                    ✅ Atender
                                </button>
                            </form>
                            <form action="${pageContext.request.contextPath}/admin/reservas"
                                  method="post" class="d-inline">
                                <input type="hidden" name="id" value="${r.id}">
                                <input type="hidden" name="acao" value="cancelar">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    ❌ Cancelar
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty reservas}">
                    <tr>
                        <td colspan="6" class="text-center text-muted py-4">
                            Nenhuma reserva aguardando no momento. 🎉
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
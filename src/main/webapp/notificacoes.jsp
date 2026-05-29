<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Notificações - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 20px; }
        .mensagem-nao-lida { background: #f0f9ff; border-left: 4px solid #40c4d4; }
        .mensagem-lida { background: #f8f9fa; border-left: 4px solid #dee2e6; opacity: 0.8; }
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
        <a href="${pageContext.request.contextPath}/admin/unidades">🏛️ Gerenciar Unidades</a>
        <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    </c:if>
    <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios-unidade">👥 Usuários da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/estoque">📦 Estoque da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/reservas">🔖 Reservas Planejadas</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/admin/notificacoes" class="active">
        🔔 Notificações
        <c:if test="${totalNaoLidas > 0}">
            <span class="badge bg-danger ms-1">${totalNaoLidas}</span>
        </c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>🔔 Notificações</h2>
        <c:if test="${totalNaoLidas > 0}">
            <span class="badge bg-danger fs-6">${totalNaoLidas} não lida(s)</span>
        </c:if>
    </div>

    <c:if test="${param.acao == 'lida'}">
        <div class="alert alert-success">✅ Mensagem marcada como lida.</div>
    </c:if>
    <c:if test="${param.acao == 'erro'}">
        <div class="alert alert-danger">❌ Erro ao processar. Tente novamente.</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <c:forEach var="m" items="${mensagens}">
                <div class="p-3 border-bottom ${m.lida ? 'mensagem-lida' : 'mensagem-nao-lida'}">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <div class="d-flex align-items-center gap-2 mb-1">
                                <strong>${m.nome}</strong>
                                <span class="text-muted small">&lt;${m.email}&gt;</span>
                                <c:if test="${not m.lida}">
                                    <span class="badge bg-info">Nova</span>
                                </c:if>
                                <c:if test="${m.usuarioId != null}">
                                    <span class="badge bg-success">Usuário cadastrado</span>
                                </c:if>
                            </div>
                            <div class="fw-bold mb-1">📌 ${m.assunto}</div>
                            <p class="mb-1 text-muted">${m.mensagem}</p>
                            <small class="text-muted">
                                📅 ${m.dataEnvio}
                            </small>
                        </div>
                        <c:if test="${not m.lida}">
                            <form action="${pageContext.request.contextPath}/admin/notificacoes"
                                  method="post" class="ms-3">
                                <input type="hidden" name="id" value="${m.id}">
                                <input type="hidden" name="acao" value="marcar_lida">
                                <button type="submit" class="btn btn-sm btn-outline-secondary">
                                    ✓ Marcar como lida
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty mensagens}">
                <div class="text-center text-muted py-5">
                    🎉 Nenhuma mensagem recebida ainda.
                </div>
            </c:if>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
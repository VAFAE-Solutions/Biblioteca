<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Empréstimo Presencial - Library</title>
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
        <a href="${pageContext.request.contextPath}/admin/unidades">🏛️ Gerenciar Unidades</a>
        <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    </c:if>
    <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios-unidade">👥 Usuários da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/estoque">📦 Estoque da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/reservas">🔖 Reservas Planejadas</a>
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
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>📋 Empréstimo Presencial</h2>
        <a href="${pageContext.request.contextPath}/admin/emprestimos"
           class="btn btn-outline-secondary">← Voltar</a>
    </div>

    <c:if test="${param.sucesso == 'true'}">
        <div class="alert alert-success">✅ Empréstimo registrado com sucesso!</div>
    </c:if>
    <c:if test="${not empty param.erro}">
        <div class="alert alert-danger">❌ ${param.erro}</div>
    </c:if>

    <%-- Passo 1 — Buscar usuário --%>
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white">
            <strong>🔍 Passo 1 — Buscar Usuário</strong>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/admin/emprestimo-presencial"
                  method="get" class="row g-3">
                <div class="col-md-8">
                    <label class="form-label fw-bold">E-mail do Usuário</label>
                    <input type="email" name="emailUsuario" class="form-control"
                           placeholder="usuario@email.com"
                           value="${param.emailUsuario}" required>
                </div>
                <div class="col-md-4 d-flex align-items-end">
                    <button type="submit" class="btn btn-info text-white w-100">
                        🔍 Buscar
                    </button>
                </div>
            </form>

            <c:if test="${not empty erroUsuario}">
                <div class="alert alert-warning mt-3">⚠️ ${erroUsuario}</div>
            </c:if>
        </div>
    </div>

    <%-- Passo 2 — Registrar empréstimo (só aparece se achou o usuário) --%>
    <c:if test="${not empty usuarioBuscado}">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white">
                <strong>👤 Usuário Encontrado</strong>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-3">
                        <p><strong>Nome:</strong> ${usuarioBuscado.nome}</p>
                    </div>
                    <div class="col-md-3">
                        <p><strong>Tipo:</strong>
                            <span class="badge bg-info text-white">
                                ${usuarioBuscado.tipo}
                            </span>
                        </p>
                    </div>
                    <div class="col-md-3">
                        <p><strong>Limite de Cotas:</strong>
                            ${usuarioBuscado.limiteCotas}
                        </p>
                    </div>
                    <div class="col-md-3">
                        <p><strong>Status:</strong>
                            <c:choose>
                                <c:when test="${usuarioBuscado.bloqueado}">
                                    <span class="badge bg-danger">BLOQUEADO</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-success">ATIVO</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-header bg-white">
                <strong>📚 Passo 2 — Selecionar Livro</strong>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/emprestimo-presencial"
                      method="post" class="row g-3">
                    <input type="hidden" name="acao" value="registrar">
                    <input type="hidden" name="usuarioId" value="${usuarioBuscado.id}">

                    <div class="col-md-8">
                        <label class="form-label fw-bold">Livro *</label>
                        <select name="livroId" class="form-select" required>
                            <option value="">Selecione o livro...</option>
                            <c:forEach var="l" items="${livros}">
                                <option value="${l.id}">
                                    ${l.titulo} — ${l.autor}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4 d-flex align-items-end">
                        <button type="submit" class="btn btn-success w-100">
                            ✅ Registrar Empréstimo
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
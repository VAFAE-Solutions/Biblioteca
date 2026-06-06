<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Painel Administrativo - Library Digital</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.text-danger:hover { background: #343a40; }
        .main-content { margin-left: 260px; padding: 20px; }
        .stats-card { border-left: 5px solid #40c4d4; }

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
    <a href="${pageContext.request.contextPath}/logout"
       class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            Bem-vindo,
            <c:choose>
                <c:when test="${usuarioLogado.tipo == 'ADMIN'}">
                    Admin: ${usuarioLogado.nome}
                </c:when>
                <c:otherwise>
                    Bibliotecário: ${usuarioLogado.nome}
                </c:otherwise>
            </c:choose>
        </h2>
        <%-- ✅ Passo 5 — URL corrigida --%>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-livro"
           class="btn btn-primary">+ Cadastrar Novo Livro</a>
    </div>

    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card p-3 shadow-sm stats-card">
                <h6>Total de Livros</h6>
                <h3>${livros.size()}</h3>
            </div>
        </div>

        <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
            <div class="col-md-4">
                <div class="card p-3 shadow-sm"
                     style="border-left: 5px solid #28a745;">
                    <h6>Total de Usuários</h6>
                    <h3>${totalUsuarios != null ? totalUsuarios : '-'}</h3>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card p-3 shadow-sm"
                     style="border-left: 5px solid #dc3545;">
                    <h6>Empréstimos Atrasados</h6>
                    <h3>${totalAtrasados != null ? totalAtrasados : '-'}</h3>
                </div>
            </div>
        </c:if>

        <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
            <div class="col-md-4">
                <div class="card p-3 shadow-sm"
                     style="border-left: 5px solid #ffc107;">
                    <h6>Empréstimos Ativos</h6>
                    <h3>${totalAtivos != null ? totalAtivos : '-'}</h3>
                </div>
            </div>
        </c:if>
    </div>

    <%-- Feedbacks --%>
    <c:if test="${param.cadastro == 'sucesso'}">
        <div class="alert alert-success">✅ Livro cadastrado com sucesso!</div>
    </c:if>
    <c:if test="${param.edicao == 'sucesso'}">
        <div class="alert alert-success">✅ Livro editado com sucesso!</div>
    </c:if>
    <c:if test="${param.exclusao == 'sucesso'}">
        <div class="alert alert-success">✅ Livro desativado com sucesso!</div>
    </c:if>
    <c:if test="${param.exclusao == 'erro'}">
        <div class="alert alert-danger">❌ Erro ao desativar livro.</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <strong>Gerenciamento de Acervo</strong>
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Capa</th>
                    <th>Título / Autor</th>
                    <th>Editora</th>
                    <th>Gênero</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="l" items="${livros}">
                    <tr class="${!l.ativo ? 'table-secondary' : ''}">
                        <td>${l.id}</td>
                        <td>
                            <img src="${not empty l.capaUrl ? l.capaUrl : ''}"
                                 width="40"
                                 onerror="this.src='https://via.placeholder.com/40x60?text=?'">
                        </td>
                        <td>
                            <strong>${l.titulo}</strong>
                            <br><small>${l.autor}</small>
                        </td>
                        <td>${l.editora}</td>
                        <td>${l.genero}</td>
                        <td>
                            <c:choose>
                                <c:when test="${l.ativo}">
                                    <span class="badge bg-success">ATIVO</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">INATIVO</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/detalhes?id=${l.id}"
                               class="btn btn-sm btn-info text-white">Ver</a>
                            <a href="${pageContext.request.contextPath}/admin/editar-livro?id=${l.id}"
                               class="btn btn-sm btn-warning">Editar</a>
                            <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
                                <c:choose>
                                    <c:when test="${l.ativo}">
                                        <form action="${pageContext.request.contextPath}/admin/excluir-livro"
                                              method="post" class="d-inline"
                                              onsubmit="return confirm('Desativar este livro?')">
                                            <input type="hidden" name="id" value="${l.id}">
                                            <button type="submit" class="btn btn-sm btn-danger">
                                                Desativar
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted small">Inativo</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
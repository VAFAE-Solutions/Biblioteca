<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Usuários da Unidade - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 20px; }
        @media (max-width: 1024px) { .sidebar-admin { width: 200px; } .main-content { margin-left: 210px; } }
        @media (max-width: 768px) {
            .sidebar-admin { width: 100%; min-height: auto; position: relative; display: flex; flex-wrap: wrap; padding: 10px; }
            .sidebar-admin .p-4 { width: 100%; padding: 10px !important; }
            .sidebar-admin a { padding: 8px 12px; font-size: 13px; }
            .main-content { margin-left: 0; padding: 15px; }
        }
        @media (max-width: 480px) { .sidebar-admin a { width: 100%; font-size: 12px; } table { min-width: 500px; } }
        .campo-visualizacao span.label { color: #6c757d; font-size: 0.85rem; display: block; }
        .campo-visualizacao span.valor { font-weight: 500; }
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
        <a href="${pageContext.request.contextPath}/admin/usuarios-unidade" class="active">👥 Usuários da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/estoque">📦 Estoque da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/reservas">🔖 Reservas Planejadas</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/admin/notificacoes">
        🔔 Notificações
        <c:if test="${totalNaoLidas > 0}"><span class="badge bg-danger ms-1">${totalNaoLidas}</span></c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>👥 Usuários da Unidade</h2>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-usuario" class="btn btn-primary">➕ Cadastrar Usuário</a>
    </div>

    <%-- Feedbacks --%>
    <c:if test="${param.acao == 'bloqueado'}"><div class="alert alert-warning">⚠️ Usuário bloqueado com sucesso.</div></c:if>
    <c:if test="${param.acao == 'desbloqueado'}"><div class="alert alert-success">✅ Usuário desbloqueado com sucesso.</div></c:if>
    <c:if test="${param.acao == 'desativado'}"><div class="alert alert-secondary">🚫 Usuário desativado com sucesso.</div></c:if>
    <c:if test="${param.acao == 'reativado'}"><div class="alert alert-success">✅ Usuário reativado com sucesso.</div></c:if>
    <c:if test="${param.acao == 'editado'}"><div class="alert alert-success">✅ Usuário atualizado com sucesso.</div></c:if>
    <c:if test="${param.acao == 'erro'}"><div class="alert alert-danger">❌ Erro ao processar ação. Tente novamente.</div></c:if>

    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-light">
                <tr><th>ID</th><th>Nome</th><th>E-mail</th><th>Tipo</th><th>Status</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${usuarios}">
                    <tr class="${u.bloqueado ? 'table-danger' : (!u.ativo ? 'table-secondary' : '')}">
                        <td>${u.id}</td>
                        <td>${u.nome}</td>
                        <td>${u.email}</td>
                        <td><span class="badge bg-info text-white">${u.tipo}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${!u.ativo}"><span class="badge bg-secondary">INATIVO</span></c:when>
                                <c:when test="${u.bloqueado}"><span class="badge bg-danger">BLOQUEADO</span></c:when>
                                <c:otherwise><span class="badge bg-success">ATIVO</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <%-- Botão Ver — abre modal estilo SharePoint --%>
                            <button class="btn btn-sm btn-info text-white"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalUsuario${u.id}">
                                👁 Ver
                            </button>
                            <c:choose>
                                <c:when test="${!u.ativo}">
                                    <form action="${pageContext.request.contextPath}/admin/usuarios-unidade" method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="reativar">
                                        <button type="submit" class="btn btn-sm btn-success">Reativar</button>
                                    </form>
                                </c:when>
                                <c:when test="${u.bloqueado}">
                                    <form action="${pageContext.request.contextPath}/admin/usuarios-unidade" method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="desbloquear">
                                        <button type="submit" class="btn btn-sm btn-success">Desbloquear</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/usuarios-unidade" method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="bloquear">
                                        <button type="submit" class="btn btn-sm btn-warning">Bloquear</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/usuarios-unidade" method="post" class="d-inline"
                                          onsubmit="return confirm('Desativar este usuário?')">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="desativar">
                                        <button type="submit" class="btn btn-sm btn-danger">Desativar</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <%-- Modal estilo SharePoint por usuário --%>
                    <div class="modal fade" id="modalUsuario${u.id}" tabindex="-1">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        👤 ${u.nome}
                                        <span class="badge bg-info text-white ms-2">${u.tipo}</span>
                                    </h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">

                                    <%-- MODO VISUALIZAÇÃO --%>
                                    <div id="modoVer${u.id}">
                                        <div class="row campo-visualizacao">
                                            <div class="col-md-6 mb-3">
                                                <span class="label">Nome</span>
                                                <span class="valor">${u.nome}</span>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <span class="label">E-mail</span>
                                                <span class="valor">${u.email}</span>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <span class="label">Telefone</span>
                                                <span class="valor">${not empty u.telefone ? u.telefone : '—'}</span>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <span class="label">CPF</span>
                                                <span class="valor">${not empty u.cpf ? u.cpf : '—'}</span>
                                            </div>
                                            <c:if test="${u.tipo == 'ESTUDANTE'}">
                                                <div class="col-md-6 mb-3">
                                                    <span class="label">RA</span>
                                                    <span class="valor">${u.ra}</span>
                                                </div>
                                            </c:if>
                                            <div class="col-md-6 mb-3">
                                                <span class="label">Status</span>
                                                <span class="valor">
                                                    <c:choose>
                                                        <c:when test="${!u.ativo}"><span class="badge bg-secondary">INATIVO</span></c:when>
                                                        <c:when test="${u.bloqueado}"><span class="badge bg-danger">BLOQUEADO</span></c:when>
                                                        <c:otherwise><span class="badge bg-success">ATIVO</span></c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <%-- MODO EDIÇÃO --%>
                                    <div id="modoEditar${u.id}" style="display:none;">
                                        <form action="${pageContext.request.contextPath}/admin/usuarios-unidade" method="post">
                                            <input type="hidden" name="acao" value="editar">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <div class="row">
                                                <div class="col-md-6 mb-3">
                                                    <label class="form-label fw-bold">Nome *</label>
                                                    <input type="text" name="nome" class="form-control"
                                                           value="${u.nome}" required minlength="3">
                                                </div>
                                                <div class="col-md-6 mb-3">
                                                    <label class="form-label fw-bold">E-mail</label>
                                                    <input type="email" class="form-control" value="${u.email}" disabled>
                                                    <small class="text-muted">E-mail não pode ser alterado.</small>
                                                </div>
                                                <div class="col-md-6 mb-3">
                                                    <label class="form-label fw-bold">Telefone</label>
                                                    <input type="tel" name="telefone" class="form-control"
                                                           value="${u.telefone}" placeholder="(11) 99999-9999">
                                                </div>
                                                <div class="col-md-6 mb-3">
                                                    <label class="form-label fw-bold">CPF</label>
                                                    <input type="text" name="cpf" class="form-control"
                                                           value="${u.cpf}" placeholder="000.000.000-00">
                                                </div>
                                                <c:if test="${u.tipo == 'ESTUDANTE'}">
                                                    <div class="col-md-6 mb-3">
                                                        <label class="form-label fw-bold">RA</label>
                                                        <input type="number" name="ra" class="form-control" value="${u.ra}">
                                                        <small class="text-muted">⚠️ Altere apenas em caso de erro.</small>
                                                    </div>
                                                </c:if>
                                            </div>
                                            <div class="d-flex gap-2 mt-2">
                                                <button type="submit" class="btn btn-primary">💾 Salvar</button>
                                                <button type="button" class="btn btn-outline-secondary"
                                                        onclick="cancelarEdicao('${u.id}')">✕ Cancelar</button>
                                            </div>
                                        </form>
                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-warning" id="btnEditar${u.id}"
                                            onclick="ativarEdicao('${u.id}')">✏️ Editar</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty usuarios}">
                    <tr><td colspan="6" class="text-center text-muted py-4">Nenhum usuário encontrado.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function ativarEdicao(id) {
        document.getElementById('modoVer' + id).style.display = 'none';
        document.getElementById('modoEditar' + id).style.display = 'block';
        document.getElementById('btnEditar' + id).style.display = 'none';
    }
    function cancelarEdicao(id) {
        document.getElementById('modoVer' + id).style.display = 'block';
        document.getElementById('modoEditar' + id).style.display = 'none';
        document.getElementById('btnEditar' + id).style.display = 'inline-block';
    }
</script>
</body>
</html>

<%-- ✅ Reabre modal após edição --%>
<c:if test="${not empty param.usuarioId}">
<script>
    window.addEventListener('load', function() {
        var modal = new bootstrap.Modal(
            document.getElementById('modalUsuario${param.usuarioId}'));
        modal.show();
    });
</script>
</c:if>

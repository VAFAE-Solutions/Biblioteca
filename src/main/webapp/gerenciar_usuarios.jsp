<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Usuários - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 20px; }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center">
        <h5>📚 Admin Painel</h5>
        <hr>
    </div>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/emprestimos">📅 Empréstimos Ativos</a>
    <a href="${pageContext.request.contextPath}/admin/usuarios" class="active">👥 Gerenciar Usuários</a>
    <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
    <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>👥 Gerenciar Usuários</h2>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-usuario"
           class="btn btn-primary">➕ Cadastrar Usuário</a>
    </div>

    <%-- Feedbacks --%>
    <c:if test="${param.acao == 'bloquear'}">
        <div class="alert alert-warning">⚠️ Usuário bloqueado com sucesso.</div>
    </c:if>
    <c:if test="${param.acao == 'desbloquear'}">
        <div class="alert alert-success">✅ Usuário desbloqueado com sucesso.</div>
    </c:if>
    <c:if test="${param.acao == 'desativar'}">
        <div class="alert alert-secondary">🚫 Usuário desativado com sucesso.</div>
    </c:if>
    <c:if test="${param.acao == 'reativar'}">
        <div class="alert alert-success">✅ Usuário reativado com sucesso.</div>
    </c:if>
    <c:if test="${param.acao == 'ajustar_limite'}">
        <div class="alert alert-success">✅ Limite de cotas ajustado com sucesso.</div>
    </c:if>
    <c:if test="${param.acao == 'resetar_limite'}">
        <div class="alert alert-info">🔄 Limite de cotas resetado para o padrão.</div>
    </c:if>
    <c:if test="${param.acao == 'reset_aprovado'}">
        <div class="alert alert-success">
            ✅ Reset aprovado! Senha temporária do usuário ID ${param.id}:
            <strong class="fs-5 ms-2">${param.senha}</strong>
            <small class="d-block mt-1 text-muted">Informe esta senha ao usuário. Ela será substituída na próxima troca de senha.</small>
        </div>
    </c:if>
    <c:if test="${param.acao == 'erro'}">
        <div class="alert alert-danger">❌ ${not empty param.msg ? param.msg : 'Erro ao processar ação.'}</div>
    </c:if>

    <%-- ✅ Painel de alertas — bloqueados e com reset --%>
    <c:if test="${not empty usuariosBloqueadosOuReset}">
        <div class="card shadow-sm mb-4 border-warning">
            <div class="card-header bg-warning text-dark">
                <strong>🚨 Atenção — Usuários que precisam de ação (${usuariosBloqueadosOuReset.size()})</strong>
            </div>
            <div class="card-body">
                <table class="table table-sm table-hover mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>Tipo de Ocorrência</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="u" items="${usuariosBloqueadosOuReset}">
                        <tr>
                            <td>${u.nome}</td>
                            <td>${u.email}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.bloqueado && u.solicitaReset}">
                                        <span class="badge bg-danger">BLOQUEADO</span>
                                        <span class="badge bg-primary ms-1">RESET SOLICITADO</span>
                                    </c:when>
                                    <c:when test="${u.bloqueado}">
                                        <span class="badge bg-danger">BLOQUEADO POR TENTATIVAS</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-primary">RESET SOLICITADO</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${u.bloqueado}">
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="desbloquear">
                                        <button type="submit" class="btn btn-sm btn-success">
                                            Desbloquear
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${u.solicitaReset}">
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="aprovar_reset">
                                        <button type="submit" class="btn btn-sm btn-primary"
                                                onclick="return confirm('Gerar senha temporária para ${u.nome}?')">
                                            🔑 Aprovar Reset
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <%-- Tabela principal de usuários --%>
    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Tipo</th>
                    <th>Limite Cotas</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
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
                                <c:when test="${u.tipo == 'ESTUDANTE' || u.tipo == 'COMUM'}">
                                    ${u.limiteCotas}
                                    <c:if test="${u.limiteCotasCustom != null}">
                                        <span class="badge bg-warning text-dark ms-1">custom</span>
                                    </c:if>
                                </c:when>
                                <c:otherwise><span class="text-muted">—</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${!u.ativo}">
                                    <span class="badge bg-secondary">INATIVO</span>
                                </c:when>
                                <c:when test="${u.bloqueado && u.solicitaReset}">
                                    <span class="badge bg-danger">BLOQUEADO</span>
                                    <span class="badge bg-primary ms-1">RESET</span>
                                </c:when>
                                <c:when test="${u.bloqueado}">
                                    <span class="badge bg-danger">BLOQUEADO</span>
                                </c:when>
                                <c:when test="${u.solicitaReset}">
                                    <span class="badge bg-success">ATIVO</span>
                                    <span class="badge bg-primary ms-1">RESET</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-success">ATIVO</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/usuarios?detalhe=${u.id}"
                               class="btn btn-sm btn-info text-white">Ver</a>
                            <c:choose>
                                <c:when test="${!u.ativo}">
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="reativar">
                                        <button type="submit" class="btn btn-sm btn-success">Reativar</button>
                                    </form>
                                </c:when>
                                <c:when test="${u.bloqueado}">
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="desbloquear">
                                        <button type="submit" class="btn btn-sm btn-success">Desbloquear</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Desativar este usuário?')">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="desativar">
                                        <button type="submit" class="btn btn-sm btn-danger">Desativar</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="bloquear">
                                        <button type="submit" class="btn btn-sm btn-warning">Bloquear</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/usuarios"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Desativar este usuário?')">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <input type="hidden" name="acao" value="desativar">
                                        <button type="submit" class="btn btn-sm btn-danger">Desativar</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty usuarios}">
                    <tr>
                        <td colspan="7" class="text-center text-muted py-4">Nenhum usuário encontrado.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%-- Modal detalhes do usuário --%>
<c:if test="${not empty usuarioDetalhe}">
    <div class="modal fade show d-block" tabindex="-1" style="background: rgba(0,0,0,0.5);">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        👤 ${usuarioDetalhe.nome}
                        <span class="badge bg-info text-white ms-2">${usuarioDetalhe.tipo}</span>
                    </h5>
                    <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn-close"></a>
                </div>
                <div class="modal-body">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <p><strong>E-mail:</strong> ${usuarioDetalhe.email}</p>
                            <p><strong>Telefone:</strong> ${not empty usuarioDetalhe.telefone ? usuarioDetalhe.telefone : 'Não informado'}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Status:</strong>
                                <c:choose>
                                    <c:when test="${!usuarioDetalhe.ativo}"><span class="badge bg-secondary">INATIVO</span></c:when>
                                    <c:when test="${usuarioDetalhe.bloqueado}"><span class="badge bg-danger">BLOQUEADO</span></c:when>
                                    <c:otherwise><span class="badge bg-success">ATIVO</span></c:otherwise>
                                </c:choose>
                            </p>
                            <c:if test="${usuarioDetalhe.tipo == 'ESTUDANTE' || usuarioDetalhe.tipo == 'COMUM'}">
                                <p><strong>Limite de Cotas:</strong> ${usuarioDetalhe.limiteCotas}
                                    <c:if test="${usuarioDetalhe.limiteCotasCustom != null}">
                                        <span class="badge bg-warning text-dark">customizado</span>
                                    </c:if>
                                </p>
                            </c:if>
                        </div>
                    </div>
                    <hr>
                    <h6 class="fw-bold">💰 Multas</h6>
                    <c:choose>
                        <c:when test="${totalMulta > 0}">
                            <div class="alert alert-danger py-2">
                                Total pendente: <strong>R$ <fmt:formatNumber value="${totalMulta}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-success py-2">✅ Sem multas pendentes.</div>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty multasUsuario}">
                        <table class="table table-sm table-hover">
                            <thead class="table-light">
                            <tr><th>Data</th><th>Valor</th><th>Status</th></tr>
                            </thead>
                            <tbody>
                            <c:forEach var="m" items="${multasUsuario}">
                                <tr>
                                    <td>${m.dataGeracao}</td>
                                    <td>R$ <fmt:formatNumber value="${m.valor}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.pago}"><span class="badge bg-success">PAGO</span></c:when>
                                            <c:otherwise><span class="badge bg-danger">PENDENTE</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${usuarioDetalhe.tipo == 'ESTUDANTE' || usuarioDetalhe.tipo == 'COMUM'}">
                        <hr>
                        <h6 class="fw-bold">📚 Ajustar Limite de Cotas</h6>
                        <form action="${pageContext.request.contextPath}/admin/usuarios"
                              method="post" class="d-flex gap-2 align-items-end">
                            <input type="hidden" name="id" value="${usuarioDetalhe.id}">
                            <input type="hidden" name="acao" value="ajustar_limite">
                            <div>
                                <label class="form-label small">Novo limite (atual: ${usuarioDetalhe.limiteCotas})</label>
                                <input type="number" name="limite" class="form-control"
                                       min="1" max="20" value="${usuarioDetalhe.limiteCotas}"
                                       style="width: 120px;">
                            </div>
                            <button type="submit" class="btn btn-primary">Salvar</button>
                        </form>
                        <c:if test="${usuarioDetalhe.limiteCotasCustom != null}">
                            <form action="${pageContext.request.contextPath}/admin/usuarios"
                                  method="post" class="mt-2">
                                <input type="hidden" name="id" value="${usuarioDetalhe.id}">
                                <input type="hidden" name="acao" value="resetar_limite">
                                <button type="submit" class="btn btn-sm btn-outline-secondary">
                                    🔄 Resetar para padrão do tipo
                                </button>
                            </form>
                        </c:if>
                    </c:if>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/admin/usuarios"
                       class="btn btn-secondary">Fechar</a>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
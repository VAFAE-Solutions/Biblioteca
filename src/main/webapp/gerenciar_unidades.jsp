<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Unidades - Library</title>
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
    <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
    <a href="${pageContext.request.contextPath}/admin/unidades" class="active">🏛️ Unidades</a>
    <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
    <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <h2 class="mb-4">🏛️ Gerenciar Unidades</h2>

    <%-- Feedbacks --%>
    <c:if test="${param.acao == 'cadastrado'}">
        <div class="alert alert-success">✅ Unidade cadastrada com sucesso!</div>
    </c:if>
    <c:if test="${param.acao == 'atualizado'}">
        <div class="alert alert-success">✅ Unidade atualizada com sucesso!</div>
    </c:if>
    <c:if test="${param.acao == 'desativado'}">
        <div class="alert alert-warning">⚠️ Unidade desativada com sucesso!</div>
    </c:if>
    <c:if test="${not empty param.erro}">
        <div class="alert alert-danger">❌ ${param.erro}</div>
    </c:if>

    <%-- Formulário cadastro --%>
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white">
            <strong>+ Adicionar Unidade</strong>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/admin/unidades" method="post">
                <input type="hidden" name="acao" value="cadastrar">
                <div class="row g-3">
                    <div class="col-md-3">
                        <label class="form-label fw-bold">Nome *</label>
                        <input type="text" name="nome" class="form-control"
                               placeholder="Ex: Unidade Central" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label fw-bold">Endereço *</label>
                        <input type="text" name="endereco" class="form-control"
                               placeholder="Rua, número" required>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label fw-bold">Telefone</label>
                        <input type="tel" name="telefone" class="form-control"
                               placeholder="(11) 99999-9999">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label fw-bold">Horário</label>
                        <input type="text" name="horario" class="form-control"
                               placeholder="Seg-Sex 8h-20h">
                    </div>
                    <div class="col-md-1 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">Salvar</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <%-- Lista de unidades --%>
    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <strong>Unidades Ativas (${unidades.size()})</strong>
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Endereço</th>
                    <th>Telefone</th>
                    <th>Horário</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${unidades}">
                    <tr>
                        <td>${u.id}</td>
                        <td><strong>${u.nome}</strong></td>
                        <td>${u.endereco}</td>
                        <td>${not empty u.telefone ? u.telefone : '-'}</td>
                        <td>${not empty u.horarioFuncionamento ? u.horarioFuncionamento : '-'}</td>
                        <td>
                            <button class="btn btn-sm btn-info text-white"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalEditar${u.id}">
                                ✏️ Editar
                            </button>
                            <form action="${pageContext.request.contextPath}/admin/unidades"
                                  method="post" class="d-inline"
                                  onsubmit="return confirm('Desativar esta unidade?')">
                                <input type="hidden" name="acao" value="desativar">
                                <input type="hidden" name="id" value="${u.id}">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    🚫 Desativar
                                </button>
                            </form>
                        </td>
                    </tr>

                    <%-- Modal Editar --%>
                    <div class="modal fade" id="modalEditar${u.id}" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">✏️ Editar Unidade</h5>
                                    <button type="button" class="btn-close"
                                            data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <form action="${pageContext.request.contextPath}/admin/unidades"
                                          method="post">
                                        <input type="hidden" name="acao" value="atualizar">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Nome *</label>
                                            <input type="text" name="nome" class="form-control"
                                                   value="${u.nome}" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Endereço *</label>
                                            <input type="text" name="endereco" class="form-control"
                                                   value="${u.endereco}" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Telefone</label>
                                            <input type="tel" name="telefone" class="form-control"
                                                   value="${u.telefone}">
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Horário</label>
                                            <input type="text" name="horario" class="form-control"
                                                   value="${u.horarioFuncionamento}">
                                        </div>
                                        <div class="d-flex gap-2">
                                            <button type="submit" class="btn btn-primary">
                                                💾 Salvar
                                            </button>
                                            <button type="button" class="btn btn-outline-secondary"
                                                    data-bs-dismiss="modal">Cancelar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty unidades}">
                    <tr>
                        <td colspan="6" class="text-center text-muted py-4">
                            Nenhuma unidade cadastrada.
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
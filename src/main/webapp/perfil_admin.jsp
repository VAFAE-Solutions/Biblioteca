<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Meu Perfil - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 30px; }
        .perfil-card { background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 20px; }
        .avatar-circle { width: 80px; height: 80px; border-radius: 50%; background-color: #40c4d4; display: flex; align-items: center; justify-content: center; font-size: 32px; color: white; font-weight: bold; }
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
    <a href="${pageContext.request.contextPath}/perfil" class="active">👤 Meu Perfil</a>
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

    <c:if test="${param.atualizado == 'sucesso'}">
        <div class="alert alert-success">✅ Perfil atualizado com sucesso!</div>
    </c:if>
    <c:if test="${param.senha == 'sucesso'}">
        <div class="alert alert-success">✅ Senha alterada com sucesso!</div>
    </c:if>
    <c:if test="${not empty param.erro}">
        <div class="alert alert-danger">❌ ${param.erro}</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-danger">❌ ${erro}</div>
    </c:if>

    <div class="perfil-card">
        <div class="d-flex align-items-center justify-content-between mb-4">
            <div class="d-flex align-items-center gap-4">
                <div class="avatar-circle">
                    ${usuarioLogado.nome.substring(0,1).toUpperCase()}
                </div>
                <div>
                    <h3 class="mb-1">${usuarioLogado.nome}</h3>
                    <span class="badge bg-info text-white">${usuarioLogado.tipo}</span>
                </div>
            </div>
            <button class="btn btn-outline-info" onclick="abrirEdicao()">✏️ Editar Perfil</button>
        </div>
        <hr>
        <div id="modoVisualizacao">
            <div class="row mt-3">
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">E-mail</label>
                    <p class="fw-bold">${usuarioLogado.email}</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">Telefone</label>
                    <p class="fw-bold">${not empty usuarioLogado.telefone ? usuarioLogado.telefone : 'Não informado'}</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">CPF</label>
                    <p class="fw-bold">${not empty usuarioLogado.cpf ? usuarioLogado.cpf : 'Não informado'}</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">Membro desde</label>
                    <p class="fw-bold">${usuarioLogado.createdAt}</p>
                </div>
            </div>
        </div>
        <div id="modoEdicao" style="display: none;">
            <form action="${pageContext.request.contextPath}/perfil" method="post">
                <input type="hidden" name="acao" value="editar">
                <div class="row mt-3">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Nome *</label>
                        <input type="text" name="nome" class="form-control"
                               value="${usuarioLogado.nome}" required minlength="3">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">E-mail</label>
                        <input type="email" class="form-control"
                               value="${usuarioLogado.email}" disabled>
                        <small class="text-muted">E-mail não pode ser alterado.</small>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Telefone</label>
                        <input type="tel" name="telefone" class="form-control"
                               value="${usuarioLogado.telefone}"
                               placeholder="(11) 99999-9999">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">CPF</label>
                        <input type="text" name="cpf" class="form-control"
                               value="${usuarioLogado.cpf}"
                               placeholder="000.000.000-00">
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-info text-white px-4">💾 Salvar</button>
                    <button type="button" class="btn btn-outline-secondary"
                            onclick="fecharEdicao()">Cancelar</button>
                </div>
            </form>
        </div>
    </div>

    <div class="perfil-card">
        <div class="d-flex align-items-center justify-content-between mb-3">
            <h5 class="mb-0">🔒 Trocar Senha</h5>
            <button class="btn btn-outline-secondary btn-sm" onclick="toggleSenhaCard()">
                Expandir / Recolher
            </button>
        </div>
        <div id="senhaCard" style="display: none;">
            <form action="${pageContext.request.contextPath}/perfil" method="post">
                <input type="hidden" name="acao" value="trocar_senha">
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Senha Atual *</label>
                        <div class="input-group">
                            <input type="password" name="senhaAtual" id="senhaAtual" class="form-control" required>
                            <button type="button" class="btn btn-outline-secondary" onclick="toggleSenha('senhaAtual', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Nova Senha *</label>
                        <div class="input-group">
                            <input type="password" name="novaSenha" id="novaSenha" class="form-control" minlength="6" required>
                            <button type="button" class="btn btn-outline-secondary" onclick="toggleSenha('novaSenha', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Confirmar Nova Senha *</label>
                        <div class="input-group">
                            <input type="password" name="confirmarSenha" id="confirmarSenha" class="form-control" required>
                            <button type="button" class="btn btn-outline-secondary" onclick="toggleSenha('confirmarSenha', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-warning px-4">🔒 Alterar Senha</button>
            </form>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function abrirEdicao() {
        document.getElementById('modoVisualizacao').style.display = 'none';
        document.getElementById('modoEdicao').style.display = 'block';
    }
    function fecharEdicao() {
        document.getElementById('modoVisualizacao').style.display = 'block';
        document.getElementById('modoEdicao').style.display = 'none';
    }
    function toggleSenhaCard() {
        const card = document.getElementById('senhaCard');
        card.style.display = card.style.display === 'none' ? 'block' : 'none';
    }
    function toggleSenha(inputId, btn) {
        const input = document.getElementById(inputId);
        const icon = btn.querySelector('i');
        if (input.type === 'password') {
            input.type = 'text';
            icon.className = 'bi bi-eye';
        } else {
            input.type = 'password';
            icon.className = 'bi bi-eye-slash';
        }
    }
</script>
</body>
</html>

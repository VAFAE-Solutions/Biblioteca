<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Meu Perfil - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
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
        .perfil-card { background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 20px; }
        .avatar-circle { width: 80px; height: 80px; border-radius: 50%; background-color: #40c4d4; display: flex; align-items: center; justify-content: center; font-size: 32px; color: white; font-weight: bold; }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>
    <div>
        <span class="me-3"><strong>${usuarioLogado.nome}</strong></span>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Sair</a>
    </div>
</div>

<div class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard">🏠 Catálogo</a>
    <a href="${pageContext.request.contextPath}/perfil" class="active">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/meus-emprestimos">📖 Meus Empréstimos</a>
    <a href="${pageContext.request.contextPath}/minhas-reservas">🔖 Minhas Reservas</a>
    <a href="${pageContext.request.contextPath}/multas">💰 Multas</a>
    <a href="${pageContext.request.contextPath}/home" class="link-home">🌐 Página Inicial</a>
    <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;">🚪 Sair</a>
</div>

<div class="content">

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
                <c:if test="${usuarioLogado.tipo == 'ESTUDANTE'}">
                    <div class="col-md-6 mb-3">
                        <label class="text-muted small">RA</label>
                        <p class="fw-bold">${not empty ra ? ra : 'Não informado'}</p>
                    </div>
                </c:if>
                <c:if test="${usuarioLogado.tipo == 'COMUM' || usuarioLogado.tipo == 'ESTUDANTE'}">
                    <div class="col-md-6 mb-3">
                        <label class="text-muted small">CPF</label>
                        <p class="fw-bold">${not empty usuarioLogado.cpf ? usuarioLogado.cpf : 'Não informado'}</p>
                    </div>
                </c:if>
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">Membro desde</label>
                    <p class="fw-bold">${usuarioLogado.createdAt}</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">Prazo de Empréstimo</label>
                    <p class="fw-bold">${usuarioLogado.prazoEmprestimo} dias</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">Limite de Livros</label>
                    <p class="fw-bold">${usuarioLogado.limiteCotas} livros</p>
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
                    <c:if test="${usuarioLogado.tipo == 'ESTUDANTE'}">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">RA</label>
                            <input type="text" class="form-control" value="${ra}" disabled>
                            <small class="text-muted">RA não pode ser alterado.</small>
                        </div>
                    </c:if>
                    <c:if test="${usuarioLogado.tipo == 'COMUM' || usuarioLogado.tipo == 'ESTUDANTE'}">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">CPF</label>
                            <input type="text" name="cpf" class="form-control"
                                   value="${usuarioLogado.cpf}"
                                   placeholder="000.000.000-00">
                        </div>
                    </c:if>
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
                            <input type="password" name="senhaAtual" id="senhaAtual"
                                   class="form-control" required>
                            <button type="button" class="btn btn-outline-secondary"
                                    onclick="toggleSenha('senhaAtual', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Nova Senha *</label>
                        <div class="input-group">
                            <input type="password" name="novaSenha" id="novaSenha"
                                   class="form-control" minlength="6" required>
                            <button type="button" class="btn btn-outline-secondary"
                                    onclick="toggleSenha('novaSenha', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Confirmar Nova Senha *</label>
                        <div class="input-group">
                            <input type="password" name="confirmarSenha" id="confirmarSenha"
                                   class="form-control" required>
                            <button type="button" class="btn btn-outline-secondary"
                                    onclick="toggleSenha('confirmarSenha', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-warning px-4">🔒 Alterar Senha</button>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4">
            <div class="card p-3 shadow-sm text-center" style="border-left: 5px solid #40c4d4;">
                <h6 class="text-muted">Empréstimos Ativos</h6>
                <h3 class="text-info">${totalEmprestimos}</h3>
                <a href="${pageContext.request.contextPath}/meus-emprestimos"
                   class="btn btn-sm btn-outline-info mt-2">Ver</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3 shadow-sm text-center" style="border-left: 5px solid #dc3545;">
                <h6 class="text-muted">Multas Pendentes</h6>
                <h3 class="text-danger">
                    R$ <fmt:formatNumber value="${totalMulta}"
                        minFractionDigits="2" maxFractionDigits="2"/>
                </h3>
                <a href="${pageContext.request.contextPath}/multas"
                   class="btn btn-sm btn-outline-danger mt-2">Ver</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3 shadow-sm text-center" style="border-left: 5px solid #ffc107;">
                <h6 class="text-muted">Reservas Ativas</h6>
                <h3 class="text-warning">${totalReservas}</h3>
                <a href="${pageContext.request.contextPath}/minhas-reservas"
                   class="btn btn-sm btn-outline-warning mt-2">Ver</a>
            </div>
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
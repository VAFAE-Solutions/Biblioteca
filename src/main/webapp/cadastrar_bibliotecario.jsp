<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Bibliotecário - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
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
        @media (max-width: 480px) { .sidebar-admin a { width: 100%; font-size: 12px; } }
        .email-input-group { display: flex; align-items: center; border: 1px solid #ced4da; border-radius: 6px; overflow: hidden; }
        .email-input-group input { border: none; flex: 1; padding: 8px 12px; font-size: 14px; outline: none; }
        .email-input-group span { background: #e9ecef; padding: 8px 12px; color: #6c757d; white-space: nowrap; font-size: 14px; border-left: 1px solid #ced4da; }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center"><h5>📚 Admin Painel</h5><hr></div>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/perfil">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/admin/emprestimos">📅 Empréstimos Ativos</a>
    <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
    <a href="${pageContext.request.contextPath}/admin/unidades">🏛️ Gerenciar Unidades</a>
    <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
    <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario" class="active">👨‍💼 Cadastrar Bibliotecário</a>
    <a href="${pageContext.request.contextPath}/admin/notificacoes">
        🔔 Notificações
        <c:if test="${totalNaoLidas > 0}"><span class="badge bg-danger ms-1">${totalNaoLidas}</span></c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>👨‍💼 Cadastrar Bibliotecário</h2>
        <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn btn-outline-secondary">← Voltar</a>
    </div>

    <c:if test="${param.cadastro == 'sucesso'}">
        <div class="alert alert-success">✅ Bibliotecário cadastrado com sucesso!</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-danger">❌ ${erro}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario"
                  method="post" onsubmit="montarEmail()">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Nome *</label>
                        <input type="text" name="nome" class="form-control" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">E-mail *</label>
                        <div class="email-input-group">
                            <input type="text" id="emailPrefix" placeholder="seunome" required>
                            <span>@biblioteca.com</span>
                        </div>
                        <input type="hidden" name="email" id="emailCompleto">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Senha *</label>
                        <div class="input-group">
                            <input type="password" name="senha" id="senhaBib"
                                   class="form-control" minlength="6" required>
                            <button type="button" class="btn btn-outline-secondary"
                                    onclick="toggleSenha('senhaBib', this)">
                                <i class="bi bi-eye-slash"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Telefone</label>
                        <input type="tel" name="telefone" class="form-control" placeholder="(11) 99999-9999">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">CPF</label>
                        <input type="text" name="cpf" class="form-control" placeholder="000.000.000-00">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Unidade *</label>
                        <select name="unidadeId" class="form-select" required>
                            <option value="">Selecione a unidade...</option>
                            <c:forEach var="u" items="${unidades}">
                                <option value="${u.id}">${u.nome} — ${u.endereco}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary px-4">💾 Cadastrar Bibliotecário</button>
                    <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn btn-outline-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function montarEmail() {
        const prefix = document.getElementById('emailPrefix').value.trim();
        document.getElementById('emailCompleto').value = prefix + '@biblioteca.com';
    }
    function toggleSenha(inputId, btn) {
        const input = document.getElementById(inputId);
        const icon = btn.querySelector('i');
        if (input.type === 'password') { input.type = 'text'; icon.className = 'bi bi-eye'; }
        else { input.type = 'password'; icon.className = 'bi bi-eye-slash'; }
    }
    document.querySelector('[name="telefone"]').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            if (value.length <= 2) value = value.replace(/^(\d{0,2})/, '($1');
            else if (value.length <= 7) value = value.replace(/^(\d{2})(\d{0,5})/, '($1) $2');
            else value = value.replace(/^(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
            e.target.value = value;
        }
    });
    document.querySelector('[name="cpf"]').addEventListener('input', function(e) {
        let v = e.target.value.replace(/\D/g, '');
        v = v.replace(/(\d{3})(\d)/, '$1.$2');
        v = v.replace(/(\d{3})(\d)/, '$1.$2');
        v = v.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        e.target.value = v;
    });
</script>
</body>
</html>

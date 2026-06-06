<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Usuário - Library</title>
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
        <h2>➕ Cadastrar Usuário</h2>
        <c:choose>
            <c:when test="${usuarioLogado.tipo == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/usuarios"
                   class="btn btn-outline-secondary">← Voltar</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/admin/usuarios-unidade"
                   class="btn btn-outline-secondary">← Voltar</a>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${param.cadastro == 'sucesso'}">
        <div class="alert alert-success">✅ Usuário cadastrado com sucesso!</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-danger">❌ ${erro}</div>
    </c:if>

    <div class="card shadow-sm" style="max-width: 600px;">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/admin/cadastrar-usuario"
                  method="post">

                <div class="mb-3">
                    <label class="form-label">Tipo de Usuário</label>
                    <select name="tipo" class="form-select" id="tipoSelect" required
                            onchange="mostrarCampos()">
                        <option value="">Selecione...</option>
                        <option value="COMUM">Comum</option>
                        <option value="ESTUDANTE">Estudante</option>
                        <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
                            <option value="ADMIN">Administrador</option>
                        </c:if>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Nome</label>
                    <input type="text" name="nome" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Senha</label>
                    <input type="password" name="senha" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">CPF</label>
                    <input type="text" name="cpf" class="form-control">
                </div>

                <div class="mb-3">
                    <label class="form-label">Telefone</label>
                    <input type="text" name="telefone" class="form-control">
                </div>

                <%-- Campo RA — só para ESTUDANTE --%>
                <div class="mb-3" id="campoRa" style="display:none;">
                    <label class="form-label">RA</label>
                    <input type="number" name="ra" class="form-control">
                </div>

                <button type="submit" class="btn btn-primary w-100">
                    Cadastrar
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    function mostrarCampos() {
        const tipo = document.getElementById('tipoSelect').value;
        document.getElementById('campoRa').style.display =
                tipo === 'ESTUDANTE' ? 'block' : 'none';
        document.getElementById('campoUnidade').style.display =
                tipo === 'BIBLIOTECARIO' ? 'block' : 'none';
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
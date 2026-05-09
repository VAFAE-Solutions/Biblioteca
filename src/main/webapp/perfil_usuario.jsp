<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Meu Perfil - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; }
        .topbar {
            background-color: #40c4d4;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            color: white;
        }
        .sidebar {
            width: 220px;
            height: 100vh;
            background: #fff;
            border-right: 1px solid #ccc;
            position: fixed;
            padding-top: 20px;
        }
        .sidebar a {
            display: block;
            padding: 15px;
            color: #333;
            text-decoration: none;
            border-bottom: 1px solid #ddd;
        }
        .sidebar a:hover {
            background-color: #f0f0f0;
            color: #40c4d4;
            font-weight: bold;
        }
        .sidebar a.active {
            background-color: #e8f7f8;
            color: #40c4d4;
            font-weight: bold;
            border-left: 4px solid #40c4d4;
        }
        .content { margin-left: 240px; padding: 30px; }
        .perfil-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            margin-bottom: 20px;
        }
        .avatar-circle {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #40c4d4;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            color: white;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>
    <div>
        <span class="me-3"><strong>${usuarioLogado.nome}</strong></span>
        <a href="${pageContext.request.contextPath}/logout"
           class="btn btn-danger btn-sm">Sair</a>
    </div>
</div>

<div class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard">🏠 Catálogo</a>
    <a href="${pageContext.request.contextPath}/perfil" class="active">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/meus-emprestimos">📖 Meus Empréstimos</a>
    <a href="${pageContext.request.contextPath}/minhas-reservas">🔖 Minhas Reservas</a>
    <a href="${pageContext.request.contextPath}/multas">💰 Multas</a>
    <a href="${pageContext.request.contextPath}/logout"
       style="color: #dc3545;">🚪 Sair</a>
</div>

<div class="content">

    <div class="perfil-card">
        <div class="d-flex align-items-center gap-4 mb-4">
            <div class="avatar-circle">
                ${usuarioLogado.nome.substring(0,1).toUpperCase()}
            </div>
            <div>
                <h3 class="mb-1">${usuarioLogado.nome}</h3>
                <span class="badge bg-info text-white">${usuarioLogado.tipo}</span>
            </div>
        </div>

        <hr>

        <div class="row mt-3">

            <div class="col-md-6 mb-3">
                <label class="text-muted small">E-mail</label>
                <p class="fw-bold">${usuarioLogado.email}</p>
            </div>

            <div class="col-md-6 mb-3">
                <label class="text-muted small">Telefone</label>
                <p class="fw-bold">
                    ${not empty usuarioLogado.telefone
                        ? usuarioLogado.telefone : 'Não informado'}
                </p>
            </div>

            <%-- RA — só para estudante via PerfilServlet --%>
            <c:if test="${usuarioLogado.tipo == 'ESTUDANTE'}">
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">RA</label>
                    <p class="fw-bold">
                        ${not empty ra ? ra : 'Não informado'}
                    </p>
                </div>
            </c:if>

            <%-- CPF — para usuário comum --%>
            <c:if test="${usuarioLogado.tipo == 'COMUM'}">
                <div class="col-md-6 mb-3">
                    <label class="text-muted small">CPF</label>
                    <p class="fw-bold">
                        ${not empty usuarioLogado.cpf
                            ? usuarioLogado.cpf : 'Não informado'}
                    </p>
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

    <%-- Cards de resumo --%>
    <div class="row">
        <div class="col-md-4">
            <div class="card p-3 shadow-sm text-center"
                 style="border-left: 5px solid #40c4d4;">
                <h6 class="text-muted">Empréstimos Ativos</h6>
                <h3 class="text-info">${totalEmprestimos}</h3>
                <a href="${pageContext.request.contextPath}/meus-emprestimos"
                   class="btn btn-sm btn-outline-info mt-2">Ver</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3 shadow-sm text-center"
                 style="border-left: 5px solid #dc3545;">
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
            <div class="card p-3 shadow-sm text-center"
                 style="border-left: 5px solid #ffc107;">
                <h6 class="text-muted">Reservas Ativas</h6>
                <h3 class="text-warning">${totalReservas}</h3>
                <a href="${pageContext.request.contextPath}/minhas-reservas"
                   class="btn btn-sm btn-outline-warning mt-2">Ver</a>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
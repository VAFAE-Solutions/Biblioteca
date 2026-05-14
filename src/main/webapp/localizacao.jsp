<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Localização - Library Digital</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; }
        .page-hero {
            background: linear-gradient(135deg, #40c4d4, #27aab5);
            color: white;
            padding: 80px 0;
            text-align: center;
        }
        .unidade-card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            border-left: 5px solid #40c4d4;
        }
    </style>
</head>
<body>

<header class="main-header">
    <div class="header-top">
        <div class="logo-box">
            <a href="${pageContext.request.contextPath}/home">
                <img src="https://cdn-icons-png.flaticon.com/512/29/29302.png">
            </a>
        </div>
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/home" method="get" class="search-wrapper">
                <input type="text" name="txtBusca" placeholder="Procurar Livro ou Autor">
                <button type="submit">🔍</button>
            </form>
        </div>
        <div class="user-box">
            <div class="auth-links">
                <a href="${pageContext.request.contextPath}/login">Entrar</a>
                <a href="${pageContext.request.contextPath}/cadastro">Cadastrar-se</a>
            </div>
            <div class="avatar"></div>
        </div>
    </div>
    <nav class="nav-bar">
        <ul>
            <li><a href="${pageContext.request.contextPath}/sobre">Sobre Nós</a></li>
            <li><a href="${pageContext.request.contextPath}/generos">Gêneros</a></li>
            <li><a href="${pageContext.request.contextPath}/localizacao" style="color:#40c4d4; font-weight:bold;">Localização</a></li>
            <li><a href="${pageContext.request.contextPath}/duvidas">Dúvidas</a></li>
            <li><a href="${pageContext.request.contextPath}/contato">Contato</a></li>
        </ul>
    </nav>
</header>

<div class="page-hero">
    <h1>📍 Nossas Unidades</h1>
    <p class="lead mt-3">Encontre a biblioteca mais próxima de você</p>
</div>

<div class="container py-5">
    <div class="row g-4">
        <c:forEach var="u" items="${unidades}">
            <div class="col-md-6">
                <div class="card unidade-card p-4">
                    <h4 class="fw-bold text-info">🏛️ ${u.nome}</h4>
                    <hr>
                    <p><strong>📍 Endereço:</strong> ${u.endereco}</p>
                    <p><strong>📞 Telefone:</strong>
                        ${not empty u.telefone ? u.telefone : 'Não informado'}
                    </p>
                    <p><strong>🕐 Horário:</strong>
                        ${not empty u.horarioFuncionamento ? u.horarioFuncionamento : 'Não informado'}
                    </p>
                    <a href="${pageContext.request.contextPath}/login"
                       class="btn btn-outline-info mt-2">
                        Acessar Acervo →
                    </a>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty unidades}">
            <div class="col-12 text-center text-muted py-5">
                Nenhuma unidade cadastrada no momento.
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
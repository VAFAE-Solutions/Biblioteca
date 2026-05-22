<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Gêneros - Library Digital</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; }
        .page-hero { background: linear-gradient(135deg, #40c4d4, #27aab5); color: white; padding: 80px 0; text-align: center; }
        .genero-card { border: none; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.08); transition: transform 0.2s; cursor: pointer; }
        .genero-card:hover { transform: translateY(-5px); }
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
            <c:choose>
                <c:when test="${not empty sessionScope.usuarioLogado}">
                    <c:choose>
                        <c:when test="${sessionScope.usuarioLogado.tipo == 'ADMIN' || sessionScope.usuarioLogado.tipo == 'BIBLIOTECARIO'}">
                            <a href="${pageContext.request.contextPath}/admin" style="padding: 8px 16px; background: #fff; color: #39c3cf; border-radius: 8px; text-decoration: none; font-weight: bold; border: 1px solid #39c3cf;">← Voltar ao Painel</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/dashboard" style="padding: 8px 16px; background: #fff; color: #39c3cf; border-radius: 8px; text-decoration: none; font-weight: bold; border: 1px solid #39c3cf;">← Voltar ao Catálogo</a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <div class="auth-links">
                        <a href="${pageContext.request.contextPath}/login">Entrar</a>
                        <a href="${pageContext.request.contextPath}/cadastro">Cadastrar-se</a>
                    </div>
                    <div class="avatar"></div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <nav class="nav-bar">
        <ul>
            <li><a href="${pageContext.request.contextPath}/sobre">Sobre Nós</a></li>
            <li><a href="${pageContext.request.contextPath}/generos" style="color:#40c4d4; font-weight:bold;">Gêneros</a></li>
            <li><a href="${pageContext.request.contextPath}/localizacao">Localização</a></li>
            <li><a href="${pageContext.request.contextPath}/duvidas">Dúvidas</a></li>
            <li><a href="${pageContext.request.contextPath}/contato">Contato</a></li>
        </ul>
    </nav>
</header>

<div class="page-hero">
    <h1>📚 Gêneros do Acervo</h1>
    <p class="lead mt-3">Explore nossos livros por categoria</p>
</div>

<div class="container py-5">
    <div class="row g-4">
        <c:set var="generos" value="Aventura,Fantasia,Ficção,Ficção Científica,Manga,Poesia,Romance,Tecnologia,Terror" />
        <c:forEach var="genero" items="${generos}">
            <div class="col-md-4">
                <a href="${pageContext.request.contextPath}/buscar?txtBusca=${genero}&filtro=genero" style="text-decoration: none;">
                    <div class="card genero-card p-4">
                        <div class="d-flex align-items-center gap-3">
                            <span style="font-size: 2rem;">
                            <c:choose>
                                <c:when test="${genero == 'Aventura'}">&#9876;&#65039;</c:when>     <%-- ⚔️ --%>
                                <c:when test="${genero == 'Fantasia'}">&#129503;</c:when>            <%-- 🧝 --%>
                                <c:when test="${genero == 'Ficção'}">&#127756;</c:when>              <%-- 🌌 --%>
                                <c:when test="${genero == 'Ficção Científica'}">&#128640;</c:when>   <%-- 🚀 --%>
                                <c:when test="${genero == 'Manga'}">&#127945;</c:when>               <%-- 🎌 --%>
                                <c:when test="${genero == 'Poesia'}">&#128220;</c:when>              <%-- 📜 --%>
                                <c:when test="${genero == 'Romance'}">&#10084;&#65039;</c:when>      <%-- ❤️ --%>
                                <c:when test="${genero == 'Tecnologia'}">&#128187;</c:when>          <%-- 💻 --%>
                                <c:when test="${genero == 'Terror'}">&#128128;</c:when>              <%-- 💀 --%>
                                <c:otherwise>&#128214;</c:otherwise>                                 <%-- 📖 --%>
                            </c:choose>
                            </span>
                            <div>
                                <h5 class="fw-bold mb-0 text-dark">${genero}</h5>
                                <small class="text-muted">Ver livros →</small>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
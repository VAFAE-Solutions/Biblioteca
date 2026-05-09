<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Garante que sempre passa pelo HomeServlet --%>
<% if (request.getAttribute("livros") == null) {
    response.sendRedirect(request.getContextPath() + "/home");
    return;
} %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca | Início</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header class="main-header">
    <div class="header-top">

        <div class="logo-box">
            <img src="https://cdn-icons-png.flaticon.com/512/29/29302.png">
        </div>

        <div class="search-box">
            <form action="${pageContext.request.contextPath}/home"
                  method="get" class="search-wrapper">
                <input type="text" name="txtBusca"
                       value="${termoPesquisado}"
                       placeholder="Procurar Livro ou Autor">
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
            <li>Sobre Nós</li>
            <li>Gêneros</li>
            <li>Localização</li>
            <li>Dúvidas</li>
            <li>Contato</li>
        </ul>
    </nav>
</header>

<main class="home-wrapper">

    <%-- Resultados de busca --%>
    <c:if test="${not empty termoPesquisado}">
        <div class="highlights-container">
            <h2>Resultados para: "${termoPesquisado}"</h2>
            <div class="books-grid">
                <c:forEach var="livro" items="${livros}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}"
                       class="book-card">
                        <img src="${not empty livro.capaUrl ? livro.capaUrl : ''}"
                             onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:forEach>
            </div>
            <c:if test="${empty livros}">
                <p style="padding: 20px;">Nenhum livro encontrado.</p>
            </c:if>
            <hr>
        </div>
    </c:if>

    <div class="highlights-container">
        <h2>Em alta!</h2>
        <div class="books-grid">
            <a href="${pageContext.request.contextPath}/detalhes?id=1" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/8228691-L.jpg">
                <p>Don Quixote</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=2" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/8231856-L.jpg">
                <p>Dom Casmurro</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=3" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/8231990-L.jpg">
                <p>Demian</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=4" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/8231995-L.jpg">
                <p>Drácula</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=5" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/8232001-L.jpg">
                <p>A Divina Comédia</p>
            </a>
        </div>
    </div>

    <div class="highlights-container">
        <h2>Recomendações da Equipe!</h2>
        <div class="books-grid">
            <a href="${pageContext.request.contextPath}/detalhes?id=6" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/8099256-L.jpg">
                <p>Java for Dummies</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=7" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/7222246-L.jpg">
                <p>Moby Dick</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=8" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/7984916-L.jpg">
                <p>Harry Potter</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=9" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/7222256-L.jpg">
                <p>A Guerra dos Mundos</p>
            </a>
            <a href="${pageContext.request.contextPath}/detalhes?id=10" class="book-card">
                <img src="https://covers.openlibrary.org/b/id/10958337-L.jpg">
                <p>Jujutsu Kaisen</p>
            </a>
        </div>
    </div>

</main>

</body>
</html>
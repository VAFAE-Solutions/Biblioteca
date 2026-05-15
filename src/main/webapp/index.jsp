<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Garante que sempre passa pelo HomeServlet --%>
<% if (request.getAttribute("todosLivros") == null) {
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
            <a href="${pageContext.request.contextPath}/home">
                <img src="https://cdn-icons-png.flaticon.com/512/29/29302.png">
            </a>
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
            <%-- ✅ Logado — mostra botão voltar conforme tipo --%>
            <c:choose>
                <c:when test="${not empty sessionScope.usuarioLogado}">
                    <c:choose>
                        <c:when test="${sessionScope.usuarioLogado.tipo == 'ADMIN' ||
                                       sessionScope.usuarioLogado.tipo == 'BIBLIOTECARIO'}">
                            <a href="${pageContext.request.contextPath}/admin"
                               style="padding: 8px 16px; background: #fff;
                                      color: #39c3cf; border-radius: 8px;
                                      text-decoration: none; font-weight: bold;
                                      border: 1px solid #39c3cf;">
                                ← Voltar ao Painel
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/dashboard"
                               style="padding: 8px 16px; background: #fff;
                                      color: #39c3cf; border-radius: 8px;
                                      text-decoration: none; font-weight: bold;
                                      border: 1px solid #39c3cf;">
                                ← Voltar ao Catálogo
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <%-- Não logado — mostra entrar/cadastrar --%>
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
            <li><a href="${pageContext.request.contextPath}/generos">Gêneros</a></li>
            <li><a href="${pageContext.request.contextPath}/localizacao">Localização</a></li>
            <li><a href="${pageContext.request.contextPath}/duvidas">Dúvidas</a></li>
            <li><a href="${pageContext.request.contextPath}/contato">Contato</a></li>
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

    <%-- Em alta --%>
    <div class="highlights-container">
        <h2>Em alta!</h2>
        <div class="books-grid">
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 4}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 2}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 5}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 6}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 7}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <%-- Recomendações --%>
    <div class="highlights-container">
        <h2>Recomendações da Equipe!</h2>
        <div class="books-grid">
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 8}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 9}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 10}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 11}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
            <c:forEach var="livro" items="${todosLivros}">
                <c:if test="${livro.id == 12}">
                    <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}" class="book-card">
                        <img src="${livro.capaUrl}" onerror="this.src='https://via.placeholder.com/200x300?text=Sem+Capa'">
                        <p>${livro.titulo}</p>
                    </a>
                </c:if>
            </c:forEach>
        </div>
    </div>

</main>

</body>
</html>
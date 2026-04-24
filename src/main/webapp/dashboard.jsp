<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca Digital - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body { background-color: #f4f4f4; }

        /* TOPO */
        .topbar {
            background-color: #40c4d4;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            color: white;
        }

        .search-box { width: 300px; }

        /* SIDEBAR */
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

        /* CONTEÚDO */
        .content {
            margin-left: 240px;
            padding: 30px;
        }

        .profile-box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }

        .profile-img {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #eee;
            background-image: url('https://cdn-icons-png.flaticon.com/512/149/149071.png');
            background-size: cover;
        }

        /* LIVROS */
        .card-img-top { height: 200px; object-fit: cover; }
        .book-card { transition: transform 0.2s; border: none; }
        .book-card:hover { transform: scale(1.03); }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>

    <form class="d-flex" action="dashboard" method="get">
        <input class="form-control me-2 search-box"
               type="search"
               name="txtBusca"
               value="${termoPesquisado}"
               placeholder="Procurar Livro ou Autor">
        <button class="btn btn-light" type="submit">🔍</button>
    </form>

    <div>
        <button class="btn btn-outline-light me-2 btn-sm">Carrinho</button>
        <span class="me-3"><strong>${usuarioLogado.email}</strong></span>
        <a href="logout" class="btn btn-danger btn-sm">Sair</a>
    </div>
</div>

<div class="sidebar">
    <a href="dashboard">🏠 Catálogo</a>
    <a href="#">👤 Perfil</a>
    <%-- AJUSTE: Link funcional para o Servlet de Empréstimos --%>
    <a href="meus-emprestimos" class="fw-bold text-primary">📖 Meus Empréstimos</a>
    <a href="#">🔔 Notificações</a>
    <a href="#">❤️ Lista de Desejo</a>
    <a href="#">⚙️ Configurações</a>
</div>

<div class="content">

    <div class="profile-box">
        <div>
            <%-- AJUSTE: Verificando se o nome existe, caso contrário usa o e-mail --%>
            <h3>Bem Vindo, ${not empty usuarioLogado.nome ? usuarioLogado.nome : usuarioLogado.email}</h3>
            <p class="text-muted mb-0">CPF Cadastrado: ${usuarioLogado.cpf}</p>
        </div>
        <div class="profile-img"></div>
    </div>

    <h4 class="mb-4">
        <c:choose>
            <c:when test="${not empty termoPesquisado}">
                🔎 Resultados para: "${termoPesquisado}"
            </c:when>
            <c:otherwise>
                📖 Catálogo de Livros
            </c:otherwise>
        </c:choose>
    </h4>

    <div class="row">
        <c:forEach var="livro" items="${livros}">
            <div class="col-md-3 mb-4">
                <div class="card h-100 book-card shadow-sm">
                    <img src="${not empty livro.capaUrl ? livro.capaUrl : 'https://via.placeholder.com/200x300?text=Sem+Capa'}"
                         class="card-img-top" alt="${livro.titulo}">

                    <div class="card-body">
                        <h6 class="card-title text-truncate">${livro.titulo}</h6>
                        <p class="card-text small text-muted">${livro.autor}</p>
                    </div>

                    <div class="card-footer bg-transparent border-top-0">
                        <a href="detalhes?id=${livro.id}" class="btn btn-info text-white btn-sm w-100">
                            Ver Detalhes
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty livros}">
            <div class="col-12">
                <div class="alert alert-light border shadow-sm mt-3">
                    Nenhum livro encontrado para "<strong>${termoPesquisado}</strong>".
                    <br>
                    <a href="dashboard" class="alert-link text-info">Clique aqui para ver todos os livros.</a>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
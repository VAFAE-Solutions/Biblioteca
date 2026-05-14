<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Sobre Nós - Library Digital</title>
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
        .card-equipe {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: transform 0.2s;
        }
        .card-equipe:hover { transform: translateY(-5px); }
        .avatar-membro {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: linear-gradient(135deg, #40c4d4, #27aab5);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            color: white;
            font-weight: bold;
            margin: 0 auto 15px;
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
            <li><a href="${pageContext.request.contextPath}/sobre" style="color:#40c4d4; font-weight:bold;">Sobre Nós</a></li>
            <li><a href="${pageContext.request.contextPath}/generos">Gêneros</a></li>
            <li><a href="${pageContext.request.contextPath}/localizacao">Localização</a></li>
            <li><a href="${pageContext.request.contextPath}/duvidas">Dúvidas</a></li>
            <li><a href="${pageContext.request.contextPath}/contato">Contato</a></li>
        </ul>
    </nav>
</header>

<div class="page-hero">
    <h1>📚 Sobre a Library Digital</h1>
    <p class="lead mt-3">Conectando pessoas ao conhecimento desde 2024</p>
</div>

<div class="container py-5">

    <div class="row align-items-center mb-5">
        <div class="col-md-6">
            <h2 class="fw-bold mb-3">Nossa Missão</h2>
            <p class="text-muted fs-5">
                A Library Digital nasceu com o objetivo de democratizar o acesso
                ao conhecimento, oferecendo um sistema moderno e eficiente de
                gerenciamento de biblioteca para estudantes, professores e
                entusiastas da leitura.
            </p>
            <p class="text-muted fs-5">
                Acreditamos que livros transformam vidas e que toda pessoa merece
                acesso fácil e rápido ao acervo bibliográfico.
            </p>
        </div>
        <div class="col-md-6 text-center">
            <img src="https://cdn-icons-png.flaticon.com/512/2232/2232688.png"
                 width="250" alt="Biblioteca">
        </div>
    </div>

    <hr class="my-5">

    <h2 class="fw-bold text-center mb-5">Nossa Equipe</h2>
    <div class="row g-4 justify-content-center">

        <div class="col-md-4">
            <div class="card card-equipe p-4 text-center">
                <div class="avatar-membro">A</div>
                <h5 class="fw-bold">Angélica Feitosa</h5>
                <span class="badge bg-info text-white mb-2">Banco de Dados</span>
                <p class="text-muted small">RA: 2404054</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card card-equipe p-4 text-center">
                <div class="avatar-membro">V</div>
                <h5 class="fw-bold">Vinicius Rodrigues</h5>
                <span class="badge bg-success mb-2">Back-end</span>
                <p class="text-muted small">RA: 2402432</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card card-equipe p-4 text-center">
                <div class="avatar-membro">F</div>
                <h5 class="fw-bold">Felipe Decio</h5>
                <span class="badge bg-warning text-dark mb-2">Front-end</span>
                <p class="text-muted small">RA: 2301598</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card card-equipe p-4 text-center">
                <div class="avatar-membro">A</div>
                <h5 class="fw-bold">Amanda Aparecida</h5>
                <span class="badge bg-warning text-dark mb-2">Front-end</span>
                <p class="text-muted small">RA: 2301546</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card card-equipe p-4 text-center">
                <div class="avatar-membro">E</div>
                <h5 class="fw-bold">Erik Willian</h5>
                <span class="badge bg-success mb-2">Back-end</span>
                <p class="text-muted small">RA: 2401537</p>
            </div>
        </div>

    </div>

    <hr class="my-5">

    <div class="row text-center g-4">
        <div class="col-md-4">
            <div class="card card-equipe p-4">
                <h1>📖</h1>
                <h3 class="fw-bold text-info">12+</h3>
                <p class="text-muted">Livros no Acervo</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card card-equipe p-4">
                <h1>👥</h1>
                <h3 class="fw-bold text-info">100+</h3>
                <p class="text-muted">Usuários Cadastrados</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card card-equipe p-4">
                <h1>🏫</h1>
                <h3 class="fw-bold text-info">1</h3>
                <p class="text-muted">Unidade Disponível</p>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
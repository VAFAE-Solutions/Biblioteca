<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Dúvidas - Library Digital</title>
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
        .accordion-button:not(.collapsed) {
            background-color: #e8f7f8;
            color: #27aab5;
        }
        .accordion-button:focus { box-shadow: none; }
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
            <li><a href="${pageContext.request.contextPath}/localizacao">Localização</a></li>
            <li><a href="${pageContext.request.contextPath}/duvidas" style="color:#40c4d4; font-weight:bold;">Dúvidas</a></li>
            <li><a href="${pageContext.request.contextPath}/contato">Contato</a></li>
        </ul>
    </nav>
</header>

<div class="page-hero">
    <h1>❓ Dúvidas Frequentes</h1>
    <p class="lead mt-3">Encontre respostas para as perguntas mais comuns</p>
</div>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8">

            <div class="accordion shadow-sm" id="faqAccordion">

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button" type="button"
                                data-bs-toggle="collapse" data-bs-target="#faq1">
                            Como faço para emprestar um livro?
                        </button>
                    </h2>
                    <div id="faq1" class="accordion-collapse collapse show"
                         data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-muted">
                            Faça login no sistema, acesse o catálogo, escolha o livro desejado
                            e clique em "Emprestar Agora". O livro ficará disponível para retirada
                            na unidade selecionada.
                        </div>
                    </div>
                </div>

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button"
                                data-bs-toggle="collapse" data-bs-target="#faq2">
                            Qual o prazo de devolução?
                        </button>
                    </h2>
                    <div id="faq2" class="accordion-collapse collapse"
                         data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-muted">
                            O prazo varia conforme o tipo de usuário: <strong>Estudantes</strong>
                            têm 15 dias e <strong>Usuários Comuns</strong> têm 7 dias para
                            devolução do livro.
                        </div>
                    </div>
                </div>

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button"
                                data-bs-toggle="collapse" data-bs-target="#faq3">
                            Quantos livros posso pegar por vez?
                        </button>
                    </h2>
                    <div id="faq3" class="accordion-collapse collapse"
                         data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-muted">
                            <strong>Estudantes</strong> podem ter até 5 livros emprestados
                            simultaneamente. <strong>Usuários Comuns</strong> podem ter até
                            3 livros.
                        </div>
                    </div>
                </div>

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button"
                                data-bs-toggle="collapse" data-bs-target="#faq4">
                            O que acontece se eu atrasar a devolução?
                        </button>
                    </h2>
                    <div id="faq4" class="accordion-collapse collapse"
                         data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-muted">
                            Será gerada uma multa de <strong>R$ 2,00 por dia de atraso</strong>.
                            Enquanto houver multas pendentes, não será possível realizar novos
                            empréstimos ou reservas.
                        </div>
                    </div>
                </div>

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button"
                                data-bs-toggle="collapse" data-bs-target="#faq5">
                            Como faço uma reserva?
                        </button>
                    </h2>
                    <div id="faq5" class="accordion-collapse collapse"
                         data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-muted">
                            Quando um livro estiver indisponível, você pode entrar na fila de
                            espera clicando em "Reservar na Fila". Você será notificado quando
                            o livro estiver disponível.
                        </div>
                    </div>
                </div>

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button"
                                data-bs-toggle="collapse" data-bs-target="#faq6">
                            Como me cadastro no sistema?
                        </button>
                    </h2>
                    <div id="faq6" class="accordion-collapse collapse"
                         data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-muted">
                            Clique em "Cadastrar-se" no menu superior, preencha seus dados
                            básicos e complete o cadastro. Estudantes precisam informar
                            o RA (Registro Acadêmico).
                        </div>
                    </div>
                </div>

            </div>

            <div class="text-center mt-5">
                <p class="text-muted">Ainda tem dúvidas?</p>
                <a href="${pageContext.request.contextPath}/contato"
                   class="btn btn-info text-white px-4">
                    Entre em Contato →
                </a>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
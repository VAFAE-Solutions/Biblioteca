<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Contato - Library Digital</title>
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
        .contato-card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }
        .info-card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            border-left: 5px solid #40c4d4;
        }
        .btn-enviar {
            background: linear-gradient(135deg, #40c4d4, #27aab5);
            border: none;
            color: white;
            padding: 12px 40px;
            border-radius: 8px;
            font-size: 16px;
        }
        .btn-enviar:hover { opacity: 0.9; color: white; }
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
            <li><a href="${pageContext.request.contextPath}/duvidas">Dúvidas</a></li>
            <li><a href="${pageContext.request.contextPath}/contato" style="color:#40c4d4; font-weight:bold;">Contato</a></li>
        </ul>
    </nav>
</header>

<div class="page-hero">
    <h1>✉️ Entre em Contato</h1>
    <p class="lead mt-3">Estamos aqui para ajudar!</p>
</div>

<div class="container py-5">
    <div class="row g-4">

        <%-- Informações de contato --%>
        <div class="col-md-4">
            <div class="card info-card p-4 mb-3">
                <h5>📧 E-mail</h5>
                <p class="text-muted mb-0">biblioteca@library.com</p>
            </div>
            <div class="card info-card p-4 mb-3">
                <h5>📞 Telefone</h5>
                <p class="text-muted mb-0">(11) 3333-4444</p>
            </div>
            <div class="card info-card p-4 mb-3">
                <h5>🕐 Horário de Atendimento</h5>
                <p class="text-muted mb-0">
                    Segunda a Sexta: 8h às 20h<br>
                    Sábado: 8h às 14h
                </p>
            </div>
            <div class="card info-card p-4">
                <h5>📍 Endereço</h5>
                <p class="text-muted mb-0">
                    Rua da Biblioteca, 123<br>
                    São Paulo - SP
                </p>
            </div>
        </div>

        <%-- Formulário de contato --%>
        <div class="col-md-8">
            <div class="card contato-card p-4">
                <h4 class="fw-bold mb-4">Envie sua mensagem</h4>

                <c:if test="${param.enviado == 'sucesso'}">
                    <div class="alert alert-success">
                        ✅ Mensagem enviada com sucesso! Retornaremos em breve.
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/contato" method="post">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Nome *</label>
                            <input type="text" name="nome" class="form-control"
                                   placeholder="Seu nome" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">E-mail *</label>
                            <input type="email" name="email" class="form-control"
                                   placeholder="seu@email.com" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold">Assunto *</label>
                            <input type="text" name="assunto" class="form-control"
                                   placeholder="Assunto da mensagem" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold">Mensagem *</label>
                            <textarea name="mensagem" class="form-control"
                                      rows="5" placeholder="Digite sua mensagem..."
                                      required></textarea>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-enviar w-100">
                                📨 Enviar Mensagem
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
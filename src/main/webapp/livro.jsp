<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Detalhes - ${livro.titulo}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .book-cover { max-width: 300px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.2); }
        body { background-color: #f8f9fa; padding-top: 50px; }
        .alert-link { text-decoration: underline; }
    </style>
</head>
<body>

<div class="container bg-white p-5 rounded shadow-sm">

    <%-- MENSAGENS DE FEEDBACK --%>
    <c:if test="${param.reserva == 'sucesso'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>✅ Reservado com Sucesso!</strong>
            O livro já aparece na sua lista.
            <a href="meus-emprestimos" class="alert-link mx-2">Ir para Meus Empréstimos</a>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.reserva == 'erro'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>❌ Erro na Reserva</strong>
            Não há exemplares disponíveis ou você já possui este livro.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-4 text-center">
            <img src="${livro.capaUrl}" class="book-cover mb-3" onerror="this.src='https://via.placeholder.com/300x450?text=Sem+Capa'">
            <p><a href="dashboard" class="btn btn-outline-secondary w-100">Voltar ao Catálogo</a></p>
        </div>

        <div class="col-md-8">
            <h1 class="display-5 fw-bold">${livro.titulo}</h1>

            <%-- Status Dinâmico --%>
            <c:choose>
                <c:when test="${param.reserva == 'sucesso'}">
                    <p class="text-warning fw-bold">Status: Reservado por você</p>
                </c:when>
                <c:otherwise>
                    <p class="text-success fw-bold">Status: Disponível!</p>
                </c:otherwise>
            </c:choose>

            <hr>

            <div class="mb-4">
                <p><strong>Autor:</strong> ${livro.autor}</p>
                <p><strong>Editora:</strong> ${livro.editora}</p>
            </div>

            <div class="mb-4">
                <h5>Descrição:</h5>
                <p class="text-muted">
                    ${not empty livro.descricao ? livro.descricao : "Obra clássica disponível para empréstimo imediato conforme as regras da biblioteca."}
                </p>
            </div>

            <%-- Lógica do Botão --%>
            <c:choose>
                <c:when test="${param.reserva == 'sucesso'}">
                    <button class="btn btn-secondary btn-lg px-5 fw-bold" disabled>Já Reservado</button>
                    <a href="meus-emprestimos" class="btn btn-outline-primary btn-lg ms-2">Ver Minha Lista</a>
                </c:when>
                <c:otherwise>
                    <a href="reservar?id=${livro.id}" class="btn btn-warning btn-lg px-5 fw-bold">Reservar Agora!</a>
                </c:otherwise>
            </c:choose>

            <br>
            <a href="#" class="btn btn-link mt-2 text-decoration-none">Adicionar à lista de desejos</a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
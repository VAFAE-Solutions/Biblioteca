<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Detalhes - ${livro.titulo}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .book-cover {
            max-width: 300px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }
        body { background-color: #f8f9fa; padding-top: 50px; }
    </style>
</head>
<body>

<div class="container bg-white p-5 rounded shadow-sm">

    <c:if test="${param.reserva == 'sucesso'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>✅ Empréstimo realizado com sucesso!</strong>
            O livro já aparece na sua lista.
            <a href="${pageContext.request.contextPath}/meus-emprestimos"
               class="alert-link mx-2">Ir para Meus Empréstimos</a>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.reserva == 'sucesso_reserva'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>✅ Reserva realizada com sucesso!</strong>
            Você está na fila de espera.
            <a href="${pageContext.request.contextPath}/minhas-reservas"
               class="alert-link mx-2">Ver Minhas Reservas</a>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.reserva == 'erro'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>❌ Empréstimo não realizado.</strong>
            Não há exemplares disponíveis. Deseja fazer uma reserva?
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.erro == 'multa_pendente'}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <strong>⚠️ Multa Pendente!</strong>
            Quite suas multas antes de realizar um empréstimo.
            <a href="${pageContext.request.contextPath}/multas"
               class="alert-link mx-2">Ver Multas</a>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.erro == 'limite_atingido'}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <strong>⚠️ Limite Atingido!</strong>
            Você atingiu o limite máximo de empréstimos simultâneos.
            <a href="${pageContext.request.contextPath}/meus-emprestimos"
               class="alert-link mx-2">Ver Meus Empréstimos</a>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.erro == 'reserva_erro'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>❌ Erro na reserva.</strong>
            Não foi possível realizar a reserva. Tente novamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-4 text-center">

            <img src="${not empty livro.capaUrl ? livro.capaUrl : ''}"
                 class="book-cover mb-3"
                 onerror="this.src='https://via.placeholder.com/300x450?text=Sem+Capa'">

            <div class="mb-3">
                <c:choose>
                    <c:when test="${exemplarDisponivel}">
                        <span class="status-disponivel">✅ Disponível!</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status-indisponivel">❌ Indisponível</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- ✅ Voltar para o lugar certo conforme o perfil --%>
            <p>
                <c:choose>
                    <c:when test="${not empty sessionScope.usuarioLogado &&
                                   (sessionScope.usuarioLogado.tipo == 'ADMIN' ||
                                    sessionScope.usuarioLogado.tipo == 'BIBLIOTECARIO')}">
                        <a href="${pageContext.request.contextPath}/admin"
                           class="btn btn-outline-secondary w-100">Voltar ao Painel</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/dashboard"
                           class="btn btn-outline-secondary w-100">Voltar ao Catálogo</a>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="col-md-8">
            <h1 class="display-5 fw-bold">${livro.titulo}</h1>
            <hr>

            <div class="mb-4">
                <p><strong>Autor:</strong> ${livro.autor}</p>
                <p><strong>Editora:</strong> ${livro.editora}</p>
                <p><strong>Ano:</strong> ${livro.anoPublicacao}</p>
                <p><strong>Gênero:</strong> ${livro.genero}</p>
            </div>

            <div class="mb-4">
                <h5>Descrição:</h5>
                <p class="text-muted">
                    ${not empty livro.descricao ? livro.descricao :
                    "Obra disponível para empréstimo conforme as regras da biblioteca."}
                </p>
            </div>

            <c:choose>

                <c:when test="${param.reserva == 'sucesso'}">
                    <button class="btn btn-secondary btn-lg px-5 fw-bold" disabled>
                        Já Emprestado
                    </button>
                    <a href="${pageContext.request.contextPath}/meus-emprestimos"
                       class="btn btn-outline-primary btn-lg ms-2">
                        Ver Minha Lista
                    </a>
                </c:when>

                <c:when test="${param.reserva == 'sucesso_reserva'}">
                    <button class="btn btn-secondary btn-lg px-5 fw-bold" disabled>
                        Já Reservado
                    </button>
                    <a href="${pageContext.request.contextPath}/minhas-reservas"
                       class="btn btn-outline-primary btn-lg ms-2">
                        Ver Minhas Reservas
                    </a>
                </c:when>

                <c:when test="${empty sessionScope.usuarioLogado}">
                    <a href="${pageContext.request.contextPath}/login"
                       class="btn btn-warning btn-lg px-5 fw-bold">
                        Faça login para Emprestar
                    </a>
                </c:when>

                <c:when test="${exemplarDisponivel}">
                    <form action="${pageContext.request.contextPath}/reservar"
                          method="post">
                        <input type="hidden" name="livroId" value="${livro.id}">
                        <button type="submit"
                                class="btn btn-warning btn-lg px-5 fw-bold">
                            Emprestar Agora!
                        </button>
                    </form>
                </c:when>

                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/reservar-livro"
                          method="post">
                        <input type="hidden" name="livroId" value="${livro.id}">
                        <button type="submit"
                                class="btn btn-primary btn-lg px-5 fw-bold">
                            🔖 Reservar na Fila
                        </button>
                    </form>
                    <small class="text-muted d-block mt-2">
                        Este livro está indisponível.
                        Reserve seu lugar na fila de espera!
                    </small>
                </c:otherwise>

            </c:choose>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
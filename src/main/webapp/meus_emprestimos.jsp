<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Meus Empréstimos - Biblioteca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; padding-top: 50px; }
        .table-container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
        .alert-multa { border-left: 5px solid #dc3545; }
        .alert-atraso { border-left: 5px solid #fd7e14; }
    </style>
</head>
<body>

<div class="container">
    <div class="table-container shadow-sm">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-primary">📚 Meus Livros Emprestados</h2>
            <a href="${pageContext.request.contextPath}/dashboard"
               class="btn btn-outline-secondary">Voltar ao Catálogo</a>
        </div>

        <%-- Aviso de devolução em atraso --%>
        <c:if test="${temAtrasado}">
            <div class="alert alert-warning alert-atraso shadow-sm mb-4">
                <h5 class="alert-heading mb-1">⏰ Atenção! Devolução em Atraso</h5>
                <span>Você possui livros com prazo de devolução vencido.
                      Devolva o quanto antes para evitar multas adicionais.</span>
            </div>
        </c:if>

        <%-- Aviso de devolução com sucesso --%>
        <c:if test="${param.devolucao == 'sucesso'}">
            <div class="alert alert-success shadow-sm mb-4">
                <h5 class="alert-heading mb-1">✅ Devolução realizada!</h5>
                <span>O livro foi devolvido com sucesso.</span>
            </div>
        </c:if>

        <%-- Alerta de multas pendentes --%>
        <c:if test="${totalMulta > 0}">
            <div class="alert alert-danger alert-multa shadow-sm mb-4">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h5 class="alert-heading mb-1">⚠️ Multas por Atraso</h5>
                        <span>Você possui débitos pendentes referentes a livros não entregues no prazo.</span>
                    </div>
                    <div class="text-end">
                        <span class="fs-4 fw-bold">
                            R$ <fmt:formatNumber value="${totalMulta}"
                                minFractionDigits="2" maxFractionDigits="2"/>
                        </span>
                    </div>
                </div>
                <%-- ✅ Aviso de pagamento presencial --%>
                <hr>
                <p class="mb-0 small">
                    🏛️ <strong>Pagamento presencial:</strong>
                    O pagamento de multas é realizado exclusivamente nas unidades da biblioteca.
                    Após quitar o débito no balcão, o bibliotecário registrará a quitação no sistema.
                    <a href="${pageContext.request.contextPath}/localizacao" class="alert-link">
                        Ver unidades →
                    </a>
                </p>
            </div>
        </c:if>

        <table class="table table-hover">
            <thead class="table-light">
            <tr>
                <th>Livro</th>
                <th>Data de Retirada</th>
                <th>Data de Devolução</th>
                <th>Status</th>
                <th>Ação</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="emp" items="${emprestimos}">
                <tr class="${emp.status == 'ATRASADO' ? 'table-danger' : ''}">
                    <td><strong>${emp.exemplar.livro.titulo}</strong></td>
                    <td>${emp.dataEmprestimo}</td>
                    <td class="${emp.status == 'ATRASADO' ? 'text-danger fw-bold' : ''}">
                        ${emp.dataDevolucaoPrevista}
                        <c:if test="${emp.status == 'ATRASADO'}">
                            <span class="badge bg-danger ms-1">VENCIDO</span>
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${emp.status == 'ATIVO'}">
                                <span class="badge bg-success">ATIVO</span>
                            </c:when>
                            <c:when test="${emp.status == 'ATRASADO'}">
                                <span class="badge bg-danger">ATRASADO</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">FINALIZADO</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${emp.status != 'FINALIZADO'}">
                            <form action="${pageContext.request.contextPath}/reservar"
                                  method="post">
                                <input type="hidden" name="id" value="${emp.id}">
                                <input type="hidden" name="acao" value="devolver">
                                <button type="submit"
                                        class="btn btn-sm ${emp.status == 'ATRASADO'
                                               ? 'btn-danger' : 'btn-primary'}">
                                    Devolver
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty emprestimos}">
                <tr>
                    <td colspan="5" class="text-center py-5 text-muted">
                        Você não possui nenhum empréstimo ativo no momento.
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
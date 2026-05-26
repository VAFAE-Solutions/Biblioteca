<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Empréstimos Ativos - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 20px; }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center">
        <h5>📚
            <c:choose>
                <c:when test="${usuarioLogado.tipo == 'ADMIN'}">Admin Painel</c:when>
                <c:otherwise>Bibliotecário Painel</c:otherwise>
            </c:choose>
        </h5>
        <hr>
    </div>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/emprestimos" class="active">📅 Empréstimos Ativos</a>
    <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
        <a href="${pageContext.request.contextPath}/admin/unidades">🏛️ Unidades</a>
        <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    </c:if>
    <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios-unidade">👥 Usuários da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/estoque">📦 Estoque da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/reservas">🔖 Reservas Planejadas</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/admin/notificacoes">
        🔔 Notificações
        <c:if test="${totalNaoLidas > 0}">
            <span class="badge bg-danger ms-1">${totalNaoLidas}</span>
        </c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>📅 Empréstimos Ativos</h2>
        <a href="${pageContext.request.contextPath}/admin/emprestimo-presencial"
           class="btn btn-primary">+ Registrar Empréstimo Presencial</a>
    </div>

    <%-- Feedbacks --%>
    <c:if test="${param.devolucao == 'sucesso'}">
        <div class="alert alert-success">✅ Devolução registrada com sucesso!</div>
    </c:if>
    <c:if test="${param.pagamento == 'sucesso'}">
        <div class="alert alert-success">✅ Pagamento de multa registrado com sucesso!</div>
    </c:if>
    <c:if test="${param.erro == 'falha'}">
        <div class="alert alert-danger">❌ Erro ao processar operação. Tente novamente.</div>
    </c:if>

    <c:if test="${not empty emprestimosAtrasados}">
        <div class="alert alert-danger mb-4">
            ⚠️ <strong>${emprestimosAtrasados.size()}</strong> empréstimo(s) em atraso!
        </div>
        <%-- ✅ Aviso de pagamento presencial para o bibliotecário --%>
        <div class="alert alert-info mb-4">
            🏛️ <strong>Pagamento de multas:</strong>
            Registre o pagamento apenas após receber o valor presencialmente no balcão.
            O botão <strong>💰 Registrar Pagamento</strong> confirma a quitação e libera
            o usuário para novos empréstimos.
        </div>
    </c:if>

    <%-- Ativos --%>
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white">
            <strong>✅ Ativos (${emprestimosAtivos.size()})</strong>
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Livro</th>
                    <th>Usuário</th>
                    <th>Data Empréstimo</th>
                    <th>Devolução Prevista</th>
                    <th>Ação</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="emp" items="${emprestimosAtivos}">
                    <tr>
                        <td>${emp.id}</td>
                        <td><strong>${emp.exemplar.livro.titulo}</strong></td>
                        <td>${emp.usuario.nome}</td>
                        <td>${emp.dataEmprestimo}</td>
                        <td>${emp.dataDevolucaoPrevista}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/emprestimos"
                                  method="post" class="d-inline">
                                <input type="hidden" name="acao" value="devolver">
                                <input type="hidden" name="emprestimoId" value="${emp.id}">
                                <button type="submit" class="btn btn-sm btn-success">
                                    ✅ Devolver
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty emprestimosAtivos}">
                    <tr>
                        <td colspan="6" class="text-center text-muted py-4">
                            Nenhum empréstimo ativo no momento.
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <%-- Atrasados --%>
    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <strong>⏰ Atrasados (${emprestimosAtrasados.size()})</strong>
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Livro</th>
                    <th>Usuário</th>
                    <th>Data Empréstimo</th>
                    <th>Devolução Prevista</th>
                    <th>Multa</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="emp" items="${emprestimosAtrasados}">
                    <tr class="table-danger">
                        <td>${emp.id}</td>
                        <td><strong>${emp.exemplar.livro.titulo}</strong></td>
                        <td>${emp.usuario.nome}</td>
                        <td>${emp.dataEmprestimo}</td>
                        <td>${emp.dataDevolucaoPrevista}</td>
                        <td>
                            <%-- ✅ Mostra valor da multa --%>
                            <c:choose>
                                <c:when test="${not empty multasMap[emp.id]}">
                                    <span class="badge bg-danger fs-6">
                                        R$ <fmt:formatNumber
                                            value="${multasValorMap[emp.id]}"
                                            minFractionDigits="2"
                                            maxFractionDigits="2"/>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted small">Calculando...</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/emprestimos"
                                  method="post" class="d-inline">
                                <input type="hidden" name="acao" value="devolver">
                                <input type="hidden" name="emprestimoId" value="${emp.id}">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    ✅ Devolver
                                </button>
                            </form>
                            <c:if test="${not empty multasMap[emp.id]}">
                                <form action="${pageContext.request.contextPath}/admin/emprestimos"
                                      method="post" class="d-inline">
                                    <input type="hidden" name="acao" value="pagar_multa">
                                    <input type="hidden" name="multaId" value="${multasMap[emp.id]}">
                                    <button type="submit" class="btn btn-sm btn-warning"
                                            onclick="return confirm('Confirmar recebimento do pagamento presencial?')">
                                        💰 Registrar Pagamento
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty emprestimosAtrasados}">
                    <tr>
                        <td colspan="7" class="text-center text-muted py-4">
                            Nenhum empréstimo atrasado. 🎉
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
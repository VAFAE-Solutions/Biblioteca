<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Relatórios Globais - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .sidebar-admin a.active { background: #343a40; color: white; border-left: 4px solid #40c4d4; }
        .main-content { margin-left: 260px; padding: 20px; }
        .stat-card { border: none; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.08); }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center">
        <h5>📚 Admin Painel</h5>
        <hr>
    </div>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/perfil">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/admin/emprestimos">📅 Empréstimos Ativos</a>
    <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
    <a href="${pageContext.request.contextPath}/admin/unidades">🏛️ Gerenciar Unidades</a>
    <a href="${pageContext.request.contextPath}/admin/relatorios" class="active">📋 Relatórios Globais</a>
    <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    <a href="${pageContext.request.contextPath}/admin/notificacoes">
        🔔 Notificações
        <c:if test="${totalNaoLidas > 0}">
            <span class="badge bg-danger ms-1">${totalNaoLidas}</span>
        </c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <h2 class="mb-4">📋 Relatórios Globais</h2>

    <%-- Cards de estatísticas gerais --%>
    <div class="row g-4 mb-4">
        <div class="col-md-3">
            <div class="card stat-card p-3 text-center"
                 style="border-left: 5px solid #40c4d4;">
                <h6 class="text-muted">Total Empréstimos</h6>
                <h3 class="text-info">${taxaAtraso.total}</h3>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card p-3 text-center"
                 style="border-left: 5px solid #28a745;">
                <h6 class="text-muted">Ativos</h6>
                <h3 class="text-success">${taxaAtraso.ativos}</h3>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card p-3 text-center"
                 style="border-left: 5px solid #dc3545;">
                <h6 class="text-muted">Atrasados</h6>
                <h3 class="text-danger">${taxaAtraso.atrasados}</h3>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card p-3 text-center"
                 style="border-left: 5px solid #ffc107;">
                <h6 class="text-muted">Taxa de Atraso</h6>
                <h3 class="text-warning">${taxaAtraso.taxaAtraso}%</h3>
            </div>
        </div>
    </div>

    <%-- Gráficos --%>
    <div class="row g-4 mb-4">

        <%-- Gráfico pizza — status dos empréstimos --%>
        <div class="col-md-3">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-white border-0 pb-0">
                    <strong>🥧 Status</strong>
                </div>
                <div class="card-body d-flex align-items-center justify-content-center">
                    <canvas id="graficoPizza" style="max-height: 200px;"></canvas>
                </div>
            </div>
        </div>

        <%-- Gráfico barras — livros mais emprestados --%>
        <div class="col-md-9">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-white border-0 pb-0">
                    <strong>📊 Livros Mais Emprestados</strong>
                </div>
                <div class="card-body">
                    <canvas id="graficoLivros" style="max-height: 200px;"></canvas>
                </div>
            </div>
        </div>

        <%-- Gráfico barras horizontal — empréstimos por usuário --%>
        <div class="col-md-12">
            <div class="card shadow-sm">
                <div class="card-header bg-white border-0 pb-0">
                    <strong>👥 Empréstimos por Usuário</strong>
                </div>
                <div class="card-body">
                    <canvas id="graficoUsuarios" style="max-height: 150px;"></canvas>
                </div>
            </div>
        </div>

    </div>

    <div class="row g-4">

        <%-- Livros mais emprestados — tabela --%>
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header bg-white">
                    <strong>📚 Livros Mais Emprestados</strong>
                </div>
                <div class="card-body">
                    <table class="table table-sm table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Título</th>
                            <th>Autor</th>
                            <th>Total</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="l" items="${livrosMaisEmprestados}" varStatus="s">
                            <tr>
                                <td><strong>${s.index + 1}º</strong></td>
                                <td>${l.titulo}</td>
                                <td><small class="text-muted">${l.autor}</small></td>
                                <td><span class="badge bg-info text-white">${l.totalEmprestimos}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty livrosMaisEmprestados}">
                            <tr>
                                <td colspan="4" class="text-center text-muted py-3">
                                    Nenhum empréstimo registrado.
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%-- Empréstimos por usuário — tabela --%>
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header bg-white">
                    <strong>👥 Empréstimos por Usuário</strong>
                </div>
                <div class="card-body">
                    <table class="table table-sm table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>Nome</th>
                            <th>Tipo</th>
                            <th>Total</th>
                            <th>Atrasados</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="u" items="${emprestimosPorUsuario}">
                            <tr>
                                <td>${u.nome}</td>
                                <td><span class="badge bg-info text-white">${u.tipo}</span></td>
                                <td>${u.totalEmprestimos}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.atrasados > 0}">
                                            <span class="badge bg-danger">${u.atrasados}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-success">0</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty emprestimosPorUsuario}">
                            <tr>
                                <td colspan="4" class="text-center text-muted py-3">
                                    Nenhum empréstimo registrado.
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%-- Livros atrasados — tabela --%>
        <div class="col-md-12">
            <div class="card shadow-sm">
                <div class="card-header bg-white">
                    <strong>⏰ Livros Atrasados</strong>
                </div>
                <div class="card-body">
                    <table class="table table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>Livro</th>
                            <th>Autor</th>
                            <th>Usuário</th>
                            <th>Devol. Prevista</th>
                            <th>Dias de Atraso</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="a" items="${livrosAtrasados}">
                            <tr class="table-danger">
                                <td><strong>${a.titulo}</strong></td>
                                <td>${a.autor}</td>
                                <td>${a.usuarioNome}</td>
                                <td>${a.dataPrevista}</td>
                                <td>
                                    <span class="badge bg-danger">${a.diasAtraso} dias</span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty livrosAtrasados}">
                            <tr>
                                <td colspan="5" class="text-center text-muted py-3">
                                    Nenhum livro atrasado. 🎉
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>

    Chart.defaults.font.family = "'Segoe UI', sans-serif";
    Chart.defaults.font.size = 12;
    Chart.defaults.color = '#6c757d';

    // ✅ Gráfico Pizza
    new Chart(document.getElementById('graficoPizza'), {
        type: 'doughnut',
        data: {
            labels: ['Ativos', 'Atrasados', 'Finalizados'],
            datasets: [{
                data: [
                    ${taxaAtraso.ativos},
                    ${taxaAtraso.atrasados},
                    ${taxaAtraso.finalizados}
                ],
                backgroundColor: ['#20c997', '#dc3545', '#40c4d4'],
                borderWidth: 0,
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            cutout: '70%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { boxWidth: 12, padding: 12 }
                }
            }
        }
    });

    // ✅ Gráfico Barras — Livros
    new Chart(document.getElementById('graficoLivros'), {
        type: 'bar',
        data: {
            labels: [
                <c:forEach var="l" items="${livrosMaisEmprestados}" varStatus="s">
                    '${l.titulo}'<c:if test="${!s.last}">,</c:if>
                </c:forEach>
            ],
            datasets: [{
                label: 'Empréstimos',
                data: [
                    <c:forEach var="l" items="${livrosMaisEmprestados}" varStatus="s">
                        ${l.totalEmprestimos}<c:if test="${!s.last}">,</c:if>
                    </c:forEach>
                ],
                backgroundColor: 'rgba(64, 196, 212, 0.7)',
                borderColor: '#40c4d4',
                borderWidth: 1,
                borderRadius: 6,
                barThickness: 30
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: { stepSize: 1 },
                    grid: { color: 'rgba(0,0,0,0.05)' }
                },
                x: { grid: { display: false } }
            }
        }
    });

    // ✅ Gráfico Barras Horizontal — Usuários
    new Chart(document.getElementById('graficoUsuarios'), {
        type: 'bar',
        data: {
            labels: [
                <c:forEach var="u" items="${emprestimosPorUsuario}" varStatus="s">
                    '${u.nome}'<c:if test="${!s.last}">,</c:if>
                </c:forEach>
            ],
            datasets: [
                {
                    label: 'Total',
                    data: [
                        <c:forEach var="u" items="${emprestimosPorUsuario}" varStatus="s">
                            ${u.totalEmprestimos}<c:if test="${!s.last}">,</c:if>
                        </c:forEach>
                    ],
                    backgroundColor: 'rgba(64, 196, 212, 0.7)',
                    borderColor: '#40c4d4',
                    borderWidth: 1,
                    borderRadius: 4,
                    barThickness: 20
                },
                {
                    label: 'Atrasados',
                    data: [
                        <c:forEach var="u" items="${emprestimosPorUsuario}" varStatus="s">
                            ${u.atrasados}<c:if test="${!s.last}">,</c:if>
                        </c:forEach>
                    ],
                    backgroundColor: 'rgba(220, 53, 69, 0.7)',
                    borderColor: '#dc3545',
                    borderWidth: 1,
                    borderRadius: 4,
                    barThickness: 20
                }
            ]
        },
        options: {
            indexAxis: 'y',
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { boxWidth: 12, padding: 12 }
                }
            },
            scales: {
                x: {
                    beginAtZero: true,
                    ticks: { stepSize: 1 },
                    grid: { color: 'rgba(0,0,0,0.05)' }
                },
                y: { grid: { display: false } }
            }
        }
    });

</script>
</body>
</html>
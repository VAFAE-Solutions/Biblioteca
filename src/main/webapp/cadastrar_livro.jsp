<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Livro - Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .main-content { margin-left: 260px; padding: 20px; }
        .preview-capa { max-width: 150px; border-radius: 8px; display: none; margin-top: 10px; }
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
    <a href="${pageContext.request.contextPath}/admin/emprestimos">📅 Empréstimos Ativos</a>
    <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
        <a href="${pageContext.request.contextPath}/admin/relatorios">📋 Relatórios Globais</a>
        <a href="${pageContext.request.contextPath}/admin/cadastrar-bibliotecario">👨‍💼 Cadastrar Bibliotecário</a>
    </c:if>
    <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios-unidade">👥 Usuários da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/estoque">📦 Estoque da Unidade</a>
        <a href="${pageContext.request.contextPath}/admin/reservas">🔖 Reservas Planejadas</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>+ Cadastrar Novo Livro</h2>
        <a href="${pageContext.request.contextPath}/admin"
           class="btn btn-outline-secondary">← Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/cadastrar-livro" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Título *</label>
                        <input type="text" name="titulo" class="form-control" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Autor *</label>
                        <input type="text" name="autor" class="form-control" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Editora</label>
                        <input type="text" name="editora" class="form-control">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label fw-bold">Ano de Publicação</label>
                        <input type="number" name="anoPublicacao" class="form-control"
                               min="1000" max="2099">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label fw-bold">Gênero</label>
                        <input type="text" name="genero" class="form-control">
                    </div>
                    <div class="col-md-12 mb-3">
                        <label class="form-label fw-bold">URL da Capa</label>
                        <input type="text" name="capaUrl" id="capaUrl"
                               class="form-control"
                               placeholder="https://..."
                               oninput="previewCapa(this.value)">
                        <img id="previewImg" class="preview-capa"
                             src="" alt="Preview da capa">
                    </div>
                    <div class="col-md-12 mb-3">
                        <label class="form-label fw-bold">Descrição</label>
                        <textarea name="descricao" class="form-control" rows="3"></textarea>
                    </div>
                    <div class="col-md-12 mb-3">
                        <label class="form-label fw-bold">Sumário</label>
                        <textarea name="sumario" class="form-control" rows="3"></textarea>
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary px-4">
                        💾 Salvar Livro
                    </button>
                    <a href="${pageContext.request.contextPath}/admin"
                       class="btn btn-outline-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function previewCapa(url) {
        const img = document.getElementById('previewImg');
        if (url) {
            img.src = url;
            img.style.display = 'block';
            img.onerror = () => img.style.display = 'none';
        } else {
            img.style.display = 'none';
        }
    }
</script>
</body>
</html>
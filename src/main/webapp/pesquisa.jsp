<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Pesquisa de Livros</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="tela-pesquisa">

    <header class="pesquisa-cabecalho">

        <div class="pesquisa-logo">
            <img src="https://cdn-icons-png.flaticon.com/512/2232/2232688.png">
            <p>LIBRARY</p>
        </div>

        <div class="pesquisa-topo-direita">

            <div class="pesquisa-barra-azul">
                <div class="pesquisa-caixa">
                    <input type="text" id="campoPesquisa" placeholder="Procurar Livro">
                    <button onclick="pesquisarLivro()">🔍</button>
                </div>

                <div class="pesquisa-login">
                    <span>Entrar</span>
                    <span>Cadastra-se</span>
                </div>

                <div class="pesquisa-circulo"></div>
            </div>

            <nav class="pesquisa-menu">
                <a href="#">Sobre Nós</a>
                <a href="#">Gêneros</a>
                <a href="#">Localização</a>
                <a href="#">Dúvidas</a>
                <a href="#">Contate-nos!</a>
            </nav>

        </div>

    </header>

    <main class="pesquisa-conteudo">

        <h2>Pesquisa de Livros</h2>

        <div class="pesquisa-filtros">
            <select id="filtroGenero" onchange="pesquisarLivro()">
                <option value="">Todos os gêneros</option>
                <option value="classico">Clássico</option>
                <option value="romance">Romance</option>
                <option value="terror">Terror</option>
                <option value="tecnologia">Tecnologia</option>
                <option value="fantasia">Fantasia</option>
            </select>

            <select id="filtroStatus" onchange="pesquisarLivro()">
                <option value="">Todos os status</option>
                <option value="disponivel">Disponível</option>
                <option value="indisponivel">Indisponível</option>
            </select>
        </div>

        <p id="resultadoBusca"></p>

        <section id="listaLivros" class="pesquisa-livros"></section>

    </main>

</div>

<script src="pesquisa.js"></script>

</body>
</html>
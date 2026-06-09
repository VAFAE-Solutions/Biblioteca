# 📚 Library Digital — Sistema de Gerenciamento de Biblioteca

> Sistema web completo para gerenciamento de biblioteca desenvolvido pela equipe **VAFAE Solutions**  
> Faculdade Impacta — Ciência da Computação — CC 4A Noite — 2025

---

## 👥 Equipe

| Nome | RA | Função |
|------|----|--------|
| Angélica Feitosa | 2404054 | Banco de Dados |
| Vinicius Rodrigues | 2402432 | Back-end |
| Felipe Decio | 2301598 | Front-end |
| Amanda Aparecida | 2301546 | Front-end |
| Erik Willian | 2401537 | Back-end |

---

## 🛠️ Stack Tecnológica

- **Back-end:** Java 21 (Jakarta EE) + Apache Tomcat 10.1
- **Front-end:** JSP + Bootstrap 5 + CSS3
- **Banco de Dados:** MySQL 8.0
- **Pool de Conexões:** HikariCP 5.1
- **Build:** Maven 3 (gera `.war`)
- **Testes:** JUnit 5 + Mockito (600+ testes)
- **Controle de Versão:** Git + GitHub

---

## 🏗️ Arquitetura

O projeto segue o padrão **MVC** com camadas bem definidas:

```
src/main/java/org/example/demo/
├── controller/     # Servlets — recebem requisições HTTP
├── dao/            # Data Access Objects — código SQL
│   └── Persistivel.java  # Interface genérica dos DAOs
├── database/       # Conexão com o banco (HikariCP)
├── filter/         # Filtros HTTP (autenticação, notificações)
├── model/          # Entidades de domínio
└── service/        # Regras de negócio
```

---

## ⚙️ Pré-requisitos

- Java JDK 21+
- Apache Tomcat 10.1.x
- MySQL 8.0+
- Maven 3.x
- Git

---

## 🚀 Como Rodar Localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/VAFAE-Solutions/Biblioteca.git
cd Biblioteca
git checkout principal
```

### 2. Configurar o banco de dados

```sql
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Execute o script de criação das tabelas e a stored procedure `sp_autenticar_usuario`.

### 3. Criar o arquivo de configuração

Crie `src/main/resources/database.properties` (não versionado):

```properties
db.url=jdbc:mysql://localhost:3306/biblioteca
db.usuario=root
db.senha=SUA_SENHA
db.maxConnections=10
db.minIdle=2
db.connectionTimeout=30000
db.idleTimeout=600000
```

### 4. Build e execução

**Via IntelliJ (desenvolvimento):**
- Abra o projeto → Configure o Smart Tomcat → Run

**Via Tomcat standalone:**
```bash
# Gerar o WAR
mvn clean package

# Copiar para o Tomcat
copy target\demo-1.0-SNAPSHOT.war C:\tomcat10\webapps\demo.war

# Iniciar o Tomcat
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot
C:\tomcat10\bin\startup.bat
```

**Via script automático:**
```bash
# Execute na raiz do projeto
deploy.bat
```

### 5. Acessar o sistema

```
http://localhost:8080/demo/home
```

---

## 🔑 Credenciais Padrão

| Perfil | E-mail | Senha |
|--------|--------|-------|
| Admin | admin@biblioteca.com | admin123 |
| Bibliotecário | carlos@biblioteca.com | biblio123 |
| Estudante | joao@biblioteca.com | estudante123 |
| Comum | ander.jesus@biblioteca.com | comum123 |

> Todos os e-mails usam o domínio `@biblioteca.com`

---

## ✨ Funcionalidades

### Usuário (Estudante / Comum)
- Catálogo de livros com busca inteligente e filtros
- Empréstimo e reserva de livros
- Visualização de empréstimos, reservas e multas
- Perfil pessoal com edição de dados

### Bibliotecário
- Gerenciamento do acervo (cadastrar, editar, desativar livros)
- Empréstimo presencial
- Gerenciamento de usuários da unidade
- Estoque e reservas da unidade

### Administrador
- Todas as funcionalidades do bibliotecário
- Gerenciamento global de usuários
- Relatórios globais
- Gerenciamento de unidades
- Cadastro de bibliotecários

---

## 🖼️ Sistema de Capas

- Ao cadastrar um livro com URL de capa, a imagem é **baixada e salva automaticamente** no banco (`MEDIUMBLOB`)
- Fallback: banco → URL externa → `sem-capa.svg` local
- Nas listagens, apenas uma flag é carregada (sem bytes) para **performance**

---

## 🔒 Segurança

- Senhas armazenadas com **hash SHA-256**
- Autenticação via **stored procedure MySQL**
- Bloqueio automático após 3 tentativas incorretas
- Filtro de autenticação protege todas as rotas privadas
- `database.properties` excluído do versionamento

---

## 🧪 Testes

```bash
mvn test
```

600+ testes unitários cobrindo models, services e filtros com JUnit 5 + Mockito.

---

## 📁 Padrão de Commits

| Prefixo | Uso |
|---------|-----|
| `feat:` | Nova funcionalidade |
| `fix:` | Correção de bug |
| `chore:` | Configuração, dependências |
| `docs:` | Documentação |
| `refactor:` | Refatoração |
| `test:` | Testes |

---

## ⚠️ Observações

- O arquivo `database.properties` **não é versionado** — cada desenvolvedor cria o seu
- `unidade_id = 2` está hardcoded em alguns servlets (Unidade Central)
- Tomcat 10.1 é obrigatório (Jakarta EE 10 — não compatível com Tomcat 9)

---

*VAFAE Solutions — Faculdade Impacta — CC 4A Noite — 2025*

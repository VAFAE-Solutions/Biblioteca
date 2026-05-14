package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public String[] executarLoginProcedure(String email, String senhaHash) {
        String sql = "{call sp_autenticar_usuario(?, ?, ?, ?, ?)}";

        try (Connection con = Database.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senhaHash);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.VARCHAR);

            stmt.execute();

            return new String[]{
                    stmt.getString(3),
                    String.valueOf(stmt.getInt(4)),
                    stmt.getString(5)
            };

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar procedure de login: " + e.getMessage(), e);
        }
    }

    public boolean inserir(Usuario usuario) {
        String sql = """
                INSERT INTO usuario (nome, email, senha_hash, tipo, ra, cpf, telefone, unidade_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.setString(4, usuario.getTipo().name());
            ps.setString(6, usuario.getCpf());
            ps.setString(7, usuario.getTelefone());

            if (usuario instanceof UsuarioEstudante estudante) {
                ps.setInt(5, estudante.getRa());
                ps.setNull(8, Types.INTEGER);
            } else if (usuario instanceof UsuarioBibliotecario bibliotecario) {
                ps.setNull(5, Types.INTEGER);
                ps.setInt(8, bibliotecario.getUnidadeId());
            } else {
                ps.setNull(5, Types.INTEGER);
                ps.setNull(8, Types.INTEGER);
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só usuários ativos
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ? AND ativo = TRUE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só usuários ativos
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND ativo = TRUE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        }
    }

    // ✅ Lista só usuários ativos
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario WHERE ativo = TRUE";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
    }

    // ✅ Lista todos incluindo inativos — para admin ver histórico
    public List<Usuario> listarTodosIncluindoInativos() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Usuario usuario) {
        String sql = """
                UPDATE usuario SET nome = ?, email = ?, telefone = ?,
                cpf = ?, updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setString(4, usuario.getCpf());
            ps.setInt(5, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public boolean atualizarBloqueio(int id, boolean bloqueado, int tentativas) {
        String sql = """
                UPDATE usuario SET bloqueado = ?, tentativas_login = ?,
                ultima_tentativa = ?, updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, bloqueado);
            ps.setInt(2, tentativas);
            ps.setObject(3, bloqueado ? java.time.LocalDateTime.now() : null);
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar bloqueio: " + e.getMessage(), e);
        }
    }

    public boolean atualizarTentativas(Usuario usuario) {
        String sql = """
                UPDATE usuario SET tentativas_login = ?, ultima_tentativa = ?, bloqueado = ?
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getTentativasLogin());
            ps.setObject(2, usuario.getUltimaTentativa());
            ps.setBoolean(3, usuario.isBloqueado());
            ps.setInt(4, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar tentativas: " + e.getMessage(), e);
        }
    }

    public int contarEmprestimosAtivos(int usuarioId) {
        String sql = """
                SELECT COUNT(*) FROM emprestimo
                WHERE usuario_id = ? AND status IN ('ATIVO', 'ATRASADO')
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar empréstimos: " + e.getMessage(), e);
        }
    }

    // ✅ Desativar em vez de deletar
    public boolean desativar(int id) {
        String sql = "UPDATE usuario SET ativo = FALSE, updated_at = NOW() WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar usuário: " + e.getMessage(), e);
        }
    }

    // ✅ Reativar usuário
    public boolean reativar(int id) {
        String sql = "UPDATE usuario SET ativo = TRUE, updated_at = NOW() WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reativar usuário: " + e.getMessage(), e);
        }
    }

    // ✅ Mantido para compatibilidade mas não recomendado
    public boolean deletar(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");

        Usuario usuario = switch (tipo) {
            case "ESTUDANTE" -> {
                UsuarioEstudante e = new UsuarioEstudante();
                e.setRa(rs.getInt("ra"));
                yield e;
            }
            case "BIBLIOTECARIO" -> {
                UsuarioBibliotecario b = new UsuarioBibliotecario();
                b.setUnidadeId(rs.getInt("unidade_id"));
                yield b;
            }
            case "ADMIN" -> new UsuarioAdministrador();
            default      -> new Usuario();
        };

        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenhaHash(rs.getString("senha_hash"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setBloqueado(rs.getBoolean("bloqueado"));
        usuario.setAtivo(rs.getBoolean("ativo")); // ✅ novo campo
        usuario.setTentativasLogin(rs.getInt("tentativas_login"));

        Timestamp ultimaTentativa = rs.getTimestamp("ultima_tentativa");
        usuario.setUltimaTentativa(ultimaTentativa != null
                ? ultimaTentativa.toLocalDateTime() : null);

        Timestamp createdAt = rs.getTimestamp("created_at");
        usuario.setCreatedAt(createdAt != null
                ? createdAt.toLocalDateTime() : null);

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        usuario.setUpdatedAt(updatedAt != null
                ? updatedAt.toLocalDateTime() : null);

        return usuario;
    }
}
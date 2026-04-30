package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // ✅ Executa a stored procedure de autenticação
    public String[] executarLoginProcedure(String email, String senhaHash) {
        String sql = "{call sp_autenticar_usuario(?, ?, ?, ?, ?)}";

        try (Connection con = Database.getConnection();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senhaHash);
            stmt.registerOutParameter(3, Types.VARCHAR); // p_status_id
            stmt.registerOutParameter(4, Types.INTEGER); // p_usuario_id
            stmt.registerOutParameter(5, Types.VARCHAR); // p_tipo

            stmt.execute();

            return new String[]{
                    stmt.getString(3),              // status
                    String.valueOf(stmt.getInt(4)), // id
                    stmt.getString(5)               // tipo
            };

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar procedure de login: " + e.getMessage(), e);
        }
    }

    // ✅ Inserir usuário
    public boolean inserir(Usuario usuario) {
        String sql = """
                INSERT INTO usuario (nome, email, senha_hash, tipo, ra, telefone, unidade_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.setString(4, usuario.getTipo().name());
            ps.setString(6, usuario.getTelefone());

            if (usuario instanceof UsuarioEstudante estudante) {
                ps.setInt(5, estudante.getRa());
                ps.setNull(7, Types.INTEGER);
            } else if (usuario instanceof UsuarioBibliotecario bibliotecario) {
                ps.setNull(5, Types.INTEGER);
                ps.setInt(7, bibliotecario.getUnidadeId());
            } else {
                ps.setNull(5, Types.INTEGER);
                ps.setNull(7, Types.INTEGER);
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

    // ✅ Buscar por ID
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

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

    // ✅ Buscar por email
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

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

    // ✅ Listar todos
    public List<Usuario> listarTodos() {
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

    // ✅ Atualizar dados do usuário
    public boolean atualizar(Usuario usuario) {
        String sql = """
                UPDATE usuario SET nome = ?, email = ?, telefone = ?, updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setInt(4, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    // ✅ Atualizar tentativas de login
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

    // ✅ Mapear ResultSet → objeto correto
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
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setBloqueado(rs.getBoolean("bloqueado"));
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
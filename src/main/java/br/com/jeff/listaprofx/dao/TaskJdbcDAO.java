package br.com.jeff.listaprofx.dao;

import br.com.jeff.listaprofx.model.Priority;
import br.com.jeff.listaprofx.model.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskJdbcDAO implements TaskDAO {

    private final String url;

    public TaskJdbcDAO(String dbPath) {
        this.url = "jdbc:sqlite:" + dbPath;
    }

    @Override
    public Task save(Task t) {
        String sql = """
            INSERT INTO tasks(description, date_creation, date_conclusion, priority, done)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, t.getDescription());
            pstmt.setTimestamp(2, Timestamp.valueOf(t.getDateCreation()));
            pstmt.setTimestamp(3, t.getDateConclusion() != null ? Timestamp.valueOf(t.getDateConclusion()) : null);
            pstmt.setString(4, t.getPriority().name());
            pstmt.setInt(5, t.isDone() ? 1 : 0);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir a tarefa, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    t.setId(generatedKeys.getInt(1));
                }
            }

            return t;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar tarefa", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar tarefa", e);
        }
    }

    @Override
    public Optional<Task> update(Task t) {
        String sql = """
            UPDATE tasks
            SET description = ?, date_creation = ?, date_conclusion = ?, priority = ?, done = ?
            WHERE id = ?
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getDescription());
            pstmt.setTimestamp(2, Timestamp.valueOf(t.getDateCreation()));
            pstmt.setTimestamp(3, t.getDateConclusion() != null ? Timestamp.valueOf(t.getDateConclusion()) : null);
            pstmt.setString(4, t.getPriority().name());
            pstmt.setInt(5, t.isDone() ? 1 : 0);
            pstmt.setInt(6, t.getId());

            int affected = pstmt.executeUpdate();
            return affected > 0 ? Optional.of(t) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar tarefa", e);
        }
    }

    @Override
    public List<Task> listAll() {
        String sql = "SELECT id, description, date_creation, date_conclusion, priority, done FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setDescription(rs.getString("description"));

                Timestamp tsCreation = rs.getTimestamp("date_creation");
                t.setDateCreation(tsCreation.toLocalDateTime());

                Timestamp tsConclusion = rs.getTimestamp("date_conclusion");
                t.setDateConclusion(tsConclusion != null ? tsConclusion.toLocalDateTime() : null);

                t.setPriority(Priority.valueOf(rs.getString("priority")));
                t.setDone(rs.getInt("done") == 1);

                tasks.add(t);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas", e);
        }

        return tasks;
    }
}

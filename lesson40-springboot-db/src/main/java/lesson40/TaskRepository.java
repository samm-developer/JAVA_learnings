package lesson40;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<Task> TASK_MAPPER = (rs, rowNum) ->
            new Task(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("done") == 1
            );

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        initSchema();
    }

    private void initSchema() {
        jdbc.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  title TEXT NOT NULL,
                  done INTEGER NOT NULL
                )
                """);

        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM tasks", Integer.class);
        if (count != null && count == 0) {
            jdbc.update("INSERT INTO tasks(title, done) VALUES(?, ?)", "Learn Spring + DB", 0);
            jdbc.update("INSERT INTO tasks(title, done) VALUES(?, ?)", "Replace ArrayList with JDBC", 1);
        }
    }

    public List<Task> findAll() {
        return jdbc.query("SELECT id, title, done FROM tasks ORDER BY id", TASK_MAPPER);
    }

    public List<Task> findDone() {
        return jdbc.query("SELECT id, title, done FROM tasks WHERE done = 1 ORDER BY id", TASK_MAPPER);
    }

    public Optional<Task> findById(int id) {
        List<Task> list = jdbc.query(
                "SELECT id, title, done FROM tasks WHERE id = ?",
                TASK_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    public Task insert(String title, boolean done) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO tasks(title, done) VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, title);
            ps.setInt(2, done ? 1 : 0);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        int id = key != null ? key.intValue() : -1;
        return new Task(id, title, done);
    }

    public boolean update(int id, String title, boolean done) {
        int rows = jdbc.update(
                "UPDATE tasks SET title = ?, done = ? WHERE id = ?",
                title,
                done ? 1 : 0,
                id
        );
        return rows > 0;
    }

    public boolean delete(int id) {
        int rows = jdbc.update("DELETE FROM tasks WHERE id = ?", id);
        return rows > 0;
    }
}

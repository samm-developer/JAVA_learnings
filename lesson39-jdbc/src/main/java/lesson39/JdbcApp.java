package lesson39;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Lesson 39: JDBC basics (Java ↔ database)
 *
 * From lesson39-jdbc:
 *   mvn -q compile exec:java
 */
public class JdbcApp {
    // SQLite file will be created next to where you run the command
    private static final String URL = "jdbc:sqlite:tasks.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            System.out.println("Connected to SQLite: tasks.db");

            createTable(conn);
            clearOldData(conn); // so re-runs stay clean for learning
            insertTask(conn, "Learn JDBC", false);
            insertTask(conn, "Connect Spring to DB", false);
            insertTask(conn, "Practice SQL", true);

            System.out.println("--- all tasks ---");
            listTasks(conn);

            System.out.println("--- mark id=1 done ---");
            markDone(conn, 1);
            listTasks(conn);

            System.out.println("--- only done ---");
            listDone(conn);

        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void createTable(Connection conn) throws Exception {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  title TEXT NOT NULL,
                  done INTEGER NOT NULL
                )
                """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
        System.out.println("Table ready: tasks");
    }

    static void clearOldData(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DELETE FROM tasks");
        }
    }

    static void insertTask(Connection conn, String title, boolean done) throws Exception {
        // ? placeholders prevent SQL injection
        String sql = "INSERT INTO tasks(title, done) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, done ? 1 : 0); // SQLite has no boolean — use 0/1
            ps.executeUpdate();
        }
        System.out.println("Inserted: " + title);
    }

    static void listTasks(Connection conn) throws Exception {
        String sql = "SELECT id, title, done FROM tasks ORDER BY id";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean done = rs.getInt("done") == 1;
                System.out.println(id + " | " + title + " | done=" + done);
            }
        }
    }

    static void markDone(Connection conn, int id) throws Exception {
        String sql = "UPDATE tasks SET done = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int updated = ps.executeUpdate();
            System.out.println("Updated rows: " + updated);
        }
    }

    static void listDone(Connection conn) throws Exception {
        String sql = "SELECT id, title FROM tasks WHERE done = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " + rs.getString("title"));
            }
        }
    }
}

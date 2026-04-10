import java.sql.*;
import java.util.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:studyplanner.db";

    public static void createTable() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "deadline TEXT NOT NULL," +
                "priority INTEGER NOT NULL," +
                "isCompleted INTEGER DEFAULT 0)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertTask(String title, String deadline, int priority) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO tasks(title,deadline,priority,isCompleted) VALUES(?,?,?,0)")) {
            ps.setString(1, title);
            ps.setString(2, deadline);
            ps.setInt(3, priority);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteTask(int id) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM tasks WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void markCompleted(int id, boolean completed) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE tasks SET isCompleted=? WHERE id=?")) {
            ps.setInt(1, completed ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static List<String[]> getAllTasks() {
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT id,title,deadline,priority,isCompleted FROM tasks ORDER BY priority ASC")) {
            while (rs.next())
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("title"),
                    rs.getString("deadline"),
                    String.valueOf(rs.getInt("priority")),
                    rs.getInt("isCompleted") == 1 ? "DONE" : "PENDING"
                });
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
package repository;

import model.Expense;
import model.Category;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JdbcExpenseRepository implements ExpenseRepository {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<Expense> findAll() {
        List<Expense> transactions = new ArrayList<>();
        String sql = "SELECT id, description, amount, category, date FROM expenses ORDER BY id";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int amt = rs.getInt("amount");
                String des = rs.getString("description");
                Category category = Category.valueOf(rs.getString("category"));
                LocalDate day = rs.getDate("date").toLocalDate();
                String dateStr = day.format(formatter);

                transactions.add(new Expense(id, amt, des, category, dateStr));
            }
        } catch (SQLException e) {
            System.out.println("Error reading from database: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public void saveAll(List<Expense> transaction) {
        String deleteSql = "DELETE FROM expenses";
        String insertSql = "INSERT INTO expenses (id, description, amount, category, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSql);
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (Expense expense : transaction) {
                    ps.setInt(1, expense.getID());
                    ps.setString(2, expense.getDes());
                    ps.setInt(3, expense.getAmt());
                    ps.setString(4, expense.getCategory().name());
                    ps.setDate(5, Date.valueOf(expense.getDay()));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }
}
package repository;

import model.Expense;
import java.util.List;

public interface ExpenseRepository {
    List<Expense> findAll();
    void saveAll(List<Expense> transaction);
}
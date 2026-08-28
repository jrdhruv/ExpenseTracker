package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Expense;
public class ExpenseFilter {
    
    public List<Expense> filter(List<Expense> transaction, Category category, LocalDate date , Integer month , Integer year){
        
        List<Expense> result = new ArrayList<>();
            
        for(Expense e : transaction){
            if (category != null && e.getCategory() != category) continue;
            if (date != null && !e.getDay().equals(date)) continue;
            if (month != null && e.getDay().getMonthValue() != month) continue;
            if (year != null && e.getDay().getYear() != year) continue;

            result.add(e);
        }

        return result;
    }
}

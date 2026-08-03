package service;

import model.Expense;
import model.Category;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
public class ExpenseService {
   static Expense ob = new Expense();

     public  void addExpense(List<Expense> transaction,int amt, String des, Category category){
        Expense expense = new Expense(amt,des,category);
        transaction.add(expense);
    }


     public void deleteExpense(List<Expense> transaction,int id ){
        boolean removed = false;
            for(int i=0;i<transaction.size();i++){
                if(transaction.get(i).getID()== id ){
                   transaction.remove(i);
                   removed =true;
                   break;
                }
            }
            if (!removed) {
             throw new NoSuchElementException("Expense with ID " + id + " not found");
            }
    }



     public void updateExpense(List<Expense> transaction, int upd, Integer amt , String des , Category category){
              Expense existing = null;

             for (Expense e : transaction) {
                    if (e.getID() == upd) {
                        existing = e;
                        break;
                    }
                }

                if (existing == null) {
                    throw new NoSuchElementException("Expense with ID " + upd + " not found");
                }

                if (amt != null) existing.setAmt(amt);
                if (des != null) existing.setDes(des);
                if (category != null) existing.setCategory(category);
     }


    public Map<Category,Double> getSummary(List<Expense> transaction){
          Map<Category , Double> summary = new HashMap<>();

          for(Expense e : transaction){
            summary.merge(e.getCategory(), (double) e.getAmt(), Double::sum);
          }
        return summary;
    }

    public  void reset(List<Expense> transaction){
             transaction.clear();
             Expense.resetCounter();
    }
    
}

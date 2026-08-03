package cli;

import model.Expense;
import model.Category;
import service.ExpenseService;
import repository.ExpenseRepository;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;

 class ExpenseCli{

   public static void main(String[] args){
       
       ExpenseRepository repository = new ExpenseRepository();
       ExpenseService service = new ExpenseService();
       ArrayList <Expense> transaction = new ArrayList<>(repository.findAll());

   
     BufferedReader br = new BufferedReader(new InputStreamReader(System.in,Charset.defaultCharset()));
     String line = null;
     try {
        line = br.readLine();
      } catch (IOException e) {
        System.out.println("Problem : "+e);
        e.printStackTrace();
      }
       String parts[]= line.split(" ");
        switch (parts[0]) {

          case "add": 
          handleAdd(service, transaction, parts);
          break;
          case  "delete": 
          handleDelete(service, transaction, parts);
          break;
          case  "update": 
          handleUpdate(service, transaction, parts);
          break;
          case  "summary":
          handleSummary(service,transaction);
          break;
          case  "list":
          handleList(transaction);
          break;
          case  "reset":
          handleReset(service, transaction);
          break;

          default: System.out.println("Error Try Again"); break;
        }
       repository.saveAll(transaction);
    }
   

   private static void handleAdd(ExpenseService service, ArrayList<Expense> transaction, String[] parts) {
        int amt = 0;
        String des = "null";
        Category category = Category.None;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("--amount") && i + 1 < parts.length)
                amt = Integer.parseInt(parts[i + 1]);
            if (parts[i].equalsIgnoreCase("--description") && i + 1 < parts.length)
                des = parts[i + 1];
            if (parts[i].equalsIgnoreCase("--category") && i + 1 < parts.length)
                category = Category.valueOf(parts[i + 1]);
        }

        try {
            service.addExpense(transaction, amt, des, category);
            System.out.println("Expenses added successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
   }


   private static void handleDelete(ExpenseService service , ArrayList<Expense> transaction,String[] parts){
       int _del_=-1;
        for(int i=0;i<parts.length;i++){
             if(parts[i].equalsIgnoreCase("--id") && i + 1 < parts.length)
             _del_ = Integer.parseInt(parts[i+1]);
            }

        try{
          service.deleteExpense(transaction, _del_);
          System.out.println("Expense deleted successfully");
        }catch (NoSuchElementException e){
          System.out.println("Error: "+e.getMessage());
        }
   }


   private static void handleUpdate(ExpenseService service, ArrayList<Expense> transaction, String[] parts){
          int upd = -1;
          Integer amt = null;
          String des = null;
          Category category = null;

            for(int i=0;i<parts.length;i++){
                if(parts[i].equalsIgnoreCase("--id") && i + 1 < parts.length)
                      upd = Integer.parseInt(parts[i+1]);
                if(parts[i].equalsIgnoreCase("--amount") && i + 1 < parts.length)
                      amt = Integer.parseInt(parts[i+1]);
                if(parts[i].equalsIgnoreCase("--description") && i + 1 < parts.length)
                      des = parts[i+1];
                if(parts[i].equalsIgnoreCase("--category") && i + 1 < parts.length)
                      category =  Category.valueOf(parts[i+1]);
            }

            try {
              service.updateExpense(transaction, upd, amt, des, category);
              System.out.println("Expenses updated successfully");
                } catch (NoSuchElementException e) {
              System.out.println("Error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
              System.out.println("Error: " + e.getMessage());
            }
        }
   

   private static void handleSummary(ExpenseService service, ArrayList<Expense> transaction){
      Map<Category, Double> summary = service.getSummary(transaction);

          if (summary.isEmpty()) 
            System.out.println("No expenses recorded.");
         else {
            System.out.println("Expense Summary by Category:");
            double grandTotal = 0.0;

            for (Map.Entry<Category, Double> entry : summary.entrySet()) {
                System.out.printf("%-15s $%.2f%n", entry.getKey(), entry.getValue());
                grandTotal += entry.getValue();
            }

            System.out.printf("%-15s $%.2f%n", "Total:", grandTotal);
        }
    }


   private static void handleList(ArrayList<Expense> transaction){
             System.out.println("ID\tDate\t\tDescription\tCategory\tAmount");
             for(Expense ob: transaction)
             System.out.println(ob.getID()+"\t"+ob.getDate()+"\t"+ob.getDes()+"\t\t"+ob.getCategory()+"\t\t"+ob.getAmt());
    }


   private static void handleReset(ExpenseService service , ArrayList<Expense> transaction){
             service.reset(transaction);
             System.out.println("Reset Done");
    }
        

}



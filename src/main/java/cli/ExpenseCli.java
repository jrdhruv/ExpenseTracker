package cli;

import model.Expense;
import model.Category;
import service.ExpenseFilter;
import service.ExpenseService;
import repository.CsvExpenseRepository;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

 class ExpenseCli{

   public static void main(String[] args){
       
       CsvExpenseRepository repository = new CsvExpenseRepository();
       ExpenseService service = new ExpenseService(); 
       ExpenseFilter filter = new ExpenseFilter();
       ArrayList <Expense> transaction = new ArrayList<>(repository.findAll());

   
     BufferedReader br = new BufferedReader(new InputStreamReader(System.in,Charset.defaultCharset()));

     printHelp();

    while(true){
      System.out.print("\n> ");
      String line = null;
      try {
          line = br.readLine();
        } catch (IOException e) {
          System.out.println("Problem : "+e);
          e.printStackTrace();
        }

        if (line == null) break; // EOF (e.g. Ctrl+D)

          line = line.trim();
        if (line.isEmpty()) continue;

          String[] parts = line.split(" ");

        if (parts[0].equalsIgnoreCase("exit") || parts[0].equalsIgnoreCase("quit")) {
                  repository.saveAll(transaction);
                  System.out.println("Saved. Goodbye!");
                  break;
        }
        
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
          case  "filter":
          handleFilter(filter,transaction,parts);
          break;
          case  "list":
          handleList(transaction);
          break;
          case  "reset":
          handleReset(service, transaction);
          break;

          default: System.out.println("Error Try Again"); break;
        }
      }
    }
   


  private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  add --amount <n> --description <text> --category <cat>");
        System.out.println("  delete --id <n>");
        System.out.println("  update --id <n> [--amount <n>] [--description <text>] [--category <cat>]");
        System.out.println("  list");
        System.out.println("  summary");
        System.out.println("  filter [--category <cat>] [--date dd/MM/yyyy] [--month <n>] [--year <n>]");
        System.out.println("  reset");
        System.out.println("  exit");
    }

  
   private static void handleAdd(ExpenseService service, ArrayList<Expense> transaction, String[] parts) {
        Integer amt = null;
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
     
        if(amt == null){
          System.out.println("Error: --amount is required");
          return; 
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

    private static void handleFilter(ExpenseFilter filter, ArrayList<Expense> transaction , String[] parts){
             Category category = null;
             LocalDate date = null;
             Integer month = null;
             Integer year = null;
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

             for(int i =0 ;i<parts.length;i++){
              if(parts[i].equalsIgnoreCase("--category") && i + 1 < parts.length)
                category = Category.valueOf(parts[i+1]);
              if(parts[i].equalsIgnoreCase("--date") && i + 1 < parts.length)
                date = LocalDate.parse(parts[i+1],formatter);
              if(parts[i].equalsIgnoreCase("--month") && i + 1 < parts.length)
                month = Integer.parseInt(parts[i+1]);
              if(parts[i].equalsIgnoreCase("--year") && i + 1 < parts.length)
                year = Integer.parseInt(parts[i+1]);
             }

             try{
              List<Expense> filtered = filter.filter(transaction, category,date, month, year);
                  if (filtered.isEmpty()) 
                    System.out.println("No matching expenses found.");
                  else {
                    System.out.println("ID\tDate\t\tDescription\tCategory\tAmount");
                    for (Expense e : filtered) 
                        System.out.println(e.getID() + "\t" + e.getDate() + "\t" + e.getDes() + "\t\t" + e.getCategory() + "\t\t" + e.getAmt());          
                    }
             }catch(NoSuchElementException e){
              System.out.println("Error: "+e.getMessage());
             }catch(IllegalArgumentException e){
              System.out.println("Error: "+e.getMessage());
             }catch (java.time.format.DateTimeParseException e) {
              System.out.println("Error: Invalid date format, expected dd/MM/yyyy");
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



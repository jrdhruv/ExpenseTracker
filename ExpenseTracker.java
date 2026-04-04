import java.util.*;
import java.io.*;
import java.time.LocalDate;
 class ExpenseTracker {
    static ID ob = new ID();

   public static void main(String[] args){
  
       ArrayList <ID> transaction = new ArrayList<>();


       loadFromCSV(transaction);

       
     BufferedReader br = new BufferedReader(new InputStreamReader(System.in,System.console().charset()));
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
             addExpense(transaction, parts);
             break;

          case  "delete":
             deleteExpense(transaction, parts);
             break;

          case  "update":
             updateExpense(transaction, parts);
             break;

          case  "summary":
             getSummary(transaction);
             break;

          case  "list":
             getList(transaction);
             break;

          case  "reset":
             reset(transaction);
             break;
            
          default:
            System.out.println("Error Try Again");
            break;

        }
       saveToCSV(transaction);
    }
   





    // Method to load data from CSV
    private static void loadFromCSV(ArrayList<ID> transaction) {
        try (BufferedReader br = new BufferedReader(new FileReader("expenses.csv"))) {
            String line;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
               // Skip header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                     ob.setID(Integer.parseInt(parts[0]));
                     ob.setDate(parts[1]);
                     ob.setDes(parts[2]);
                     ob.setCategory(Category.valueOf(parts[3]));
                     ob.setAmt( Integer.parseInt(parts[4]));
                    ID expense = new ID(ob.getID(),ob.getAmt(),ob.getDes(),ob.getCategory(),ob.getDate());
                    transaction.add(expense);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing expense file found. Creating new one.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }




    

    // Method to save data to CSV
    private static void saveToCSV(ArrayList<ID> transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("expenses.csv"))) {
    // Write header
            bw.write("ID,Date,Description,Category,Amount");
            bw.newLine();
            
            for (ID expense : transaction) {
                // Handle descriptions with commas by wrapping in quotes
                String description = expense.getDes().contains(",") ? 
                    "\"" + expense.getDes() + "\"" : expense.getDes();
                    
                bw.write(expense.getID() + "," + expense.getDate() + "," + 
                         description + "," + expense.getCategory()+","+expense.getAmt());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }









    private static  void addExpense(ArrayList<ID> transaction,String parts[]){
        for(int i=0;i<parts.length;i++){
             if(parts[i].equalsIgnoreCase("--amount"))
             ob.setAmt(Integer.parseInt(parts[i+1]));
             if(parts[i].equalsIgnoreCase("--description"))
             ob.setDes(parts[i+1]);
             if(parts[i].equalsIgnoreCase("--category"))
             ob.setCategory(Category.valueOf(parts[i+1]));
            }

             transaction.add(new ID(ob.getAmt(),ob.getDes(),ob.getCategory()));

             System.out.println("Expenses added successfully");
    }







    private static void deleteExpense(ArrayList<ID> transaction,String parts[]){
        int _del_=-1;
        for(int i=0;i<parts.length;i++){
             if(parts[i].equalsIgnoreCase("--id"))
             _del_ = Integer.parseInt(parts[i+1]);
            }
              boolean removed = false;
              for(int i=0;i<transaction.size();i++){
                if(transaction.get(i).getID()==_del_){
                   transaction.remove(i);
                   removed =true;
                   break;
                }
            }
                if (removed)
                    System.out.println("Expense deleted successfully");
                else
                    System.out.println("Error: ID not found");
    }







    private static void updateExpense(ArrayList<ID> transaction,String parts[]){
        int _upd_= -1;
        boolean _amt_ = false,_des_ =false , _cat_=false;
          for(int i=0;i<parts.length;i++){
             if(parts[i].equalsIgnoreCase("--id"))
             _upd_=Integer.parseInt(parts[i+1]);
             if(parts[i].equalsIgnoreCase("--amount")){
              ob.setAmt(Integer.parseInt(parts[i+1]));
              _amt_=true;
             }
             if(parts[i].equalsIgnoreCase("--description")){
              ob.setDes(parts[i+1]);
              _des_=true;
             }
             if(parts[i].equalsIgnoreCase("--category")){
              ob.setCategory(Category.valueOf(parts[i+1]));
              _cat_=true;
            }
        }

              boolean updated = false;
                 for (int j = 0; j < transaction.size(); j++) {        
                   if (transaction.get(j).getID() == _upd_) {
                     ID existing = transaction.get(j);            
                     if (_amt_) existing.setAmt(ob.getAmt());
                     if (_des_) existing.setDes(ob.getDes());
                     if (_cat_) existing.setCategory(ob.getCategory());
                     updated =true;
                     break;
                  }
                  
                 }
             if (updated)
                  System.out.println("Expenses updated successfully");
             else
                  System.out.println("Error: ID not found");
     }






    private static void getSummary(ArrayList<ID> transaction){
            int _sum_= 0;
             for(ID ob: transaction)
             _sum_+= ob.getAmt();
             System.out.println("Total expenses : "+_sum_);
    }





    private static void getList(ArrayList<ID> transaction){
             System.out.println("ID\tDate\t\tDescription\tCategory\tAmount");
             for(ID ob: transaction)
             System.out.println(ob.getID()+"\t"+ob.getDate()+"\t"+ob.getDes()+"\t\t"+ob.getCategory()+"\t\t"+ob.getAmt());
    }






    private static void reset(ArrayList<ID> transaction){
             transaction.clear();
             ID.idCounter = 0;
             System.out.println("Reset Done");
    }
    
 }

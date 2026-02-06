import java.util.*;
import java.io.*;
import java.time.LocalDate;
 class ExpenseTracker {
   public static void main(String[] args){
       int  _amt_=0;
       String _des_="";
       int _del_=0;
       int  _upd_=0;
       int _sum_=0;
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
       String parts[]=line.split(" ");




        switch (parts[0]) {

          case "add":
             addExpense(transaction, parts, _amt_, _des_);
             break;

          case  "delete":
             deleteExpense(transaction, _del_, parts);
             break;

          case  "update":
             updateExpense(transaction, _upd_, parts, _amt_, _des_);
             break;

          case  "summary":
             getSummary(transaction,_sum_);
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
                if (parts.length >= 4) {
                    int fileId = Integer.parseInt(parts[0]);
                    String date = parts[1];
                    String des = parts[2];
                    int amt = Integer.parseInt(parts[3]);
                    
                    ID expense = new ID(fileId, amt, des, date);
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
            bw.write("ID,Date,Description,Amount");
            bw.newLine();
            
            for (ID expense : transaction) {
                // Handle descriptions with commas by wrapping in quotes
                String description = expense.getDes().contains(",") ? 
                    "\"" + expense.getDes() + "\"" : expense.getDes();
                    
                bw.write(expense.getID() + "," + expense.getDate() + "," + 
                         description + "," + expense.getAmt());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }





    private static void addExpense(ArrayList<ID> transaction,String parts[],int _amt_,String _des_){
         for(int i=0;i<parts.length;i++){
             if(parts[i].equalsIgnoreCase("--amount"))
             _amt_= Integer.parseInt(parts[i+1]);
             if(parts[i].equalsIgnoreCase("--description"))
             _des_ = parts[i+1];
            }
             transaction.add(new ID(_amt_,_des_));
             System.out.println("Expenses added successfully");
    }

    private static void deleteExpense(ArrayList<ID> transaction,int _del_,String parts[]){
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

    private static void updateExpense(ArrayList<ID> transaction,int _upd_,String parts[],int _amt_,String _des_){
          for(int i=0;i<parts.length;i++){
             if(parts[i].equalsIgnoreCase("--id"))
             _upd_=Integer.parseInt(parts[i+1]);
             if(parts[i].equalsIgnoreCase("--amount"))
             _amt_= Integer.parseInt(parts[i+1]);
             if(parts[i].equalsIgnoreCase("--description"))
             _des_ = parts[i+1];
            }
              boolean updated = false;
                 for (int i = 0; i < transaction.size(); i++) {
                     if (transaction.get(i).getID() == _upd_) {
                        transaction.set(i, new ID(_upd_, _amt_, _des_, LocalDate.now().toString()));
                        updated = true;
                        break;
                       }
                     }
             if (updated)
                  System.out.println("Expenses updated successfully");
             else
                  System.out.println("Error: ID not found");
    }

    private static void getSummary(ArrayList<ID> transaction,int _sum_){
             for(ID ob: transaction)
             _sum_+= ob.getAmt();
             System.out.println("Total expenses : "+_sum_);
    }

    private static void getList(ArrayList<ID> transaction){
             System.out.println("ID\tDate\t\tDescription\tAmount");
             for(ID ob: transaction)
             System.out.println(ob.getID()+"\t"+ob.getDate()+"\t"+ob.getDes()+"\t\t"+ob.getAmt());
    }

    private static void reset(ArrayList<ID> transaction){
             transaction.clear();
             ID.idCounter = 0;
             System.out.println("Reset Done");
    }
   
}

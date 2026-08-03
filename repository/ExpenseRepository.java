package repository;

import model.Expense;
import model.Category;
import java.util.*;
import java.io.*;

public class ExpenseRepository{
    
    private static final String FILE_PATH = "expenses.csv";

    public List<Expense> findAll() {
        List<Expense> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))){
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
                    Expense expense = new Expense(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[4]),
                        parts[2],
                        Category.valueOf(parts[3]),
                        parts[1]
                    );
                    transactions.add(expense);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing expense file found. Creating new one.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }


    public void saveAll(List<Expense> transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))){
    // Write header
            bw.write("ID,Date,Description,Category,Amount");
            bw.newLine();
            
            for (Expense expense : transaction) {
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
    
}

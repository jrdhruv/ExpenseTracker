
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

enum Category{
   None,Food,Travel,Laundary
}
 public class ID {
   static int idCounter =0;
   private  int  amt;
   private String des;
   private int id;
   private LocalDate day;
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   private String date;
   private Category category;

    ID(){
        this.id=0;
        this.amt=0;
        this.des="null";
        this.day= LocalDate.now();
        this.date=day.format(formatter);
        this.category=Category.None;
       }

    ID(int amt){
        idCounter++;
        this.id=idCounter;
        this.amt=amt;
        this.des="null";
        this.day= LocalDate.now();
        this.date=day.format(formatter);
        this.category=Category.None;
       }

    ID(int amt,String des){
        idCounter++;
        this.id=idCounter;
        this.amt=amt;
        this.des=des;
        this.day= LocalDate.now();
        this.date=day.format(formatter);
        this.category=Category.None;
       }

    ID(int amt,Category category){
        idCounter++;
        this.id=idCounter;
        this.amt=amt;
        this.des="null";
        this.day= LocalDate.now();
        this.date=day.format(formatter);
        this.category=category;
       }

    ID(int amt,String des,Category category){
      idCounter++;
      this.id=idCounter;
      this.amt=amt;
      this.des=des;
      this.day= LocalDate.now();
      this.date=day.format(formatter);
      this.category=category;
     }
    ID(int id, int amt, String des,Category category, String date) {
    this.id = id;
    this.amt = amt;
    this.des = des;
    this.date = date;
    this.category=category;
    
    if (id > idCounter) 
        idCounter = id;  
    }

  


    public int getAmt(){
       return amt;
    }
    public String getDes(){
      return des;
    }
    public int getID(){
      return id;
    }
    public LocalDate getDay(){
      return day;
    }
    public String getDate(){
      return date;
    }
    public Category getCategory(){
      return category;
    }



    public int setAmt(int amt){
      if(amt<0)
         throw new IllegalArgumentException("Amount cannot be negative");
     return this.amt=amt;
    }
    public String setDes(String des){
      if(des == null || des.trim().isEmpty())
        return this.des="null";
     return this.des=des;
    }
    public int setID(int id){
      if(id<=0)
         throw new IllegalArgumentException("ID cannot be negative");
      return this.id=id;
    }
    public LocalDate setDay(LocalDate day){
      return this.day=day;
    }
    public String setDate(String date){
     return this.date=date;
    }
    public Category setCategory(Category category){
      if(category==null)
        return this.category=Category.None;
      return this.category=category;
    }

    
  }

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
 public class ID {
   static int idCounter =0;
   private  int  amt;
   private String des;
   private int id;
   private LocalDate day;
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   private String date;

    ID( int  amt,String des){
        idCounter++;
        this.id=idCounter;
        this.amt=amt;
        this.des=des;
        this.day= LocalDate.now();
        this.date=day.format(formatter);
      }

      
    ID(int id, int amt, String des, String date) {
    this.id = id;
    this.amt = amt;
    this.des = des;
    this.date = date;
    
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

    public void setAmt(int amt){
      if(amt<=0)
         throw new IllegalArgumentException("Amount cannot be negative");
      this.amt=amt;
    }
    public void getDes(String des){
      if(des == null || des.trim().isEmpty())
        System.out.println("Description cannot be empty");
      this.des=des;
    }
    public void setID(int id){
      if(id<=0)
         throw new IllegalArgumentException("ID cannot be negative");
      this.id=id;
    }
    public void setDay(LocalDate day){
      this.day=day;
    }
    public void setDate(String date){
      this.date=date;
    }
  }
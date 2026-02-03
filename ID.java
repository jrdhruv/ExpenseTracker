
import java.time.LocalDate;
 public class ID {
  static int idCounter =0;
    int  amt;
    String des;
    int id;
    LocalDate day;


    ID( int  amt,String des){
        idCounter++;
        this.id=idCounter;
        this.amt=amt;
        this.des=des;
        this.day= LocalDate.now();
      }

      
    ID(int id, int amt, String des, String day) {
    this.id = id;
    this.amt = amt;
    this.des = des;
    this.day = LocalDate.parse(day);
    
    if (id > idCounter) 
        idCounter = id;  
  
    }
  }

 

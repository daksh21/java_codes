import java.util.concurrent.*;

public class Sleeping_barber extends Thread {

    /**
 *
 * @author daksh
 */
    Semaphore customers = new Semaphore(0); //customer semaphone
    Semaphore barber = new Semaphore(0); //barber semaphore
    Semaphore mutex = new Semaphore(1); //mutex 

 int CHAIRS = 5;
 int numberOfFreeSeats = CHAIRS;

 
class Customer extends Thread {   //class customer
  int id;
  boolean notCut=true;

  public Customer(int i) {   //function customer
    id = i;
  }

  public void run() {   
    /*method run() relating to customer. 
      this method shows the customer entry, 
      which customer sat down and whether there are any free
      seats left or not for next customer.
      */     
    while (notCut) {  
      try {
      mutex.acquire();  
      if (numberOfFreeSeats > 0) {  
        System.out.println("Customer " + this.id + " just sat down.");
        numberOfFreeSeats--;  
        customers.release();  
        mutex.release();   
        try {
	barber.acquire();  
        notCut = false;  
        this.get_haircut(); 
        } catch (InterruptedException ex) {}
      }   
      else  {  
        System.out.println("There are no free seats. Customer " + this.id + " has left the barbershop.");
        mutex.release();  
        notCut=false; 
      }
     }
      catch (InterruptedException ex) {}
    }
  }

   public void get_haircut(){
       //shows the customer number that is having the hair cut currently.
    System.out.println("Customer " + this.id + " is getting his hair cut");
    try {
    sleep(5050);
    } catch (InterruptedException ex) {}
  }

}

 
class Barber extends Thread {
  
  public Barber() {}
  // this method shows the barber mutex which 
  // forms a mutex relation for who is currently 
  // being served, are there any free seats 
  // and further does the work of releasing all the locks.
  public void run() {
    while(true) {  
      try {
      customers.acquire(); 
      mutex.release(); 
        numberOfFreeSeats++; 
      barber.release(); 
      mutex.release();
      this.cutHair();  
      } catch (InterruptedException ex) {}
    }
  }

  public void cutHair(){
    System.out.println("The barber is cutting hair");
    try {
      sleep(5000);
    } catch (InterruptedException ex){ }
  }
}       
  
//main method
  public static void main(String args[]) {
    
    Sleeping_barber sb = new Sleeping_barber(); // creating new instance of barber  
    sb.start();  
  }

  public void run(){   
   Barber b1 = new Barber();  
   b1.start();  

   for (int i=1; i<16; i++) {
     Customer c1 = new Customer(i);
     c1.start();
     try {
       sleep(2000);
     } catch(InterruptedException ex) {};
   }
  } 
}
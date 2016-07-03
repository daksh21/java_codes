/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tams;

import java.io.*;
import java.sql.*;

/**
 *
 * @author daksh
 */
    /**
     * @param args the command line arguments
     */

        // TODO code application logic here

class GetDbConnection { //class to connct program with the database

    Connection con = null;
    Connection getconnection() { //function makes the database connection
        try {
            String dbURL = "jdbc:mysql://localhost/tams";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, "root", ""); 
            //has dbURL as 1st parameter, username as 2nd and password as third. 
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }
}

class login
{
 int verify_cust() throws IOException { 
//function to verify the customer using
     // his username and passowrd as input.
     //if entry matches with the entry in database, then login is successful.
     
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username;
        String password;
        System.out.println("Username: ");
        username = br.readLine();
        System.out.println("Password: ");
        password = br.readLine();
        ResultSet rs;
        Statement stmt;
        GetDbConnection dbCon = new GetDbConnection();
        Connection con = dbCon.getconnection();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("select password from register where user_id = '" + username + "'");

            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    System.out.println("Login successful");
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }
}    

class registeration //class registration
{
    // private Object con;
    void register() throws SQLException, IOException{
        
// function register takes as input the customer name, 
        //email, address, user-id, phone and password. 
        //after taking all this input, it adds an entry to the 
        //database with the user inserted entries. 
        
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String name;
            String em;
            String address;
            String uid;
            String phone;
            String pass;
            System.out.println("Name: ");
            name = br.readLine();
            System.out.println("Email: ");
            em = br.readLine();
            System.out.println("Address: ");
            address = br.readLine();
            System.out.println("User-ID: ");
            uid = br.readLine();
            System.out.println("Phone: ");
            phone=br.readLine();
            System.out.println("Password:");
            pass=br.readLine();
            
            GetDbConnection dbCon = new GetDbConnection();
            Connection con = dbCon.getconnection();
            PreparedStatement ps= con.prepareStatement("insert into register (name,EMAIL,address,phone,user_id,password) values(?,?,?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, em);
            ps.setString(3, address);
            ps.setString(4, phone);
            ps.setString(5, uid);
            ps.setString(6, pass);
            ps.executeUpdate();
            System.out.println("Registration Successful!!"); 
//returns registeration successful if all entries are successfully written to db.
        }
}

class taxi_booking extends login
{
    // private Object con;
    void validate() throws IOException { 
//checks for the validity of customer 
// using his username and password.
        int i = 0;
        while (i != 1) {
            i = verify_cust();
        }
    }
    int book() throws SQLException, IOException{
    //booking function is used to book a taxi. 
        // it takes as input the required details and if all details are
        //successfully entered, returns the user a taxi number
        //which is the booked taxi number and also the cost
        //which is to be paid by the customer to the driver.
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String user_id;
            String password;
            String date;
            int cab_size;
            String source;
            String dest;
            int dist;
            int cost;
            System.out.println("User-id: ");
            user_id = br.readLine();
            System.out.println("Password: ");
            password = br.readLine();
            System.out.println("Date: ");
            date = br.readLine();
            System.out.println("Enter source station: ");
            source = br.readLine();
            System.out.println("Enter destination: ");
            dest = br.readLine();
            System.out.println("Cab(2,4,6,8,9,10,12,16,42,56 are available): ");
            cab_size = Integer.parseInt(br.readLine());
            System.out.println("Enter distance:");
            dist=Integer.parseInt(br.readLine());
            cost=150+(dist*5);
            
            GetDbConnection dbCon = new GetDbConnection();
            Connection con = dbCon.getconnection();
           
            PreparedStatement ps= con.prepareStatement("update booking set date=?, source=?,dest=?, id=?,status=? where cab_size=?");
            ps.setString(1, date);
            //ps.setInt(2, cab_size);
            ps.setString(2, source);
            ps.setString(3, dest);
            ps.setString(4, user_id);
            ps.setString(5, "BOOKED"); 
//if booking is successful, it writes the entry booked to the status column.
            ps.setInt(6, cab_size);
            ps.executeUpdate();
           //ps.executeQuery();
           ResultSet rs=null;
           Statement stmt = con.createStatement();
            rs = stmt.executeQuery("select cab_no from booking where id= '" + user_id +"'");
            if(rs.next())
            System.out.println("Booking Successful!! Taxi number is :"+rs.getInt("cab_no")+ ".Please pay Rs. "+cost+" to driver.");
        return 0;
        }
}
class taxi_cancel extends login
{
    //this function is used to cancel the booking. it asks for the taxi number of the booked
    //taxi which is to be cancelled and makes the status of corresponding taxi 
    //as not booked in the table in database.
    void validate() throws IOException {
        int i = 0;
        while (i != 1) {
            i = verify_cust(); //verify the authenticity of user.
        }
    }
    // private Object con;
    int cancel() throws SQLException, IOException{
    
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String user_id;
            String password;
            int cab_no;
            System.out.println("User-id: ");
            user_id = br.readLine();
            System.out.println("Password: ");
            password = br.readLine();
            System.out.println("Cab-number:");
            cab_no=Integer.parseInt(br.readLine());
            
            GetDbConnection dbCon = new GetDbConnection();
            Connection con = dbCon.getconnection();
           
            PreparedStatement ps= con.prepareStatement("update booking set status='NOT_BOOKED' where cab_no=?");
            ps.setInt(1,cab_no);
            ps.executeUpdate();
            System.out.println("Cancellation Successful!!");
        return 0;
        }
}


public class tams {
//main method
    public static void main(String args[]) throws SQLException,IOException{
        int choice;
        registeration r1=new registeration(); //create new instance of registration
        login l1=new login(); //new instance of login.
        taxi_booking t1=new taxi_booking();
        taxi_cancel tc1=new taxi_cancel();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            //creating user friendly menu.
       System.out.println("1. Register");
       //System.out.println("2. Login");
       System.out.println("2. Book");
       System.out.println("3. Cancel");
       System.out.println("4. Exit");
       System.out.println("Enter your choice:");
       choice=Integer.parseInt(br.readLine());
       switch(choice)
       {
       case 1:
       r1.register();
        break;
       
       case 2:
      // l1.verify_cust();
      // break;
       
       //case 3:
        t1.validate();
        t1.book();
       break;
      
       case 3:
        tc1.validate();
        tc1.cancel();
       break;     
       
       default:
       return;
    }
       
    } catch(IOException e){
    System.out.println(e);
    }
  } //end of main method
}
//end of program
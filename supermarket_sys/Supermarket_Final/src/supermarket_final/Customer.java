/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supermarket_final;

import java.io.*;
import java.sql.*;

/**
 *
 * @author daksh
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
class Customer { //class customer

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        /**
         * @param args the command line arguments
         */
        Customer c1 = new Customer(); //new instance of customer class
        boolean login;
        int choice;
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            //creating user friendly menu
            System.out.println("1)Customer Login\n2)Register\n3)Admin Login\n4)Exit");
            choice = Integer.parseInt(obj.readLine());
            switch (choice) {
                case 1:
                    login = c1.login(c1); //checking whether the details are correct.
                    if (login == true) {
                        c1.display(c1); //displays results if login successful
                    }
                    break;
                case 2:
                    Salesperson ob = new Salesperson(); //new instance of salesperson
                    System.out.println("Enter the Username:\n");
                    c1.username = obj.readLine();
                    System.out.println("Enter the Name:\n");
                    c1.name = obj.readLine();
                    System.out.println("Enter Password:\n");
                    c1.pwd = obj.readLine();
                    c1.points = 0;
                    System.out.println("Enter the phone no");
                    c1.phone_no = Integer.parseInt(obj.readLine());
                    ob.register(c1);

                    break;

                case 3:
                    Admin ad = new Admin(); //new instance of admin
                    ad.administrator();

                    break;
                case 4:
                    System.exit(0); //returning null value
            }
        }

    }

    String username, name, pwd;
    int points;
    int phone_no;
   // int max_id = 0;

    protected boolean login(Customer c1) throws IOException, SQLException {
// login takes from he customer his username and password.
        // it matches these details from already present in the 
        //database. If match is found, then login is successful
        //else unsuccessful login.
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sclp", "root", "");
            ResultSet rs;
            Statement stmt;

            System.out.println("Enter the Customer Username:");
            c1.username = obj.readLine();
            System.out.println("Enter the Customer Password:");
            c1.pwd = obj.readLine();

            if (c1.username != null && c1.pwd != null) {
                String usr = c1.username;
                String pswd = c1.pwd;
                stmt = conn.createStatement();
                String CHECK_USER = "SELECT * FROM CUSTOMER WHERE username = '" + usr + "' AND password = '" + pswd + "'";
                rs = stmt.executeQuery(CHECK_USER);
                return rs.next();
            } else {
                System.out.println("Invalid userID or Password");
            }
            //  stmt.close();
            System.out.println("Database updated successfully..."); //data entered into database

        } catch (SQLException | ClassNotFoundException se) {
            System.out.println(se);
        }
          //  stmt.close();
        //conn.close();
        return false;
    }
//function to display the required output on the screen.
    // it is used to enter the values in the database according 
    // to the amount of which purchasing is done.
    // it calculates the total points according the total billing 
    // value and updates those points in the databse. 
    public void display(Customer c) throws SQLException, ClassNotFoundException, IOException {
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        int choice, amount, temp = 0;
        String update, old_points;

        System.out.println("Customer Logged on successfully");
        System.out.println("Press 1 to enter amount for billing:");

        choice = Integer.parseInt(sc.readLine());
        if (choice == 1) {
            System.out.println("Enter amount"); //amount entered by the salesperson
            amount = Integer.parseInt(sc.readLine());
            c.points = amount / 100;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sclp", "root", "");
            ResultSet rs;
            Statement stmt;
            stmt = conn.createStatement();

            old_points = "select points from CUSTOMER where username='" + c.username + "'";
            rs = stmt.executeQuery(old_points);
            while (rs.next()) {
                temp = rs.getInt(1);
            }
            c.points = c.points + temp;
            update = "update CUSTOMER set points ='" + c.points + "'where username='" + c.username + "'";
            stmt.executeUpdate(update);
        }

        System.out.println("Thank you for shopping " + (c.points - (temp)) + " points added to your account");
    }
}

class Salesperson {
//class salesperson
    String username, password, name;

    void register(Customer obj) throws ClassNotFoundException, SQLException {
        try {
            //salesperson does the work of registing the customer. 
            //it verifies the customer record and further 
            //makes the required entry in the database accordingly.
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sclp", "root", "");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO CUSTOMER VALUES (?, ?, ?, ?, ?)");
            ps.toString();
            ps.setString(1, obj.username);
            ps.setString(2, obj.pwd);
            ps.setString(3, obj.name);
            ps.setInt(4, obj.points);
            ps.setInt(5, obj.phone_no);
            ps.executeUpdate();

            System.out.println("Database updated successfully...");
        } catch (SQLException | ClassNotFoundException se) {
            System.out.println(se);
        }
    }
}

class Admin extends Customer {
//admin class
    BufferedReader s = new BufferedReader(new InputStreamReader(System.in));

    public boolean administrator() throws SQLException, IOException, ClassNotFoundException {
//admin is responsible for finding out the winner for a specific price and
        // also he has to make the points as 0 after the prize is distributed 
        //among the customers. also admin can update the record if need be.
        //thus admin is the main head who has complete control on the database.
        Admin a1 = new Admin();
        String user, pass;
        int temp = 0;
        String points1, update;
        System.out.println("Enter admin username and password to redeem points");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sclp", "root", "");

            System.out.println("Enter the Admin Username:");
            user = s.readLine();
            System.out.println("Enter the Admin Password:");
            pass = s.readLine();

            if (user != null && pass != null) {
                Statement stmt = conn.createStatement();

                PreparedStatement ps = conn.prepareStatement("SELECT * FROM ADMIN WHERE username = '" + user + "' AND password = '" + pass + "'");
                ps.executeQuery();

                System.out.println("Admin login successful");
        
                System.out.println("Press 1 to generate winners");
                int x = Integer.parseInt(s.readLine());
                if (x == 1) {
                    points1 = "select * from CUSTOMER where points>=100";
                    ResultSet rs = stmt.executeQuery(points1);
                    while (rs.next()) {
                        System.out.println("Winners are:");
                        System.out.println("Name: " + rs.getString(1));
                        System.out.println("Points: " + rs.getInt(4));
                        System.out.println("Award GOLD coin as gift");
                    }
                    update = "update CUSTOMER set points = 0 where points>=100";
                    stmt.executeUpdate(update);
                } else {
                    System.out.println("Good bye!");
                }
            } else {
                System.out.println("Wrong ID or password");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return false;
    }
}//end of program


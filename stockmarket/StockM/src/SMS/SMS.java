package SMS;

import java.io.*;
import java.sql.*;

// making class getdbconnection to connect required db with the program.
class GetDbConnection {

    Connection con = null;

    Connection getconnection() {
        try {
            String dbURL = "jdbc:mysql://localhost/sharetrading";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, "root", "");
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }
}

//creating new class user
class User {

    int verify() {
        //verify function takes as input username and password
        // and checks the specified entry for its presence
        // in the database. if it exists, then verify returns true 
        // else it returns false.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username;
        String password;
        try {
            System.out.println("Username: ");
            username = br.readLine();
            System.out.println("Password: ");
            password = br.readLine();
            ResultSet rs;
            Statement stmt;
            GetDbConnection dbCon = new GetDbConnection();
            Connection con = dbCon.getconnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery("select password from user where username = '" + username + "'");

            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (IOException | SQLException e) {
            System.out.println(e);
            return 0;
        }
    }
}

class Customer extends User {
//buy function enters the entry of how many shares and at what 
    // price the user wants to purchase the shares. 
    //if the particular value seller or the seller selling at 
    // lower price quoted is found, then the purchase 
    // is successful else unsuccessful purchase.
    void buy() {
        int i = 0;
        while (i != 1) {
            i = verify();  //checks for valid user of the software.
        }

        GetDbConnection dbCon = new GetDbConnection();
        Connection con = dbCon.getconnection();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ResultSet rs;
            Statement stmt;
            int price;
            int nShares;
            int total_cost;
            System.out.println("Number of shares to buy: ");
            nShares = Integer.parseInt(br.readLine());
            System.out.println("Price: ");
            price = Integer.parseInt(br.readLine());

            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from seller where nShares>=" + nShares + " and price<=" + price + " order by price");
            total_cost=nShares*price;
            if (rs.next()) {
                System.out.println("Seller id:"+rs.getInt("uid"));
                System.out.println("Shares to buy:" + nShares);
                System.out.println("Price:" + rs.getInt("price"));
                System.out.println("Total cost:"+total_cost);
            } else {
                System.out.println("No seller found matching your buying criteria!");
            }
            PreparedStatement ps = con.prepareStatement("update seller set nShares="+(rs.getInt("nShares")-nShares)+" where uid="+rs.getInt("uid"));
            ps.executeUpdate();
        } catch (IOException | NumberFormatException | SQLException e) {
            System.out.println(e);
        }
    }
//sell function takes the entry of how many shares and at what 
    // price the user wants to sell the shares he holds. 
    //if the particular value buyer or the buyer quoting 
    // higher price than the selling price is found, then
    // the selling of share is 
    // successful else unsuccessful operation.

    void sell() {
        int i = 0;
        while (i != 1) {
            i = verify(); //check for validity of the user.
        }

        GetDbConnection dbCon = new GetDbConnection();
        Connection con = dbCon.getconnection();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ResultSet rs;
            Statement stmt;
            int price;
            int nShares;
            int total_cost;
            System.out.println("Number of shares to sell: ");
            nShares = Integer.parseInt(br.readLine());
            System.out.println("Price: ");
            price = Integer.parseInt(br.readLine());

            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from buyer where nShares>=" + nShares + " and price>=" + price + " order by price DESC");
             total_cost=nShares*price;
            if (rs.next()) {
                System.out.println("Buyer id:"+rs.getInt("uid"));
                System.out.println("Shares to sell:" + rs.getInt("nShares"));
                System.out.println("Price:" + rs.getInt("price"));
                System.out.println("Total cost:"+total_cost);
            } else {
                System.out.println("No buyer found matching your selling criteria!");
            }
            PreparedStatement ps = con.prepareStatement("update buyer set nShares="+(rs.getInt("nShares")-nShares)+" where uid="+rs.getInt("uid"));
            ps.executeUpdate();
        } catch (IOException | NumberFormatException | SQLException e) {
            System.out.println(e);
        }
    }
}

public class SMS {  // main class SMS
//main method
    public static void main(String[] args) throws IOException {
        Customer c1 = new Customer();
        //creating a user friendly menu.
        System.out.println("1. Buy");
        System.out.println("2. Sell");
        System.out.println("3. Exit");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        int c=Integer.parseInt(br.readLine());
        switch(c)
        {
            case 1:
                c1.buy();
                break;
            case 2: 
                c1.sell();
                break;
            default:
                break;
        }
    }
}

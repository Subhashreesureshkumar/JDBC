package Order_Management;
import java.sql.*;
import java.util.Scanner;

public class Customer {
    public static void main(String args[]){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "");
            Statement st = con.createStatement();
            ResultSet rs;
            PreparedStatement insertProd;
            PreparedStatement insertOrders;
            PreparedStatement insert;
            boolean flag = true;
            Scanner obj = new Scanner(System.in);
            
            
            System.out.println("Welcome to Fruits mart!!");             // Fruits store
            System.out.println("To place orders , Enter your details:");   //customer details

            System.out.println("Enter your id:");
            int cust_id = obj.nextInt();

            System.out.println("Enter your name:");
            String cust_name = obj.next();

            System.out.println("Enter your phone number:");
            String phone = obj.next();

            System.out.println("Enter your emailid:");
            String cust_mail = obj.next();

            System.out.println("Enter your address:");
            String cust_addr = obj.next();

            rs = st.executeQuery("Select * from Customers");
            while (rs.next()) {
                if (rs.getInt(1) == cust_id) {    // checking for existing customer or not
                    flag = true;
                    break;
                } else {
                    flag = false;
                }
            }
            if (flag == false) {     // If new customer inserting into table
                insert = con.prepareStatement("insert into Customers values(?,?,?,?,?)");
                insert.setInt(1, cust_id);
                insert.setString(2, cust_name);
                insert.setString(3, phone);
                insert.setString(4, cust_mail);
                insert.setString(5, cust_addr);
                int i = insert.executeUpdate();
            }

            System.out.println("To view items list , press 1");    // to see products list 
            int list = obj.nextInt();
            if (list == 1) {
                rs = st.executeQuery("Select proID,pname,price from Products");
                System.out.println("S.No" + "          |         " + "Items" + "          |         " + "Price");
                System.out.println("----------------------------------------------------------------------");
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + "          |         " + rs.getString(2) + "          |         " + rs.getInt(3));
                }
                System.out.println("----------------------------------------------------------------------");

            }

            double r = Math.random();
            int ord_id = (int) ((r * 20000) + 756);   // some random values(unique)

            int choice = 2;
            while (choice == 2) {   // placing orders 
                System.out.println("Enter the item you want:");
                String item = obj.next();

                System.out.println("Enter the quantity(in kg):");
                int quantity = obj.nextInt();
                
                int pri = 0;
                PreparedStatement price = con.prepareStatement("Select Price from Products where pname=?");   // getting product's price from table
                price.setString(1, item);
                rs = price.executeQuery();
                while (rs.next()) {
                    pri = rs.getInt(1);    //unit price
                }
                int rate = quantity * pri;   // calculate rate

                insertProd = con.prepareStatement("insert into ord_prod values(?,?,?,?,?)");   // inserting into Order_Product table (contains particular order and products placed in order)
                insertProd.setInt(1, ord_id);
                insertProd.setString(2, item);
                insertProd.setInt(3, quantity);
                insertProd.setInt(4, pri);
                insertProd.setInt(5, rate);
                int i2 = insertProd.executeUpdate();
                System.out.println("To continue ordering items, again press 2");   // to add items to cart

                choice = obj.nextInt();
            }


            insertOrders = con.prepareStatement("insert into Orders values(?,?,?)");    // Orders table maintaining order id, cus id, time of order
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            insertOrders.setInt(1, ord_id);
            insertOrders.setInt(2, cust_id);
            insertOrders.setDate(3, date);
            int i1 = insertOrders.executeUpdate();


            System.out.println("ORDER PLACED!!");
            System.out.println("******************************************************BILL****************************************************");


            rs = st.executeQuery("Select Orders.ordID,pro,quantity,price,rate from Customers INNER JOIN Orders ON Customers.cusID=Orders.cusID INNER JOIN ord_prod ON Orders.ordID=ord_prod.ordID");
            System.out.println("Items" + "          |         " + "Quantity" + "          |         " + "Price" + "        |         " + "Rate");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.println( rs.getString(1) + "          |         " + rs.getInt(2) + "          |         " + rs.getInt(3) + "         |          " + rs.getInt(4));
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            System.out.println("If you want to cancel order, press 3");  // delete orders from orders table
            int cancel = obj.nextInt();
            if (cancel == 3) {
            PreparedStatement del = con.prepareStatement("delete from Orders where ordID=?");
            del.setInt(1, ord_id);
            int d = del.executeUpdate();
            System.out.println("ORDER CANCELLED!!");
            }
            System.out.println("Thank you visit again!!");
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
}

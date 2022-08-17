package Order_Management;
import java.sql.*;
import java.util.Scanner;

public class Customer {
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","");
            Statement st=con.createStatement();
            ResultSet rs;
            PreparedStatement insertProd;
            PreparedStatement insertOrders;
            PreparedStatement insert;
            boolean flag=true;
            Scanner obj=new Scanner(System.in);
            System.out.println("Welcome to Fruits mart!!");
            System.out.println("To place orders , enter your details:");

            System.out.println("Enter your id:");
            int cust_id=obj.nextInt();

            System.out.println("Enter your name:");
            String cust_name=obj.next();

            System.out.println("Enter your phone number:");
            String phone=obj.next();

            System.out.println("Enter your emailid:");
            String cust_mail=obj.next();

            System.out.println("Enter your address:");
            String cust_addr=obj.next();

            rs=st.executeQuery("Select * from Customers");
            while(rs.next()) {
                if(rs.getInt(1)==cust_id) {
                    flag=true;
                    break;
                }
                else {
                    flag=false;
                }
            }
            if(flag==false)
            {
                insert = con.prepareStatement("insert into Customers values(?,?,?,?,?)");
                insert.setInt(1, cust_id);
                insert.setString(2, cust_name);
                insert.setString(3, phone);
                insert.setString(4, cust_mail);
                insert.setString(5, cust_addr);
                int i = insert.executeUpdate();
            }

            int choice;
            int ord_id= (int) Math.random();
            PreparedStatement choose;
            do {
                System.out.println("Enter the item you want:");
                String prod = obj.next();

                System.out.println("Enter the quantity(in kg):");
                int quantity = obj.nextInt();

                choose=con.prepareStatement("Select proID from Products where pname=?");
                choose.setString(2,prod);
                int prod_id=choose.executeUpdate();

                insertProd=con.prepareStatement("insert into Order_Product values(?,?,?)");
                insertProd.setInt(1, ord_id);
                insertProd.setInt(2, prod_id);
                insertProd.setInt(3, quantity);

                int i2=insertProd.executeUpdate();
                System.out.println("To continue ordering items, enter 1");

                choice=obj.nextInt();
            }while(choice==1);


            insertOrders=con.prepareStatement("insert into Orders values(?,?,?)");

            Date date= (Date) st.executeQuery("SELECT LOCALTIME() + 1");
            insertOrders.setInt(1, ord_id);
            insertOrders.setInt(2, cust_id);
            insertOrders.setDate(3,date );
            int i1=insertOrders.executeUpdate();
            System.out.println("------------BILL-----------");

            System.out.println("ORDER PLACED!!");



            rs=st.executeQuery("Select * from Customers");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getInt(1)+"  "+rs.getString(4)+"  "+rs.getString(5));
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
}

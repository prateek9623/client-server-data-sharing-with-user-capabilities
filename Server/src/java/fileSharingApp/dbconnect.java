package fileSharingApp;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dbconnect {
    private static dbconnect connect = new dbconnect();
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private dbconnect(){}
    public static dbconnect dbconnectref(){
        return connect;
    }
    private void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/client-server-data-sharing","root","wasd.1234");
            st = con.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("DB Connection failed"+ex);
        }
    }
    public boolean authenticate(String user, String pass){
        try{
            connect();
            String query = "SELECT * FROM `user` WHERE `username` = '"+user+"' AND `password` = '"+pass+"'";
            rs = st.executeQuery(query);
            rs.last();
            boolean result = rs.getRow()>0;
            return result;
        }catch(SQLException ex) {
            System.out.println("Login failed: "+ex);
        }
        return false;
    }
    public boolean register(String user, String pass, String fname, String lname, String email, String phone,String dob, String gender){
        try{
            connect();
            String query = "INSERT INTO `user`(`username`,`email`,`password`,`fname`,`lname`,`contactno`,`gender`,`dob`) VALUES ('"+user+"', '"+email+"', '"+pass+"', '"+fname+"', '"+lname+"', '"+phone+"', '"+gender+"', '"+dob+"')";
            PreparedStatement insert = con.prepareStatement(query);
            boolean result = insert.executeUpdate()>0;
            return result;
        }catch(SQLException ex) {
            System.out.println("registration failed: "+ex);
        }
        return false;
    }
    public boolean create_session(String username,String sid){
        try {
            connect();
            String query = "INSERT INTO `sessions`(`sessionid`,`username`) VALUES('"+sid+"','"+username+"') ON DUPLICATE KEY UPDATE `sessionid` = '"+sid+"', `update_time` = current_timestamp();";
            PreparedStatement insert = con.prepareStatement(query);
            boolean result = insert.executeUpdate()>0;
            return result;
        } catch (SQLException ex) {
            System.out.println("session not created: "+ex);
        }
        return false;
    }
    public void delete_session(String sid){
        try{
            connect();
            String query = "DELETE FROM sessions WHERE sessionid = '"+sid+"' ";
            PreparedStatement delete = con.prepareStatement(query);
            delete.execute();
        } catch (SQLException ex) {
            System.out.println("Session not deleted: "+ex);
        }
    }
    public boolean check_session(String sid){
        try{
            connect();
            String query = "SELECT * from sessions where sessioid = '"+sid+"' ";
            rs = st.executeQuery(query);
            rs.last();
            int i = rs.getRow();
            return i>0;
        } catch (SQLException ex) {
            Logger.getLogger(dbconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean update_session(String sid){
        try{
            String query =  "UPDATE `sessions`\n" +
                            "SET\n" +
                            "`update_time` = CURRENT_TIMESTAMP()\n" +
                            "WHERE `sessionid` = '"+sid+"'";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i>0;
        } catch (SQLException ex) {
            System.out.println("Couldnt update session: "+ex);
        }
        return false;
    }
    
}

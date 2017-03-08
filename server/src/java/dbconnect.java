
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dbconnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    public dbconnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cloud_data_sharing","root","");
            st = con.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("DB Connection failed"+ex);
        }
    }
    public boolean authenticate(String user, String pass){
        try{
            String query = "SELECT * FROM user_details WHERE username='"+user+"' AND password='"+pass+"'";
            rs = st.executeQuery(query);
            rs.last();
            return rs.getRow()>0;
        }catch(SQLException ex) {
            System.out.println("Login failed: "+ex);
        }
        return false;
    }
    public boolean register(String user, String pass, String fname, String lname, String email, String phone,String dob, String gender){
        try{
            String query = "INSERT INTO `user_details` (`username`, `password`, `firstname`, `surname`, `email`, `contact`, `dob`, `gender`) VALUES ('"+fname+"', '"+pass+"', '"+fname+"', '"+lname+"', '"+email+"', '"+phone+"', '"+dob+"', '"+gender+"')";
            PreparedStatement insert = con.prepareStatement(query);
            return insert.executeUpdate()>0;
        }catch(SQLException ex) {
            System.out.println("registration failed: "+ex);
        }
        return false;
    }
    public boolean create_session(String username,String sid){
        try {
            String query = "INSERT INTO session_details (username, sessionid) VALUES ('"+username+"','"+sid+"')";
            PreparedStatement insert = con.prepareStatement(query);
            if(insert.executeUpdate()>0){
                return false;
            }else{
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("session not created: "+ex);
        }
        return false;
    }
    public void delete_session(String sid){
        try{
            String query = "DELETE FROM session_details WHERE sessionid = '"+sid+"' ";
            PreparedStatement delete = con.prepareStatement(query);
            delete.execute();
            con.close();
        } catch (SQLException ex) {
            System.out.println("Session not deleted: "+ex);
        }
    }
    public boolean check_session(String sid){
        try{
            String query = "SELECT * from session_details where timestamp >= NOW() - INTERVAL 1 HOUR AND sessioid = '"+sid+"' ";
            rs = st.executeQuery(query);
            rs.last();
            return rs.getRow()>0;
        } catch (SQLException ex) {
            Logger.getLogger(dbconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public void update_session(String sid){
        try{
            String query = "UPDATE session_details SET Timestamp=CURRENT_TIMESTAMP WHERE sessionid = '"+sid+"'";
            PreparedStatement update = con.prepareStatement(query);
            update.executeUpdate();
            con.close();
        } catch (SQLException ex) {
            System.out.println("Couldnt update session: "+ex);
        }
    }
    
}

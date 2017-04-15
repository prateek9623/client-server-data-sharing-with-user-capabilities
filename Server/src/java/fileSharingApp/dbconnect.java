package fileSharingApp;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dbconnect {
    private static dbconnect connect1 = new dbconnect();
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private dbconnect(){}
    public static dbconnect dbconnectref(){
        return connect1;
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
            String query = "INSERT INTO `sessions`(`sessionid`,`userid`,`status`) VALUES('"+sid+"',(select `userid` from `user` where `username` = '"+username+"'),'true') ON DUPLICATE KEY UPDATE `sessionid` = '"+sid+"', `update_time` = current_timestamp();";
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
            String query = "UPDATE `client-server-data-sharing`.`sessions` SET `status`='false' WHERE `sessionid`= '"+sid+"' ";
            PreparedStatement delete = con.prepareStatement(query);
            delete.execute();
        } catch (SQLException ex) {
            System.out.println("Session not deleted: "+ex);
        }
    }
    public String check_session(String sid){
        try{
            connect();
            String query = "SELECT userid from sessions where sessionid = '"+sid+"' AND `status`='true'";
            rs = st.executeQuery(query);
            rs.last();
            String i = rs.getString("userid");
            return i;
        } catch (SQLException ex) {
            
        }
        return null;
    }
    public boolean update_session(String sid){
        try{
            connect();
            String query =  "UPDATE `sessions`\n" +
                            "SET\n" +
                            "`update_time` = CURRENT_TIMESTAMP()\n" +
                            "WHERE `sessionid` = '"+sid+"' AND `status` = 'true'";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i>0;
        } catch (SQLException ex) {
            System.out.println("Couldnt update session: "+ex);
        }
        return false;
    }
    public boolean uploadfileentry(String filename, String sessionid, String filetype, String path){
        try{
            connect();
            String query = "INSERT INTO `client-server-data-sharing`.`file_list`\n" +
                            "(`file_name`,\n" +
                            "`owner_id`,\n"+
                            "`file_size`,\n" +
                            "`path`)\n" +
                            "VALUES\n" +
                            "(\""+filename+"\",\n" +
                            "(select userid from sessions where sessionid = \""+sessionid+"\"),\n" +
                            "\""+filetype+"\",\n" +
                            "\""+path+"\");";
            PreparedStatement insert = con.prepareStatement(query);
            return insert.executeUpdate()>0;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    public ResultSet getfilelist(String sessionid){
        try{
            connect();
            ResultSet rs = st.executeQuery("Select * from file_list where owner_id = (select userid from sessions where sessionid = '"+sessionid+"')");
            rs.last();
            return rs;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    String getFilePath(String sessionid, String fileid) {
        try {
            connect();
            ResultSet rs = st.executeQuery("SELECT \n" +
                                            "    path\n" +
                                            "FROM\n" +
                                            "    file_list\n" +
                                            "WHERE\n" +
                                            "    owner_id = (SELECT \n" +
                                            "            userid\n" +
                                            "        FROM\n" +
                                            "            sessions\n" +
                                            "        WHERE\n" +
                                            "            sessionid = '"+sessionid+"')\n" +
                                            "        AND file_id = '"+fileid+"'");
            rs.first();
            System.out.println(rs.getString("path"));
            return rs.getString("path");
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    String delete(String sessionid, String deletefile) {
        try{
           connect();
           String getfilepath = getFilePath(sessionid, deletefile);
           if(getfilepath!=null||!getfilepath.equals("")){
                String query = "DELETE FROM file_list WHERE owner_id = (Select userid from sessions where sessionid = '"+sessionid+"') AND file_id ='"+deletefile+"'";
                PreparedStatement delete = con.prepareStatement(query);
                delete.execute();
                return getfilepath;
           }
       } catch (SQLException ex) {
       }
       return null;
    }
    
}

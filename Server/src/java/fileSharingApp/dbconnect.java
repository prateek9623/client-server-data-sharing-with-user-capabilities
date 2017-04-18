package fileSharingApp;


import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    String getFilePath(String sessionid, String fileid) {
        try {
            connect();
            ResultSet rs = st.executeQuery("SELECT \n" +
                                            "    path\n" +
                                            "FROM\n" +
                                            "    file_list\n" +
                                            "WHERE\n" +
                                            "    file_id = "+fileid+"\n" +
                                            "        AND (owner_id = (SELECT \n" +
                                            "            userid\n" +
                                            "        FROM\n" +
                                            "            sessions\n" +
                                            "        WHERE\n" +
                                            "            sessionid = '"+sessionid+"')\n" +
                                            "        OR owner_id = (SELECT \n" +
                                            "            sharer_id\n" +
                                            "        FROM\n" +
                                            "            shared_file_list\n" +
                                            "        WHERE\n" +
                                            "            file_id = "+fileid+"\n" +
                                            "                AND shared_to_id = (SELECT \n" +
                                            "                    userid\n" +
                                            "                FROM\n" +
                                            "                    sessions\n" +
                                            "                WHERE\n" +
                                            "                    sessionid = '"+sessionid+"')))");
            rs.first();
            System.out.println(rs.getString("path"));
            return rs.getString("path");
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    String delete(String sessionid, String deletefile, String password) {
        try{
           String getfilepath = getFilePath(sessionid, deletefile);
           if(getfilepath!=null||!getfilepath.equals("")){
                String query = "DELETE FROM file_list WHERE owner_id = (Select userid from user where userid=(Select userid from sessions where sessionid = '"+sessionid+"') and password = '"+password+"') AND file_id ='"+deletefile+"'";
                PreparedStatement delete = con.prepareStatement(query);
                delete.execute();
                return getfilepath;
           }
           return "null";
       } catch (SQLException ex) {
            System.out.println(ex);
            return "null";
       }
    }

    String check_password(String sessionid, String sharerpassword) {
        try{
            connect();
            String query = "SELECT userid FROM user WHERE (SELECT userid FROM sessions WHERE sessionid = '"+sessionid+"') AND password = '"+sharerpassword+"'";
            ResultSet rs = st.executeQuery(query);
            rs.last();
            boolean result = rs.getRow()>0;
            if(result)
                return rs.getString("userid");
            else 
                return "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    String check_userexist(String sharetoid) {
        try{
            connect();
            String query = "SELECT userid FROM user WHERE username = '"+sharetoid+"'";
            ResultSet rs = st.executeQuery(query);
            rs.last();
            boolean result = rs.getRow()>0;
            if(result)
                return rs.getString("userid");
            else 
                return "";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    boolean share(String fileid, String sharetoid, String userid) {
        try{
            connect();
            String query = "INSERT INTO `shared_file_list` (file_id, sharer_id, shared_to_id) VALUES ('"+fileid+"', '"+userid+"', '"+sharetoid+"');";
            PreparedStatement insert = con.prepareStatement(query);
            return insert.executeUpdate()>0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    boolean getfilelist(String sessionid, List<file> filelist) {
        boolean status = false;
        try{
            connect();
            String query =  "SELECT \n" +
                            "    f.file_id,\n" +
                            "    f.file_name,\n" +
                            "    f.file_size,\n" +
                            "    f.stored_on,\n" +
                            "    u.username,\n" +
                            "    'false' shared,\n" +
                            "    f.predelete\n" +
                            "FROM\n" +
                            "    file_list f\n" +
                            "        JOIN\n" +
                            "    user u\n" +
                            "WHERE\n" +
                            "    f.owner_id = u.userid\n" +
                            "        AND f.owner_id = (SELECT \n" +
                            "            userid\n" +
                            "        FROM\n" +
                            "            sessions\n" +
                            "        WHERE\n" +
                            "            sessionid = '"+sessionid+"') \n" +
                            "UNION ALL SELECT \n" +
                            "    f.file_id,\n" +
                            "    f.file_name,\n" +
                            "    f.file_size,\n" +
                            "    s.shared_on,\n" +
                            "    (SELECT \n" +
                            "            username\n" +
                            "        FROM\n" +
                            "            user\n" +
                            "        WHERE\n" +
                            "            userid = s.sharer_id) 'username',\n" +
                            "    'true' shared,\n" +
                            "    'false' predelete\n" +
                            "FROM\n" +
                            "    file_list f\n" +
                            "        JOIN\n" +
                            "    shared_file_list s\n" +
                            "WHERE\n" +
                            "    f.file_id = s.file_id\n" +
                            "        AND s.shared_to_id = (SELECT \n" +
                            "            userid\n" +
                            "        FROM\n" +
                            "            sessions\n" +
                            "        WHERE\n" +
                            "            sessionid = '"+sessionid+"');";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                file file1 = new file();
                file1.setFile_id(rs.getString(1));
                file1.setFile_name(rs.getString(2));
                file1.setFile_size(rs.getString(3));
                file1.setStored_on(rs.getString(4));
                file1.setOwnerusername(rs.getString(5));
                file1.setShared(rs.getString(6));
                file1.setPredelete(rs.getString(7));
                filelist.add(file1);
                status=true;
            }
        } catch (SQLException ex) { 
            return false;
        }
        return status;
    }

    boolean predel(String sessionid, String fileid) {
        try{
            connect();
            String query =  "UPDATE `client-server-data-sharing`.`file_list` \n" +
                            "SET \n" +
                            "    `predelete` = 'true'\n" +
                            "WHERE\n" +
                            "    `file_id` = '"+fileid+"'\n" +
                            "        AND owner_id = (SELECT \n" +
                            "            userid\n" +
                            "        FROM\n" +
                            "            sessions\n" +
                            "        WHERE\n" +
                            "            sessionid = '"+sessionid+"');";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i>0;
        } catch (SQLException ex) {
            System.out.println("Couldnt remove file: "+ex);
        }
        return false;
    }

    boolean restore(String sessionid, String fileid) {
        try{
            connect();
            String query =  "UPDATE `client-server-data-sharing`.`file_list` \n" +
                            "SET \n" +
                            "    `predelete` = 'false'\n" +
                            "WHERE\n" +
                            "    `file_id` = '"+fileid+"'\n" +
                            "        AND owner_id = (SELECT \n" +
                            "            userid\n" +
                            "        FROM\n" +
                            "            sessions\n" +
                            "        WHERE\n" +
                            "            sessionid = '"+sessionid+"');";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i>0;
        } catch (SQLException ex) {
            System.out.println("Couldnt remove file: "+ex);
        }
        return false;
    }

    boolean removeshare(String sessionid, String fileid) {
        try{
            connect();
            String query =  "DELETE FROM shared_file_list \n" +
                            "WHERE\n" +
                            "    file_id = '"+fileid+"'\n" +
                            "    AND (shared_to_id = (SELECT \n" +
                            "        userid\n" +
                            "    FROM\n" +
                            "        sessions\n" +
                            "    \n" +
                            "    WHERE\n" +
                            "        sessionid = '"+sessionid+"'));";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i>0;
        } catch (SQLException ex) {
            System.out.println("Couldnt remove file: "+ex);
        }
        return false;
    }
}

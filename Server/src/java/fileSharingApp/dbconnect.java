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

    private dbconnect() {
    }

    public static dbconnect dbconnectref() {
        return connect1;
    }

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/client-server-data-sharing", "root", "wasd.1234");
            st = con.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
        }
    }

    public boolean authenticate(String user, String pass) {
        try {
            connect();
            String query = "SELECT * FROM `user` WHERE `username` = '" + user + "' AND `password` = '" + pass + "'";
            rs = st.executeQuery(query);
            rs.last();
            boolean result = rs.getRow() > 0;
            return result;
        } catch (SQLException ex) {
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    public boolean register(String user, String pass, String fname, String lname, String email, String phone, String dob, String gender) {
        try {
            connect();
            String query = "INSERT INTO `user`(`username`,`email`,`password`,`fname`,`lname`,`contactno`,`gender`,`dob`) VALUES ('" + user + "', '" + email + "', '" + pass + "', '" + fname + "', '" + lname + "', '" + phone + "', '" + gender + "', '" + dob + "')";
            PreparedStatement insert = con.prepareStatement(query);
            boolean result = insert.executeUpdate() > 0;
            return result;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    public boolean create_session(String username, String sid) {
        try {
            connect();
            String query = "INSERT INTO `sessions`(`sessionid`,`userid`,`status`) VALUES('" + sid + "',(select `userid` from `user` where `username` = '" + username + "'),'true') ON DUPLICATE KEY UPDATE `sessionid` = '" + sid + "', `update_time` = current_timestamp();";
            PreparedStatement insert = con.prepareStatement(query);
            boolean result = insert.executeUpdate() > 0;
            return result;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
        
    }

    public void delete_session(String sid) {
        try {
            connect();
            String query = "UPDATE `client-server-data-sharing`.`sessions` SET `status`='false' WHERE `sessionid`= '" + sid + "' ";
            PreparedStatement delete = con.prepareStatement(query);
            delete.execute();
        } catch (SQLException ex) {
            return;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    public String check_session(String sid) {
        try {
            connect();
            String query = "SELECT userid from sessions where sessionid = '" + sid + "' AND `status`='true'";
            rs = st.executeQuery(query);
            rs.last();
            String i = rs.getString("userid");
            return i;
        } catch (SQLException ex) {
            return null;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    public boolean update_session(String sid) {
        try {
            connect();
            String query = "UPDATE `sessions`\n"
                    + "SET\n"
                    + "`update_time` = CURRENT_TIMESTAMP()\n"
                    + "WHERE `sessionid` = '" + sid + "' AND `status` = 'true'";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i > 0;
        } catch (SQLException ex) {
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
        return false;
    }

    public boolean uploadfileentry(String filename, String sessionid, String filesize, String path) {
        try {
            connect();
            String query = "INSERT INTO `client-server-data-sharing`.`file_list`\n"
                    + "(`file_name`,\n"
                    + "`owner_id`,\n"
                    + "`file_size`,\n"
                    + "`path`)\n"
                    + "VALUES\n"
                    + "(\"" + filename + "\",\n"
                    + "(select userid from sessions where sessionid = \"" + sessionid + "\"),\n"
                    + "\"" + filesize + "\",\n"
                    + "\"" + path + "\");";
            PreparedStatement insert = con.prepareStatement(query);
            return insert.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    String getFilePath(String sessionid, String fileid) {
        try {
            connect();
            ResultSet rs = st.executeQuery("SELECT \n"
                    + "    path\n"
                    + "FROM\n"
                    + "    file_list\n"
                    + "WHERE\n"
                    + "    file_id = " + fileid + "\n"
                    + "        AND (owner_id = (SELECT \n"
                    + "            userid\n"
                    + "        FROM\n"
                    + "            sessions\n"
                    + "        WHERE\n"
                    + "            sessionid = '" + sessionid + "')\n"
                    + "        OR owner_id = (SELECT \n"
                    + "            sharer_id\n"
                    + "        FROM\n"
                    + "            shared_file_list\n"
                    + "        WHERE\n"
                    + "            file_id = " + fileid + "\n"
                    + "                AND shared_to_id = (SELECT \n"
                    + "                    userid\n"
                    + "                FROM\n"
                    + "                    sessions\n"
                    + "                WHERE\n"
                    + "                    sessionid = '" + sessionid + "')))");
            rs.first();
            return rs.getString("path");
        } catch (SQLException ex) {
            return null;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    String delete(String sessionid, String deletefile, String password) {
        try {
            String getfilepath = getFilePath(sessionid, deletefile);
            connect();
            if (getfilepath != null || !getfilepath.equals("")) {
                String query = "DELETE FROM file_list WHERE owner_id = (Select userid from user where userid=(Select userid from sessions where sessionid = '" + sessionid + "') and password = '" + password + "') AND file_id ='" + deletefile + "'";
                PreparedStatement delete = con.prepareStatement(query);
                delete.execute();
            }
            return getfilepath;
        } catch (SQLException ex) {
            System.out.println(ex);
            return "null";
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    String check_password(String sessionid, String sharerpassword) {
        try {
            connect();
            String query = "SELECT userid FROM user WHERE (SELECT userid FROM sessions WHERE sessionid = '" + sessionid + "') AND password = '" + sharerpassword + "'";
            ResultSet rs = st.executeQuery(query);
            rs.last();
            boolean result = rs.getRow() > 0;
            if (result) {
                return rs.getString("userid");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            return "";
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    String check_userexist(String sharetoid) {
        try {
            connect();
            String query = "SELECT userid FROM user WHERE username = '" + sharetoid + "'";
            ResultSet rs = st.executeQuery(query);
            rs.last();
            boolean result = rs.getRow() > 0;
            if (result) {
                return rs.getString("userid");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            return "";
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    boolean share(String fileid, String sharetoid, String userid) {
        try {
            connect();
            String query = "INSERT INTO `shared_file_list` (file_id, sharer_id, shared_to_id) VALUES ('" + fileid + "', '" + userid + "', '" + sharetoid + "');";
            PreparedStatement insert = con.prepareStatement(query);
            return insert.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    boolean getfilelist(String sessionid, List<file> filelist) {
        boolean status = false;
        try {
            connect();
            String query = " SELECT \n"
                    + "    * \n"
                    + "FROM\n"
                    + "    (SELECT \n"
                    + "        f.file_id,\n"
                    + "            f.file_name,\n"
                    + "            f.file_size,\n"
                    + "            f.stored_on,\n"
                    + "            u.username AS sharer,\n"
                    + "            u.username AS sharedto,\n"
                    + "            (SELECT \n"
                    + "                    COUNT(*)\n"
                    + "                FROM\n"
                    + "                    shared_file_list s\n"
                    + "                WHERE\n"
                    + "                    s.file_id = f.file_id\n"
                    + "                        AND s.sharer_id = f.owner_id) AS shared,\n"
                    + "            f.predelete,\n"
                    + "            'mf' directory\n"
                    + "    FROM\n"
                    + "        file_list f\n"
                    + "    JOIN user u\n"
                    + "    WHERE\n"
                    + "        f.owner_id = u.userid\n"
                    + "            AND f.owner_id = (SELECT \n"
                    + "                userid\n"
                    + "            FROM\n"
                    + "                sessions\n"
                    + "            WHERE\n"
                    + "                sessionid = '" + sessionid + "') UNION ALL SELECT \n"
                    + "            sh.id,\n"
                    + "            f.file_name,\n"
                    + "            f.file_size,\n"
                    + "            sh.shared_on,\n"
                    + "            (SELECT \n"
                    + "                    username\n"
                    + "                FROM\n"
                    + "                    user\n"
                    + "                WHERE\n"
                    + "                    userid = sh.sharer_id) AS sharer,\n"
                    + "            (SELECT \n"
                    + "                    username\n"
                    + "                FROM\n"
                    + "                    user\n"
                    + "                WHERE\n"
                    + "                    userid = sh.shared_to_id) AS sharedto,\n"
                    + "            1 shared,\n"
                    + "            'false' predelete,\n"
                    + "            'shared' directory\n"
                    + "    FROM\n"
                    + "        file_list f\n"
                    + "    JOIN shared_file_list sh\n"
                    + "    WHERE\n"
                    + "        f.file_id = sh.file_id\n"
                    + "            AND sh.shared_to_id = (SELECT \n"
                    + "                userid\n"
                    + "            FROM\n"
                    + "                sessions\n"
                    + "            WHERE\n"
                    + "                sessionid = '" + sessionid + "') UNION ALL (SELECT \n"
                    + "        sh.id,\n"
                    + "            f.file_name,\n"
                    + "            f.file_size,\n"
                    + "            sh.shared_on,\n"
                    + "            (SELECT \n"
                    + "                    username\n"
                    + "                FROM\n"
                    + "                    user\n"
                    + "                WHERE\n"
                    + "                    userid = sh.sharer_id) AS sharer,\n"
                    + "            (SELECT \n"
                    + "                    username\n"
                    + "                FROM\n"
                    + "                    user\n"
                    + "                WHERE\n"
                    + "                    userid = sh.shared_to_id) AS sharedto,\n"
                    + "            1 shared,\n"
                    + "            'false' predelete,\n"
                    + "            'sharedto' directory\n"
                    + "    FROM\n"
                    + "        file_list f\n"
                    + "    JOIN shared_file_list sh\n"
                    + "    WHERE\n"
                    + "        f.file_id = sh.file_id\n"
                    + "            AND (sh.sharer_id = (SELECT \n"
                    + "                userid\n"
                    + "            FROM\n"
                    + "                sessions\n"
                    + "            WHERE\n"
                    + "                sessionid = '" + sessionid + "')))) s\n"
                    + "ORDER BY s.file_name ASC;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                file file1 = new file();
                file1.setFile_id(rs.getString(1));
                file1.setFile_name(rs.getString(2));
                file1.setFile_size(rs.getString(3));
                file1.setStored_on(rs.getString(4));
                file1.setOwnerusername(rs.getString(5));
                file1.setSharedto(rs.getString(6));
                file1.setShared(rs.getString(7));
                file1.setPredelete(rs.getString(8));
                file1.setDirectory(rs.getString(9));
                filelist.add(file1);
                status = true;
            }
            return status;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    boolean predel(String sessionid, String fileid) {
        try {
            connect();
            String query = "UPDATE `client-server-data-sharing`.`file_list` \n"
                    + "SET \n"
                    + "    `predelete` = 'true'\n"
                    + "WHERE\n"
                    + "    `file_id` = '" + fileid + "'\n"
                    + "        AND owner_id = (SELECT \n"
                    + "            userid\n"
                    + "        FROM\n"
                    + "            sessions\n"
                    + "        WHERE\n"
                    + "            sessionid = '" + sessionid + "');";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i > 0;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    boolean restore(String sessionid, String fileid) {
        try {
            connect();
            String query = "UPDATE `client-server-data-sharing`.`file_list` \n"
                    + "SET \n"
                    + "    `predelete` = 'false'\n"
                    + "WHERE\n"
                    + "    `file_id` = '" + fileid + "'\n"
                    + "        AND owner_id = (SELECT \n"
                    + "            userid\n"
                    + "        FROM\n"
                    + "            sessions\n"
                    + "        WHERE\n"
                    + "            sessionid = '" + sessionid + "');";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i > 0;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    boolean updateFileDetails(String sessionid, String path, String name, String fileid, String filesize) {
        try {
            connect();
            String query = "UPDATE `client-server-data-sharing`.`file_list` \n"
                    + "SET \n"
                    + "    `file_name` = '" + name + "',\n"
                    + "    `file_size` = " + filesize + ",\n"
                    + "    `path` = '" + path + "'\n"
                    + "WHERE\n"
                    + "    `file_id` = '" + fileid + "'\n"
                    + "        AND owner_id = (SELECT \n"
                    + "            userid\n"
                    + "        FROM\n"
                    + "            sessions\n"
                    + "        WHERE\n"
                    + "            sessionid = '" + sessionid + "')";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i > 0;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    boolean unShare(String userid, String shareid) {
        try {
            connect();
            String query = "DELETE FROM shared_file_list \n"
                    + "WHERE\n"
                    + "    id = '" + shareid + "'\n"
                    + "    AND (shared_to_id = (SELECT \n"
                    + "        userid\n"
                    + "    FROM\n"
                    + "        user\n"
                    + "    \n"
                    + "    WHERE\n"
                    + "        userid = '" + userid + "') OR "
                    + "     sharer_id = (SELECT \n"
                    + "        userid\n"
                    + "    FROM\n"
                    + "        user\n"
                    + "    \n"
                    + "    WHERE\n"
                    + "        userid = '" + userid + "'));";
            PreparedStatement update = con.prepareStatement(query);
            int i = update.executeUpdate();
            return i > 0;
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }
}

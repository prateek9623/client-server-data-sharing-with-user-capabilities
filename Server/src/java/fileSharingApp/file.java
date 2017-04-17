/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

/**
 *
 * @author Prateek
 */
public class file {

    private String file_id;
    private String file_name;
    private String file_size;
    private String stored_on;
    private String ownerusername;
    private String shared;
    private String predelete;

    public String getPredelete() {
        return predelete;
    }

    public void setPredelete(String predelete) {
        this.predelete = predelete;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getStored_on() {
        return stored_on;
    }

    public void setStored_on(String stored_on) {
        this.stored_on = stored_on;
    }

    public String getOwnerusername() {
        return ownerusername;
    }

    public void setOwnerusername(String ownerusername) {
        this.ownerusername = ownerusername;
    }


}

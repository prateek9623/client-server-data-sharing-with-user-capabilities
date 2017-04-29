
package client;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class sharedToFileList extends RecursiveTreeObject<sharedToFileList> {
    private SimpleStringProperty file_id;
    private SimpleStringProperty file_name;
    private SimpleStringProperty file_size;
    private SimpleStringProperty shared_on;
    private SimpleStringProperty sharedto;

    public sharedToFileList(String file_id, String file_name, String file_size, String stored_on, String sharedto) {
        this.file_id = new SimpleStringProperty(file_id);
        this.file_name = new SimpleStringProperty(file_name);
        this.file_size = new SimpleStringProperty(file_size);
        this.shared_on = new SimpleStringProperty(stored_on);
        this.sharedto = new SimpleStringProperty(sharedto);
    }

    public String getFile_id() {
        return file_id.get();
    }
    
    public SimpleStringProperty getFile_name() {
        return file_name;
    }

    public SimpleStringProperty getFile_size() {
        return file_size;
    }

    public SimpleStringProperty getShared_on() {
        return shared_on;
    }

    public SimpleStringProperty getSharedto() {
        return sharedto;
    }    
}

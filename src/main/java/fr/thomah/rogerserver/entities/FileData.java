package fr.thomah.rogerserver.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
public class FileData {

    @Id
    public String id;

    public String fileName;

    public boolean isSync = false;

    public FileData(String fileName) {
        this.fileName = fileName;
    }

}

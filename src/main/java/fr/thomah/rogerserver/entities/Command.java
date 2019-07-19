package fr.thomah.rogerserver.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "commands")
public class Command {

    @Id
    public String id;

    public String endpoint;

    public Map<String, String> params = new HashMap<>();

    public Command(String endpoint) {
        this.endpoint = endpoint;
    }

    public Command addParam(String key, String value) {
        params.put(key, value);
        return this;
    }
}

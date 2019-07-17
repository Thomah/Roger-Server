package fr.thomah.rogerserver;

import java.util.HashMap;
import java.util.Map;

public class Command {

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

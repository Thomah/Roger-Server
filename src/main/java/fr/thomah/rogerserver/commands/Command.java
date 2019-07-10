package fr.thomah.rogerserver.commands;

import java.util.Map;

public class Command {

    public String endpoint;

    public Map<String, String> params;

    public Command(String endpoint) {
        this.endpoint = endpoint;
    }

    public Command addParam(String key, String value) {
        params.put(key, value);
        return this;
    }
}

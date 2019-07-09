package fr.thomah.rogerserver.entities;

import org.springframework.data.annotation.Id;

public class SlackConfiguration {

    @Id
    public String id;

    public String channel;

    public SlackConfiguration() {}

    public SlackConfiguration(String channel) {
        this.channel = channel;
    }
}

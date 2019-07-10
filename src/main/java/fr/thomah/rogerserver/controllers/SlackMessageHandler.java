package fr.thomah.rogerserver.controllers;

import com.github.seratch.jslack.api.rtm.RTMMessageHandler;
import fr.thomah.rogerserver.commands.Tts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SlackMessageHandler implements RTMMessageHandler {

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void handle(String s) {
        this.template.convertAndSend("/command/tts", new Tts(s, "1", "0", "0"));
    }
}

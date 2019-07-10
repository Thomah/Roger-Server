package fr.thomah.rogerserver.controllers;

import com.google.gson.JsonObject;
import fr.thomah.rogerserver.commands.Tts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@EnableScheduling
@Controller
public class SlackController {

    @Autowired
    private SimpMessagingTemplate template;

    public void processRtm(JsonObject json) {
        this.template.convertAndSend("/command/tts", new Tts("Bonjour !", "1", "0", "0"));
    }

}

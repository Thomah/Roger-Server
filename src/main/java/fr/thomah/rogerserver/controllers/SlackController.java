package fr.thomah.rogerserver.controllers;

import fr.thomah.rogerserver.entities.SlackConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@EnableScheduling
@Controller
public class SlackController {

    @Autowired
    private SimpMessagingTemplate template;

    @SendTo("/topic/slack")
    @Scheduled(fixedRate = 5000)
    public void get() {
        System.out.println("scheduled");
        this.template.convertAndSend("/topic/slack", new SlackConfiguration("CKQ84CXM5"));
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting() throws InterruptedException {
        Thread.sleep(1000);
        return "Hello, world !";
    }

}

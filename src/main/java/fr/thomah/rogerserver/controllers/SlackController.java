package fr.thomah.rogerserver.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SlackController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting() throws InterruptedException {
        Thread.sleep(1000);
        return "Hello, world !";
    }

}

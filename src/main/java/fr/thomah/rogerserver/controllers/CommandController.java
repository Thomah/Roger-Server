package fr.thomah.rogerserver.controllers;

import fr.thomah.rogerserver.entities.Command;
import fr.thomah.rogerserver.repositories.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CommandController {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private SimpMessagingTemplate template;

    public void broadcast(Command command) {
        commandRepository.save(command);
        this.template.convertAndSend("/command", command);
    }

}

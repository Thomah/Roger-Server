package fr.thomah.rogerserver.controllers;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.rtm.RTMClient;
import fr.thomah.rogerserver.RogerServerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.DeploymentException;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class MainController {

    @Autowired
    private SlackMessageHandler slackMessageHandler;

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object health() {
        Slack slack = Slack.getInstance();
        RTMClient rtm;
        try {
            rtm = slack.rtm(RogerServerApplication.SLACK_BOT_TOKEN);
            rtm.addMessageHandler(slackMessageHandler);
            rtm.connect();
        } catch (IOException | DeploymentException e) {
            e.printStackTrace();
        }
        return null;
    }

}

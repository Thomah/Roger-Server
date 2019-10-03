package fr.thomah.rogerserver.controllers;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.rtm.RTMClient;
import fr.thomah.rogerserver.RogerServerApplication;
import fr.thomah.rogerserver.SlackMessageHandler;
import fr.thomah.rogerserver.entities.Command;
import fr.thomah.rogerserver.entities.FileData;
import fr.thomah.rogerserver.entities.LocalSoundCommand;
import fr.thomah.rogerserver.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
public class MainController {

    @Autowired
    private CommandController commandController;

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private SlackMessageHandler slackMessageHandler;

    private RTMClient rtm = null;

    private int nbFiles = 0;
    private List<FileData> fileDataList;
    private int probaMin = 0;
    private final static int probaMax = 3600;

    @PostConstruct
    public void init() {
        refreshFileDataList();
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void health() {
        Slack slack = Slack.getInstance();
        refreshFileDataList();
        try {
            if(rtm != null) {
                rtm.disconnect();
            }
            rtm = slack.rtm(RogerServerApplication.SLACK_BOT_TOKEN);
            rtm.addMessageHandler(slackMessageHandler);
            rtm.connect();
        } catch (IOException | DeploymentException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000)
    public void randomSpeak() {
        int random = generateRandomBetween(probaMin, probaMax);
        System.out.println("Play random sound ? " + probaMin + " / " + probaMax);
        if (random == probaMax) {
            probaMin = 0;
            int numSound = generateRandomBetween(0, nbFiles);
            FileData fileData = fileDataList.get(numSound);
            commandController.broadcast(new LocalSoundCommand(fileData.fileName));
        } else {
            probaMin++;
        }
    }

    private void refreshFileDataList() {
        fileDataList = fileDataRepository.findByIsSync(true);
        nbFiles = fileDataList.size();
        slackMessageHandler.setFileDataList(fileDataList);
    }

    private int generateRandomBetween(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

}

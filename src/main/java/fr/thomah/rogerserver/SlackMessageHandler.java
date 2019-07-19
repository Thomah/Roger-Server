package fr.thomah.rogerserver;

import com.github.seratch.jslack.api.rtm.RTMMessageHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.thomah.rogerserver.controllers.CommandController;
import fr.thomah.rogerserver.entities.Command;
import fr.thomah.rogerserver.entities.FileData;
import fr.thomah.rogerserver.entities.LocalSoundCommand;
import fr.thomah.rogerserver.entities.TtsCommand;
import fr.thomah.rogerserver.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SlackMessageHandler implements RTMMessageHandler {

    @Autowired
    private CommandController commandController;

    @Autowired
    private FileDataRepository fileDataRepository;

    private List<FileData> fileDataList;

    private String slackGrafanaBotId = System.getenv("SLACK_GRAFANA_BOT_ID");

    @Override
    public void handle(String message) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        Command objectToSend = null;

        // If Grafana is alerting
        JsonElement botId = jsonObject.get("bot_id");
        if (botId != null && slackGrafanaBotId.equals(botId.getAsString())) {
            JsonElement attachmentsElement = jsonObject.get("attachments");
            if (attachmentsElement != null) {
                JsonArray attachmentsArray = attachmentsElement.getAsJsonArray();
                JsonElement titleElement = attachmentsArray.get(0).getAsJsonObject().get("title");
                if (titleElement != null && !titleElement.getAsString().equals("")) {
                    String title = titleElement.getAsString();
                    title = title.replace("[Alerting] ", "");
                    title = "Un nouveau " + title + " a été détecté.";
                    objectToSend = new TtsCommand(title);
                }
            }
        }

        // If there is a text message
        JsonElement textElement = jsonObject.get("text");
        if (textElement != null && !textElement.getAsString().equals("")) {
            String text = textElement.getAsString();
            if (text.startsWith("dire:")) {
                objectToSend = new TtsCommand(text.replace("dire:", ""));
            } else {
                FileData matchingFileData = fileDataList.stream()
                        .filter(fileData -> text.contains(fileData.matches))
                        .findAny().orElse(null);
                if (matchingFileData != null) {
                    objectToSend = new LocalSoundCommand(matchingFileData.fileName);
                }
            }
        }

        if (objectToSend != null) {
            commandController.broadcast(objectToSend);
        }
    }

    @PostConstruct
    public void init() {
        fileDataList = fileDataRepository.findAll();
    }

}

package fr.thomah.rogerserver.controllers;

import com.github.seratch.jslack.api.rtm.RTMMessageHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.thomah.rogerserver.commands.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SlackMessageHandler implements RTMMessageHandler {

    @Autowired
    private SimpMessagingTemplate template;

    private String slackGrafanaBotId = System.getenv("SLACK_GRAFANA_BOT_ID");

    @Override
    public void handle(String message) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        Object objectToSend = null;

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
                    objectToSend = new Command("/tts")
                            .addParam("text", title)
                            .addParam("voice", "1")
                            .addParam("nocache", "0")
                            .addParam("mute", "0");
                }
            }
        }

        // If there is a text message
        JsonElement textElement = jsonObject.get("text");
        if (textElement != null && !textElement.getAsString().equals("")) {
            String text = textElement.getAsString();
            if (text.startsWith("dire:")) {
                objectToSend = new Command("/tts")
                        .addParam("text", text.replace("dire:", ""))
                        .addParam("voice", "1")
                        .addParam("nocache", "0")
                        .addParam("mute", "0");
            }
        }

        if (objectToSend != null) {
            this.template.convertAndSend("/command", objectToSend);
        }
    }

}

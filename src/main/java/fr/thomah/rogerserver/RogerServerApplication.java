package fr.thomah.rogerserver;

import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.model.User;
import com.github.seratch.jslack.api.rtm.*;
import com.google.gson.JsonParser;
import fr.thomah.rogerserver.controllers.SlackController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class RogerServerApplication {

	private static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
	private static final String SLACK_TOKEN = System.getenv("SLACK_TOKEN");
	private static final String SLACK_CHANNEL = System.getenv("SLACK_CHANNEL");

	@Autowired
	private static SlackController slackController;

	public static void main(String[] args) {
		JsonParser jsonParser = new JsonParser();
		Slack slack = Slack.getInstance();
		RTMClient rtm;
		try {
			rtm = new Slack().rtm(SLACK_BOT_TOKEN);
			User botUser = rtm.getConnectedBotUser();
			slack.methods().channelsInvite(req -> req
					.token(SLACK_TOKEN)
					.channel(SLACK_CHANNEL)
					.user(botUser.getId())
			);
			rtm.addMessageHandler((message) -> {
				slackController.processRtm(jsonParser.parse(message).getAsJsonObject());
			});
			rtm.connect();
		} catch (IOException | DeploymentException | SlackApiException e) {
			e.printStackTrace();
		}
		SpringApplication.run(RogerServerApplication.class, args);
	}

}

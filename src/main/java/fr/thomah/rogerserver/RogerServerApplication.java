package fr.thomah.rogerserver;

import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.model.User;
import com.github.seratch.jslack.api.rtm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class RogerServerApplication {

	public static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
	private static final String SLACK_TOKEN = System.getenv("SLACK_TOKEN");
	private static final String SLACK_CHANNEL = System.getenv("SLACK_CHANNEL");

	@Autowired
	private SlackMessageHandler slackMessageHandler;

	public static void main(String[] args) {
		SpringApplication.run(RogerServerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		Slack slack = Slack.getInstance();
		RTMClient rtm;
		try {
			rtm = slack.rtm(SLACK_BOT_TOKEN);
			User botUser = rtm.getConnectedBotUser();
			slack.methods().channelsInvite(req -> req
					.token(SLACK_TOKEN)
					.channel(SLACK_CHANNEL)
					.user(botUser.getId())
			);
			rtm.addMessageHandler(slackMessageHandler);
			rtm.connect();
		} catch (IOException | DeploymentException | SlackApiException e) {
			e.printStackTrace();
		}
	}

}

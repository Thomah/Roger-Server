package fr.thomah.rogerserver;

import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.model.User;
import com.github.seratch.jslack.api.rtm.*;
import fr.thomah.rogerserver.controllers.SlackMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class RogerServerApplication {

	private static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
	private static final String SLACK_TOKEN = System.getenv("SLACK_TOKEN");
	private static final String SLACK_CHANNEL = System.getenv("SLACK_CHANNEL");
    private static final String SLACK_GRAFANA_BOT_ID = System.getenv("SLACK_GRAFANA_BOT_ID");

	@Autowired
	private SlackMessageHandler slackMessageHandler;

	public static void main(String[] args) {
		SpringApplication.run(RogerServerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
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
			slackMessageHandler.setGrafanaBotId(SLACK_GRAFANA_BOT_ID);
			rtm.addMessageHandler(slackMessageHandler);
			rtm.connect();
		} catch (IOException | DeploymentException | SlackApiException e) {
			e.printStackTrace();
		}
	}

}

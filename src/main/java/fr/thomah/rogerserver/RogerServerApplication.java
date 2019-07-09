package fr.thomah.rogerserver;

import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.rtm.*;
import com.github.seratch.jslack.api.rtm.message.Message;
import com.github.seratch.jslack.api.rtm.message.Typing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.thomah.rogerserver.controllers.SlackController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class RogerServerApplication {

	@Autowired
	private SlackController slackController;

	public static void main(String[] args) {

		JsonParser jsonParser = new JsonParser();
		String token = System.getenv("SLACK_TOKEN");
		System.out.println(token);

		RTMClient rtm;
		try {
			rtm = new Slack().rtm(token);
			rtm.addMessageHandler((message) -> {
				JsonObject json = jsonParser.parse(message).getAsJsonObject();
				System.out.println(json.toString());
				if (json.get("type") != null) {
					System.out.println("Handled type: " + json.get("type").getAsString());
				}
			});
			rtm.connect();
			rtm.sendMessage(Message.builder()
					.id(System.currentTimeMillis())
					.channel("DK0US0F2N")
					.text("Hi!")
					.build().toJSONString());
		} catch (IOException | DeploymentException e) {
			e.printStackTrace();
		}
		SpringApplication.run(RogerServerApplication.class, args);
	}

}

package fr.thomah.rogerserver;

import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.rtm.*;
import com.github.seratch.jslack.api.rtm.message.Message;
import com.github.seratch.jslack.api.rtm.message.Typing;
import com.google.gson.*;

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
		String token = System.getenv("xoxb-658375329392-658400491216-fRoDBsuKhc75ba0GzMbnQg9k");

		try (RTMClient rtm = new Slack().rtm(token)) {

			rtm.addMessageHandler((message) -> {
				JsonObject json = jsonParser.parse(message).getAsJsonObject();
				if (json.get("type") != null) {
					System.out.println("Handled type: " + json.get("type").getAsString());
				}
			});

			RTMMessageHandler handler2 = (message) -> {
				System.out.println("Hello " + message);
			};

			rtm.addMessageHandler(handler2);

			// must connect within 30 seconds after issuing wss endpoint
			rtm.connect();

			rtm.sendMessage(Typing.builder()
					.id(System.currentTimeMillis())
					.channel("DK0US0F2N")
					.build().toJSONString());

			rtm.sendMessage(Message.builder()
					.id(System.currentTimeMillis())
					.channel("DK0US0F2N")
					.text("Hi!")
					.build().toJSONString());

			rtm.removeMessageHandler(handler2);

		} // #close method does #disconnect
		catch (IOException | DeploymentException e) {
			e.printStackTrace();
		}
		SpringApplication.run(RogerServerApplication.class, args);
	}

}

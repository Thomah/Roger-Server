package fr.thomah.rogerserver.entities;

public class TtsCommand extends Command {

    public TtsCommand(String text) {
        super("/tts");
        this.addParam("text", text)
                .addParam("voice", "1")
                .addParam("nocache", "0")
                .addParam("mute", "0");
    }

}

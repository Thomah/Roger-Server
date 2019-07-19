package fr.thomah.rogerserver.entities;

public class LocalSoundCommand extends Command {

    public LocalSoundCommand(String fileName) {
        super("/sound");
        this.addParam("url", "<CLIENT_URL>/files/" + fileName.replaceAll(" ", "%20"));
    }
}

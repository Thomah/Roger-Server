package fr.thomah.rogerserver.commands;

public class Tts {

    public String text;

    public String voice;

    public String nocache;

    public String mute;

    public Tts(String text, String voice, String nocache, String mute) {
        this.text = text;
        this.voice = voice;
        this.nocache = nocache;
        this.mute = mute;
    }
}

package fr.thomah.rogerserver.entities;

public class EarsCommand extends Command {

    public EarsCommand(String left, String right, String noreset) {
        super("/ears");
        this.addParam("left", left)
                .addParam("right", right)
                .addParam("noreset", noreset);
    }

}

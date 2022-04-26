package models.buildings;

import models.Civilization;
import models.Output;

public class Palace {
    private Civilization owner;
    private final Output output;

    public Palace(Civilization owner) {
        this.owner = owner;
        this.output = new Output(2, 0,2);
    }

    public Civilization getOwner() {
        return owner;
    }

    public void setOwner(Civilization owner) {
        this.owner = owner;
    }

    public Output getOutput() {
        return output;
    }
}

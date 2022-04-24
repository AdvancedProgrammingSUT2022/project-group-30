package models.buildings;

import models.Civilization;
import models.Output;

public class Palace {
    private Civilization owner;
    private Output output;

    public Palace(Civilization owner, Output output) {
        this.owner = owner;
        this.output = output;
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

    public void setOutput(Output output) {
        this.output = output;
    }

}

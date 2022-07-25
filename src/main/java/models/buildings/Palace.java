package models.buildings;

import models.Civilization;
import models.Output;

public class Palace extends Building  implements java.io.Serializable{
    private final Civilization owner;
    private final Output output;

    public Palace(Civilization owner) {
        super(BuildingType.PALACE);
        this.owner = owner;
        this.output = new Output(2, 0, 2);
    }

    public Civilization getOwner() {
        return owner;
    }

    public Output getOutput() {
        return output;
    }
}

package models.improvements;

import models.Civilization;
import models.Output;

public class Improvement {
    private final int id;
    public  int getId() {
        return id;
    }

    private static int nextAvailableId = 0;

    private final ImprovementType type;
    private transient final Civilization founder;

    private boolean isPillaged;

    public Improvement(ImprovementType type, Civilization founder) {
        this.id = nextAvailableId;
        nextAvailableId++;

        this.type = type;
        this.founder = founder;
        isPillaged = false;
    }

    public Improvement createImage() {
        Improvement image = new Improvement(type, founder);
        image.isPillaged = isPillaged;
        return image;
    }

    public ImprovementType getType() {
        return this.type;
    }

    public Civilization getFounder() {
        return this.founder;
    }

    public boolean getIsPillaged() {
        return isPillaged;
    }

    public void setIsPillaged(boolean isPillaged) {
        this.isPillaged = isPillaged;
    }

    public String getName(){
        return type.getName();
    }

    public Output getOutput(){
        return type.getOutput();
    }
}

package models.improvements;

import models.Civilization;

public class Improvement {
    private final ImprovementType type;
    private final Civilization founder;
    
    private boolean isPillaged;

    public Improvement(ImprovementType type, Civilization founder) {
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

    public boolean isPillaged() {
        return isPillaged;
    }

    public void setIsPillaged(boolean isPillaged) {
        this.isPillaged = isPillaged;
    }
}

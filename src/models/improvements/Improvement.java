package models.improvements;

import models.Civilization;

public class Improvement {
    private final ImprovementType type;
    private final Civilization founder;

    public Improvement(ImprovementType type, Civilization founder) {
        this.type = type;
        this.founder = founder;
    }

    public ImprovementType getType() {
        return this.type;
    }

    public Civilization getFounder() {
        return this.founder;
    }
}

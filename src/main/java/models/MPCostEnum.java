package models;

import models.interfaces.MPCostInterface;

public enum MPCostEnum implements MPCostInterface, java.io.Serializable{
    IMPASSABLE(),
    EXPENSIVE();
}

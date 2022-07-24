package models;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;
import models.interfaces.MPCostInterface;

public enum MPCostEnum implements MPCostInterface, EnumInterface {
    @SerializedName("Enum models.MPCostEnum Impassable")
    IMPASSABLE("Impassable"),
    @SerializedName("Enum models.MPCostEnum Expensive")
    EXPENSIVE("Expensive");

    private String name;
    private MPCostEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

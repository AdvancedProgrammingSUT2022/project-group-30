package models;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;

public enum TileVisibility implements EnumInterface {
    @SerializedName("Enum models.TileVisibility Visible")
    VISIBLE("Visible"),
    @SerializedName("Enum models.TileVisibility Revealed")
    REVEALED("Revealed"),
    @SerializedName("Enum models.TileVisibility Fog of War")
    FOG_OF_WAR("Fog of War");

    private String name;

    TileVisibility(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
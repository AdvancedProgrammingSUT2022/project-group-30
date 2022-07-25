package models;

public enum TileVisibility  implements java.io.Serializable{
    VISIBLE("Visible"),
    REVEALED("Revealed"),
    FOG_OF_WAR("Fog of War");

    private String name;

    TileVisibility(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
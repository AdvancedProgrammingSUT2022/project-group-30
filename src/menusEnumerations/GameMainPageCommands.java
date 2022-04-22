package menusEnumerations;

public enum GameMainPageCommands {
    GET_TILE_INFO("\\d+\\s*[:,]\\s*\\d+"),
    SELECT_UNIT("select unit \\d+\\s*[:,]\\d+"),
    SELECT_CIVILIAN_UNIT("select civ(ilian unit)? \\d+\\s*[:,]\\d+"),
    ;


    private String regex;

    GameMainPageCommands(String regex) {
        this.regex = regex;
    }
}

package dev.federicobau.games.jbreakout.config;

public enum StartScreen {
    MAIN_MENU,
    GAMEPLAY;

    public static StartScreen fromString(String s) {
        if (s == null) return MAIN_MENU;

        try {
            return StartScreen.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MAIN_MENU;
        }
    }

}

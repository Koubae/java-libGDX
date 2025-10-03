package dev.federicobau.games.jbreakout.config;

public enum Environment {
    DEV,
    PROD;

    public static Environment fromString(String s) {
        if (s == null) return PROD;

        try {
            return Environment.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PROD;
        }
    }

}

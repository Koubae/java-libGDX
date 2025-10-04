package dev.federicobau.games.jbreakout.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.Properties;

public final class AppConfig {

    // -------------------------------------------------
    // Config values
    public final Environment environment;
    public final StartScreen startScreen;

    public final Boolean logStats;
    public final int logStatsIntervalSeconds;

    // -------------------------------------------------

    private final static String CONFIG_FILE = "config/config.properties";

    private AppConfig(Environment environment, StartScreen startScreen, Boolean logStats, int logStatsIntervalSeconds) {
        this.environment = environment;
        this.startScreen = startScreen;
        this.logStats = logStats;
        this.logStatsIntervalSeconds = logStatsIntervalSeconds;
    }

    public static AppConfig defaults() {
        return new AppConfig(Environment.PROD, StartScreen.MAIN_MENU, false, 2);
    }

    public static AppConfig load() {
        Properties p = new Properties();

        // 1) Base config.properties from assets (works on all targets)
        loadFromAssets(p);

        // 2) Optional env-specific override from assets
        String envHint = firstNonBlank(
            System.getProperty("environment"),
            System.getenv("GAME_ENV"),
            p.getProperty("environment")
        );

        // 3) System props / env vars override
        overrideIfPresent(p, "environment", firstNonBlank(p.getProperty("environment"), System.getProperty("environment"), System.getenv("GAME_ENV")));
        overrideIfPresent(p, "startScreen", firstNonBlank(p.getProperty("startScreen"), System.getProperty("startScreen"), System.getenv("GAME_START_SCREEN")));
        overrideIfPresent(p, "logStats", firstNonBlank(p.getProperty("logStats"), System.getProperty("logStats"), System.getenv("GAME_LOG_STATS")));

        Environment environment  = Environment.fromString(p.getProperty("environment"));
        StartScreen startScreen = StartScreen.fromString(p.getProperty("startScreen"));
        Boolean logStats = Boolean.valueOf(p.getProperty("logStats"));
        int logStatsIntervalSeconds = Integer.parseInt(p.getProperty("logStatsIntervalSeconds"));

        return new AppConfig(
            environment,
            startScreen,
            logStats,
            logStatsIntervalSeconds
        );
    }


    private static void loadFromAssets(Properties p) {
        try {
            FileHandle fh = Gdx.files.internal(AppConfig.CONFIG_FILE);
            if (fh.exists()) {
                Properties tmp = new Properties();
                tmp.load(fh.read());
                p.putAll(tmp);
                Gdx.app.log("AppConfig", String.format("config.properties '%s' Loaded", AppConfig.CONFIG_FILE));
            } else {
                Gdx.app.log("AppConfig", String.format("config.properties '%s' not found in assets, using defaults", AppConfig.CONFIG_FILE));
            }
        } catch (Exception e) {
            Gdx.app.error("AppConfig", "Unable to load config.properties", e);
        }
    }

    private static void overrideIfPresent(Properties p, String key, String val) {
        if (val != null && !val.isBlank()) {
            p.setProperty(key, val);
        }
    }

    private static String firstNonBlank(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }

}

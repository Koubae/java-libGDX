package dev.federicobau.games.jbreakout.config;

import com.badlogic.gdx.graphics.Color;

public final class UIConstants {
    private UIConstants() {
        throw new AssertionError("UIConstants is a static class");
    }
    // Game
    public static final String GAME_TITLE = "JBreakout";

    // Title
    public static final int TITLE_FONT_SIZE = 120;
    public static final Color TITLE_COLOR = Color.GOLDENROD;
    public static final Color TITLE_SHADOW_COLOR = Color.GREEN;

    // Buttons
    public static final float BUTTON_WIDTH = 200f;
    public static final float BUTTON_HEIGHT = 60f;
    public static final float BUTTON_MARGIN_Y = 20f;

    // Paddle
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final int PADDLE_WIDTH = 150;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_Y_POSITION = 60;

}

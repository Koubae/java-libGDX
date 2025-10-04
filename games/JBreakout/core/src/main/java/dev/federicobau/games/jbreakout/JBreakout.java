package dev.federicobau.games.jbreakout;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import dev.federicobau.games.jbreakout.config.AppConfig;
import dev.federicobau.games.jbreakout.config.StartScreen;
import dev.federicobau.games.jbreakout.screen.GameScreen;
import dev.federicobau.games.jbreakout.screen.MainMenuScreen;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class JBreakout extends Game {
    public ShapeRenderer renderer;
    public SpriteBatch batch;
    public BitmapFont font;
    private FreeTypeFontGenerator generator;

    // Camera
    public OrthographicCamera camera;
    public Viewport viewport;

    private AppConfig config;

    // Game State
    private float delta;
    private float deltaLogStats;

    @Override
    public void create() {
        config = AppConfig.load();
        config.setLogLevel();

        // Create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        viewport = new ScreenViewport(camera);
        viewport.setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/honk.ttf"));

        Screen startScreen;
        switch (this.config.startScreen) {
            case StartScreen.GAMEPLAY:
                startScreen = new GameScreen(this);
                break;
            case StartScreen.MAIN_MENU:
            default:
                startScreen = new MainMenuScreen(this);
                break;
        }

        this.switchScreenAndClosePrevious(startScreen);

        Gdx.app.debug("JBreakout", "Application created");
    }

    @Override
    public void render() {
        input();

        delta = Gdx.graphics.getDeltaTime();

        if (config.logStats) {
            _debugLogStats();
        }


        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        viewport.setWorldSize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();

        renderer.dispose();
        batch.dispose();
        font.dispose();
        generator.dispose();

        Screen screen = this.getScreen();
        if (screen != null) { // clear possible left-over screens
            screen.dispose();
        }
    }

    public FreeTypeFontGenerator getGenerator() {
        return generator;
    }

    public void quit(String source) {
        Gdx.app.log("JBreakout", String.format("Quitting (source=%s) ...", source));
        Screen previous = this.getScreen();
        if (previous != null) {
            previous.dispose(); // only if you don’t go back to it
        }

        Gdx.app.exit();

    }

    public void switchScreenAndClosePrevious(Screen screen) {
        Screen previous = this.getScreen();
        if (previous != null) {
            Gdx.app.debug(
                "JBreakout",
                String.format("Disposing previous screen '%s'", previous.getClass().getSimpleName())
            );
            previous.dispose(); // only if you don’t go back to it
        }

        this.setScreen(screen); // triggers old.hide(), next.show()
    }

    public void switchScreenAndHidePrevious(Screen screen) {
        Screen previous = this.getScreen();
        if (previous != null) {
            Gdx.app.debug(
                "JBreakout",
                String.format("Hiding previous screen '%s'", previous.getClass().getSimpleName())
            );
            previous.hide();
        }
        this.setScreen(screen);
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            _switchFullScreenMode();
        }
    }

    private void _switchFullScreenMode() {
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        Boolean fullScreen = Gdx.graphics.isFullscreen();
        Boolean fullScreenNew = !fullScreen;

        Gdx.app.log(
            "JBreakout",
            String.format("Switching fullscreen mode from '%s' to '%s'", fullScreen, fullScreenNew)
        );

        if (fullScreenNew) {
            Gdx.graphics.setFullscreenMode(currentMode);
        } else {
            int windowWidth = (int) (currentMode.width * 0.8f);  // Set it to 80%
            int windowHeight = (int) (currentMode.height * 0.8f);
            Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
        }
    }

    private void _debugLogStats() {
        deltaLogStats += delta;

        boolean shouldLog = (int) (deltaLogStats) != 0 && (int) (deltaLogStats) % config.logStatsIntervalSeconds == 0;
        if (!shouldLog) {
            return;
        }

        deltaLogStats = 0.0f;
        long javaHeap = Gdx.app.getJavaHeap();
        long nativeHeap = Gdx.app.getNativeHeap();
        int frames = Gdx.graphics.getFramesPerSecond();
        String stats = String.format("STATS: Java heap: %d, Native heap: %d, FPS: %d", javaHeap, nativeHeap, frames);
        Gdx.app.log("JBreakout", stats);
    }

}

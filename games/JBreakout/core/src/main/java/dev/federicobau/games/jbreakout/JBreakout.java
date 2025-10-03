package dev.federicobau.games.jbreakout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JBreakout extends Game {
    public ShapeRenderer renderer;
    public SpriteBatch batch;
    public BitmapFont font;
    private FreeTypeFontGenerator generator;

    // Camera
    public OrthographicCamera camera;
    public Viewport viewport;

    private AppConfig config;

    @Override
    public void create() {
        config = AppConfig.load();

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
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
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
        this.setScreen(screen); // triggers old.hide(), next.show()
        if (previous != null) {
            Gdx.app.log("JBreakout", String.format("Disposing previous screen '%s'", previous.getClass().getSimpleName()));
            previous.dispose(); // only if you don’t go back to it
        }
    }
}

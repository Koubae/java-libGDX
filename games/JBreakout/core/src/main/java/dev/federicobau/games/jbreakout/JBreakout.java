package dev.federicobau.games.jbreakout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.federicobau.games.jbreakout.screen.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JBreakout extends Game {
    public ShapeRenderer renderer;
    public SpriteBatch batch;
    public BitmapFont font;

    // Camera
    public OrthographicCamera camera;
    public Viewport viewport;

    @Override
    public void create() {
        // Create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        // Create viewport (handles different screen sizes)
        viewport = new ScreenViewport(camera);

        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        this.switchScreenAndClosePrevious(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        renderer.dispose();
        batch.dispose();
        font.dispose();

        Screen screen = this.getScreen();
        if (screen != null) { // clear possible left-over screens
            screen.dispose();
        }
    }

    public void quit(String source) {
        Gdx.app.log("JBreakout", String.format("Quitting (source=%s) ...", source));
        Screen previous = this.getScreen();
        if (previous != null) {
            previous.dispose(); // only if you don’t go back to it
        }

        Gdx.app.exit();

    }

    private void switchScreenAndClosePrevious(Screen screen) {
        Screen previous = this.getScreen();
        this.setScreen(screen); // triggers old.hide(), next.show()
        if (previous != null) {
            previous.dispose(); // only if you don’t go back to it
        }
    }
}

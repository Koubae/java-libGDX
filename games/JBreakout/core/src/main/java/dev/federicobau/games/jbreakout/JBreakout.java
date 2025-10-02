package dev.federicobau.games.jbreakout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }
}

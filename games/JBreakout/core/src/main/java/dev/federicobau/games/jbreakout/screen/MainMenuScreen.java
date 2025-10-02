package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.federicobau.games.jbreakout.JBreakout;

public class MainMenuScreen implements Screen {
    final JBreakout game;

    public MainMenuScreen(JBreakout game) {
        this.game = game;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        game.renderer.setColor(1, 0, 0, 1); // Red
        game.renderer.rect(100, 100, 200, 50);

        game.renderer.end();

        // Draw text
        game.batch.begin();
        game.font.draw( game.batch, "Hello World!", 100, 200);
        game.batch.end();


    }

    @Override
    public void resize(int width, int height) {
//        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

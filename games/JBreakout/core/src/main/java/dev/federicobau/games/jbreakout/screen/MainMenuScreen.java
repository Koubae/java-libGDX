package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        input();

        ScreenUtils.clear(Color.BLACK);

        // Update camera
        game.camera.update();

        // Apply camera to renderers
        game.renderer.setProjectionMatrix(game.camera.combined);
        game.batch.setProjectionMatrix(game.camera.combined);


        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        game.renderer.setColor(1, 0, 0, 1); // Red
        game.renderer.rect(100, 100, 200, 50);
        game.renderer.rect(screenWidth / 2, screenHeight / 2, 200, 50);

        game.renderer.end();

        // Draw text
        game.batch.begin();

        game.font.draw( game.batch, "Hello World!", 100, 200);
        game.font.draw(game.batch, "Screen: " + screenWidth + "x" + (int)screenHeight, 400, 200);

        game.batch.end();

    }



    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
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

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

}

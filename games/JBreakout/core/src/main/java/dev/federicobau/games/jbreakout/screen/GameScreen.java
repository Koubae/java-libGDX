package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.federicobau.games.jbreakout.JBreakout;
import dev.federicobau.games.jbreakout.config.UIConstants;
import dev.federicobau.games.jbreakout.entities.Paddle;
import dev.federicobau.games.jbreakout.entities.Ball;

public class GameScreen implements Screen {
    final JBreakout game;

    private final Paddle paddle;
    private final Ball ball;

    public GameScreen(JBreakout game) {
        this.game = game;

        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();
        this.paddle = new Paddle(
            (screenWidth / 2) - 150,
            UIConstants.PADDLE_Y_POSITION,
            UIConstants.PADDLE_WIDTH,
            UIConstants.PADDLE_HEIGHT
        );

        this.ball = new Ball(
            screenWidth / 2,
            screenHeight / 2,
            UIConstants.BALL_SIZE,
            UIConstants.BALL_SPEED
        );

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();

        ScreenUtils.clear(Color.BLACK);
        // Update camera
        game.camera.update();
        // Apply camera to renderers
        game.renderer.setProjectionMatrix(game.camera.combined);
        game.batch.setProjectionMatrix(game.camera.combined);

        // ····················
        // Shape Renderer
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        paddle.update(delta);
        paddle.draw(game.renderer);

        ball.update(delta, paddle, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ball.draw(game.renderer);

        game.renderer.end();
        // ^^^^^^^^^^^^^^^^^^^^

        // ····················
        // Sprite + Text Renderer
        game.batch.begin();

        game.batch.end();
        // ^^^^^^^^^^^^^^^^^^^^

    }

    @Override
    public void resize(int width, int height) {

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

    // ---------------------------
    // LOGIC
    // ---------------------------
    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.log("GameScreen", "Closing Game and go back to MainMenuScreen");
            game.switchScreenAndClosePrevious(new MainMenuScreen(game));
        }

    }

}

package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.federicobau.games.jbreakout.JBreakout;
import dev.federicobau.games.jbreakout.config.UIConstants;
import dev.federicobau.games.jbreakout.entities.Paddle;
import dev.federicobau.games.jbreakout.entities.Ball;

public class GameScreen implements Screen {
    final JBreakout game;

    private final Paddle paddle;
    private Ball ball;
    // Game State
    private int playerLives;
    private boolean gameOver;
    private BitmapFont livesTextFont;

    public GameScreen(JBreakout game) {
        this.game = game;

        // Game State
        this.playerLives = UIConstants.PLAYER_LIVES;
        this.gameOver = false;

        float screenWidth = game.viewport.getWorldWidth();
        this.paddle = new Paddle(
            (screenWidth / 2) - 150,
            UIConstants.PADDLE_Y_POSITION,
            UIConstants.PADDLE_WIDTH,
            UIConstants.PADDLE_HEIGHT
        );

        this.ball = _createNewBall();
    }

    @Override
    public void show() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = UIConstants.PLAYER_LIVES_TEXT_COLOR;
        parameter.size = UIConstants.PLAYER_LIVES_FONT_SIZE;
        parameter.borderWidth = 1;
        livesTextFont = game.getGenerator().generateFont(parameter);

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
        livesTextFont.dispose();
    }

    @Override
    public void render(float delta) {
        gameState();
        input();

        ScreenUtils.clear(Color.BLACK);
        // Update camera
        game.camera.update();
        // Apply camera to renderers
        game.renderer.setProjectionMatrix(game.camera.combined);
        game.batch.setProjectionMatrix(game.camera.combined);

        _draw(delta);

    }

    // ---------------------------
    // LOGIC
    // ---------------------------
    private void gameState() {
        if (playerLives <= 0) {
            gameOver = true;
        }
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.log("GameScreen", "Closing Game and go back to MainMenuScreen");
            game.switchScreenAndClosePrevious(new MainMenuScreen(game));
        }

    }

    private void _draw(float delta) {
        if (gameOver) {
            _drawGameOver();
            return;
        }
        float screenHeight = game.viewport.getWorldHeight();

        // ····················
        // Shape Renderer
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        _drawGamePlay(delta);

        game.renderer.end();
        // ^^^^^^^^^^^^^^^^^^^^

        // ····················
        // Sprite + Text Renderer
        game.batch.begin();

        livesTextFont.draw(
            game.batch,
            new GlyphLayout(livesTextFont, String.format("Life %d", playerLives)),
            50,
            screenHeight - 50
        );

        game.batch.end();
        // ^^^^^^^^^^^^^^^^^^^^

    }

    private void _drawGamePlay(float delta) {
        paddle.update(delta);
        paddle.draw(game.renderer);

        ball.update(delta, paddle, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (ball.isDestroyed()) {
            this.playerLives -= 1;
            this.ball = _createNewBall();
        } else {
            ball.update(delta, paddle, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            ball.draw(game.renderer);
        }
    }

    // TODO: create a NEW screen instead!
    private void _drawGameOver() {
        Gdx.app.log("GameScreen", "Closing Game and go back to MainMenuScreen");
        game.switchScreenAndClosePrevious(new MainMenuScreen(game));
    }

    private Ball _createNewBall() {
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();
        return new Ball(
            screenWidth / 2,
            screenHeight / 2,
            UIConstants.BALL_SIZE,
            UIConstants.BALL_SPEED,
            true
        );
    }

}

package dev.federicobau.games.jbreakout.screen;

import java.util.ArrayList;
import java.nio.file.Path;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.federicobau.games.jbreakout.JBreakout;
import dev.federicobau.games.jbreakout.config.UIConstants;
import dev.federicobau.games.jbreakout.entities.Block;
import dev.federicobau.games.jbreakout.entities.Paddle;
import dev.federicobau.games.jbreakout.entities.Ball;


public class GameScreen implements Screen {
    final JBreakout game;

    private final Paddle paddle;
    private Ball ball;
    private ArrayList<Block> blocks;

    // Game State
    private int score;
    private int playerLives;
    private boolean gameOver;
    private BitmapFont livesTextFont;

    // Sounds Effect & Music
    Sound ballBouncePaddleSound;
    Sound blockDestroyedSound;

    public GameScreen(JBreakout game) {
        this.game = game;

        // Sounds Effect & Music
        ballBouncePaddleSound = Gdx.audio.newSound(
            Gdx.files.internal(String.valueOf(Path.of("sounds", UIConstants.BALL_BOUNCE_SOUND_PADDLE))));
        blockDestroyedSound = Gdx.audio.newSound(
            Gdx.files.internal(String.valueOf(Path.of("sounds", UIConstants.BLOCK_DESTROY_SOUND))));

        // Game State
        this.score = 0;
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
        this.blocks = new ArrayList<>();
        _createBlocks();
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
        ballBouncePaddleSound.dispose();
        blockDestroyedSound.dispose();
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
            100
        );
        livesTextFont.draw(
            game.batch,
            new GlyphLayout(livesTextFont, String.format("Score %d", score)),
            50,
            150
        );

        game.batch.end();
        // ^^^^^^^^^^^^^^^^^^^^

    }

    private void _drawGamePlay(float delta) {
        paddle.update(delta);
        paddle.draw(game.renderer);

        boolean collided = ball.update(delta, paddle, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (collided) {
            System.out.println("Ball bounced and collided with paddle " + delta);
            ballBouncePaddleSound.play();
        }
        if (ball.isDestroyed()) {
            this.playerLives -= 1;
            this.ball = _createNewBall();
        } else {
            ball.update(delta, paddle, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            ball.draw(game.renderer);
        }

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.isDestroyed()) {
                blocks.remove(i);
                i--;
                continue;
            }

            block.draw(game.renderer);
            if (ball.blockHitCheck(block)) {
                blockDestroyedSound.play();
                score += 1;
            }
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

    private void _createBlocks() {
        float width = game.viewport.getWorldWidth();
        float height = game.viewport.getWorldHeight();
        int blockWidth = (int) (width / UIConstants.BLOCK_WIDTH);
        int blockHeight = UIConstants.BLOCK_HEIGHT;
        int margin = UIConstants.BLOCK_MARGIN;

        int yStart = (int) (height / 2);
        int yEnd = (int) (height - blockHeight - margin);
        int yNext = blockHeight + margin;

        int xStart = (margin * 2);
        int xEnd = (int) ((width - blockWidth - margin));
        int xNext = blockWidth + margin;

        for (int y = yStart; y < yEnd; y += yNext) {
            for (int x = xStart; x < xEnd; x += xNext) {
                blocks.add(new Block(x, y, blockWidth, blockHeight));
            }
        }
    }

}

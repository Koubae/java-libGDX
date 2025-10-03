package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.federicobau.games.jbreakout.JBreakout;

public class MainMenuScreen implements Screen {
    final JBreakout game;


    private Stage stage;
    private Skin skin;

    private TextButton btnStart;
    private TextButton btnQuit;
    private TextButton btnSettings;


    public MainMenuScreen(JBreakout game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(game.viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        btnStart = new TextButton("New Game", skin, "green");
        btnSettings = new TextButton("Settings", skin);
        btnQuit = new TextButton("Exit", skin, "grey");

        btnStart.setSize(200, 60);
        btnSettings.setSize(200, 60);
        btnQuit.setSize(200, 60);

        this.setBtnPositions();

        stage.addActor(btnStart);
        stage.addActor(btnSettings);
        stage.addActor(btnQuit);

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Button Start clicked!");
                // Here you can switch to another screen, e.g.:
                // game.setScreen(new GameScreen());
            }
        });

        btnSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Button Setting clicked!");
                // Here you can switch to another screen, e.g.:
                // game.setScreen(new GameScreen());
            }
        });

        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Button Quit clicked!");
                // Here you can switch to another screen, e.g.:
                // game.setScreen(new GameScreen());

                game.quit("MainMenuScreen Button Quit clicked!");
            }
        });


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

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        this.setBtnPositions();
    }

    private void setBtnPositions() {
        float w = stage.getViewport().getWorldWidth();
        float h = stage.getViewport().getWorldHeight();

        float btnX = w / 2f - btnStart.getWidth() / 2f;
        float btnY = h / 2f - btnStart.getHeight() / 2f + 100f;
        float marginY = 20f;
        btnStart.setPosition(btnX, btnY);
        btnSettings.setPosition(btnX, btnY - btnStart.getHeight() - marginY);
        btnQuit.setPosition(btnX, btnY - (btnStart.getHeight() * 2f) - (marginY * 2f));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
        // Optional if you want to free memory but keep the screen alive:
        // stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.quit("MainMenuScreen Escape pressed");
        }

    }

}

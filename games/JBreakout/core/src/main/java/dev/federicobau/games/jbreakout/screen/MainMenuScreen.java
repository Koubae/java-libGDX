package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import dev.federicobau.games.jbreakout.JBreakout;
import dev.federicobau.games.jbreakout.config.UIConstants;
import dev.federicobau.games.jbreakout.entities.Paddle;


public class MainMenuScreen implements Screen {
    final JBreakout game;

    private Stage stage;
    Music music;
    private Skin skin;

    private BitmapFont titleFont;
    private GlyphLayout titleFontLayout = new GlyphLayout();

    private TextButton btnStart;
    private TextButton btnQuit;
    private TextButton btnSettings;

    private final Paddle paddle;

    public MainMenuScreen(JBreakout game) {
        this.game = game;
        this.paddle = new Paddle(
            0,
            UIConstants.PADDLE_Y_POSITION,
            UIConstants.PADDLE_WIDTH,
            UIConstants.PADDLE_HEIGHT
        );

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/relent-1.mp3"));
        music.setLooping(true);
        music.setVolume(1F);
    }

    @Override
    public void show() {
        music.play();

        stage = new Stage(game.viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Title
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = UIConstants.TITLE_COLOR;
        parameter.size = UIConstants.TITLE_FONT_SIZE;
        parameter.shadowColor = UIConstants.TITLE_SHADOW_COLOR;
        parameter.shadowOffsetX = 5;
        parameter.shadowOffsetY = 1;
        parameter.borderWidth = 3;
        titleFont = game.getGenerator().generateFont(parameter);
        titleFontLayout = new GlyphLayout(titleFont, UIConstants.GAME_TITLE);

        btnStart = new TextButton("New Game", skin, "green");
        btnSettings = new TextButton("Settings", skin);
        btnQuit = new TextButton("Exit", skin, "grey");

        btnStart.setSize(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT);
        btnSettings.setSize(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT);
        btnQuit.setSize(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT);

        // Table layout
        Table layoutBtn = new Table();
        layoutBtn.setFillParent(true);   // fills the stage; auto re-layout on resize
        layoutBtn.defaults()
            .width(UIConstants.BUTTON_WIDTH)
            .height(UIConstants.BUTTON_HEIGHT)
            .pad(UIConstants.BUTTON_MARGIN_Y);

        layoutBtn.center();
        layoutBtn.add(btnStart).row();
        layoutBtn.add(btnSettings).row();
        layoutBtn.add(btnQuit);
        stage.addActor(layoutBtn);

        _registerEventListeners();

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

        // ····················
        // Shape Renderer
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        paddle.setX((screenWidth / 2) - (int)(UIConstants.PADDLE_WIDTH / 2));
        paddle.draw(game.renderer);

        game.renderer.end();

        // ^^^^^^^^^^^^^^^^^^^^

        // ····················
        // Sprite + Text Renderer
        game.batch.begin();

        titleFont.draw(
            game.batch, titleFontLayout,
            (screenWidth - titleFontLayout.width) / 2f,
            screenHeight - 100
        );

        game.batch.end();
        // ^^^^^^^^^^^^^^^^^^^^

        // ····················
        // Stage Render
        stage.act(delta);
        stage.draw();
        // ^^^^^^^^^^^^^^^^^^^^

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        titleFont.dispose();
        music.dispose();
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.quit("MainMenuScreen Escape pressed");
        }
    }

    private void _registerEventListeners() {
        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Button Start clicked!");
                game.switchScreenAndClosePrevious(new GameScreen(game));
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

}

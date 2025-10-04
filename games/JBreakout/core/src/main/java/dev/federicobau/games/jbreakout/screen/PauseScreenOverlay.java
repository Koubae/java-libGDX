package dev.federicobau.games.jbreakout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import dev.federicobau.games.jbreakout.JBreakout;

public class PauseScreenOverlay implements Screen {
    private final JBreakout game;
    private final GameScreen previous;


    public PauseScreenOverlay(JBreakout game, GameScreen previous) {
        this.game = game;
        this.previous = previous;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        _draw(delta);
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
        // Called when PauseScreen is hidden (e.g., when returning to previous)
    }

    @Override
    public void dispose() {
        // Nothing allocated here; do NOT dispose previous.
    }

    private void _draw(float delta) {
        // Render the previous screen beneath the overlay. Use delta=0 to avoid advancing its timers.
        // This assumes the previous screen respects a paused/hidden state and won't advance game logic when hidden.
        try {
            previous.render(0f);
        } catch (Exception e) {
            Gdx.app.error("PauseScreen", "Warning rendering previous screen: " + e.getMessage(), e);
        }

        // Draw translucent overlay and "PAUSED" text
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 0); // don't clear colors; previous.render already drew the frame
        // Draw semi-transparent rectangle
        game.renderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0f, 0f, 0f, 0.5f));
        game.renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.renderer.end();

        // Draw "PAUSED" text
        game.batch.begin();
        GlyphLayout layout = new GlyphLayout(game.font, "PAUSED");
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f;
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, layout, x, y);
        game.batch.end();
    }


}

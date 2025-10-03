package dev.federicobau.games.jbreakout.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.federicobau.games.jbreakout.config.UIConstants;

public class Paddle {
    private float x;
    private final float y;
    private final float width;
    private final float height;

    Color color;

    public Paddle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        color = UIConstants.PADDLE_COLOR;
    }

    public void update(float delta) {
        this.setX(Gdx.input.getX() - (width / 2));
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(color);
        renderer.rect(x, y, width, height);
    }

    public void setX(float x) {
        this.x = x;
    }
}

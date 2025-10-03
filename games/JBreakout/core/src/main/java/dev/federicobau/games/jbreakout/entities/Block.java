package dev.federicobau.games.jbreakout.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.federicobau.games.jbreakout.config.UIConstants;

public class Block {
    private float x;
    private float y;
    private int width;
    private int height;
    private boolean destroyed = false;

    private Color color;

    public Block(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.color = UIConstants.BLOCK_COLOR;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.rect(x, y, width, height);
    }

    public void destroy() {
        color = UIConstants.BLOCK_DESTROYED_COLOR;
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

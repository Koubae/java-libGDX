package dev.federicobau.games.jbreakout.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.federicobau.games.jbreakout.config.UIConstants;

public class Ball {
    private float x;
    private float y;
    private final int size;

    private int xSpeed;
    private int ySpeed;

    private Color color;
    private boolean destroyed = false;

    public Ball(float x, float y, int size, int speed, boolean randomizeDirection) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = speed;
        this.ySpeed = speed;

        if (randomizeDirection) {
            float signX = ThreadLocalRandom.current().nextBoolean() ? 1f : -1f;
            this.xSpeed = (int) (speed * signX);
        }

        this.color = UIConstants.BALL_COLOR;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, size);
    }

    public boolean update(float delta, Paddle paddle, float worldWidth, float worldHeight) {
        // move
        x += xSpeed * delta;
        y += ySpeed * delta;

        boolean collided = collisionWithPaddle(paddle);
        if (collided) {
            color = UIConstants.BALL_COLOR_ON_HIT;
        } else {
            color = UIConstants.BALL_COLOR;
        }

        if (x < 0 || x > worldWidth - size) {
            reverseX();
        }

        if (y < 0 || y > worldHeight - size || collided) {
            reverseY();
        }

        if (collided) { // make sure the ball doesn't get stuck within the paddle so move above it!
            if ((y - size) <= paddle.getY()) {
                y = (paddle.getY() + (size * 2));
            }
        } else {
            this.destroyIfTouchOrNearBottom();
        }

        return collided;
    }

    private boolean collisionWithPaddle(Paddle paddle) {
        boolean touchLeft = (x + size) >= paddle.getX();
        boolean touchRight = (x - size) <= (paddle.getX() + paddle.getWidth());

        boolean touchTop = (y + size) >= paddle.getY();
        boolean touchBottom = (y - size) <= (paddle.getY() + paddle.getHeight());

        return touchLeft && touchRight && touchTop && touchBottom;

    }

    public boolean blockHitCheck(Block block) {
        boolean collided = ((x + size) >= block.getX())
            && (x - size) <= (block.getX() + block.getWidth())
            && ((y + size) >= block.getY())
            && (y - size) <= (block.getY() + block.getHeight());

        if (!collided) {
            return false;
        }

        block.destroy();
        reverseY();

        return true;

    }

    /**
     * Is the Ball near the bottom of the screen, and the Paddle canno't possibly bounce it back up
     * then we consider the Ball as destroyed!
     *
     */
    private void destroyIfTouchOrNearBottom() {
        if (y <= 0) {
            destroy();
        }
    }

    private void destroy() {
        color = UIConstants.BALL_COLOR_ON_HIT;
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private void reverseX() {
        xSpeed = -xSpeed;
    }

    private void reverseY() {
        ySpeed = -ySpeed;
    }

}

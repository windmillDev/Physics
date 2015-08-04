package ch.windmill.physics.core;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class Vector2D {
    private float x;
    private float y;

    public Vector2D(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void add(final Vector2D vector2D) {
        x += vector2D.getX();
        y += vector2D.getY();
    }
}

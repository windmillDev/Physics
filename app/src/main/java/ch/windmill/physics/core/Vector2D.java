package ch.windmill.physics.core;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class Vector2D {
    public float x;
    public float y;

    public Vector2D() {
        this(0,0);
    }

    public Vector2D(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public float length() {
        return (float) Math.sqrt((x*x + y*y));
    }

    public void normalize() {
        float len = length();
        x = x / len;
        y = y / len;
    }

    public static float dot(final Vector2D v1, final Vector2D v2) {
        return (v1.x * v2.x + v1.y * v2.y);
    }

    public static Vector2D add(final Vector2D v1, final Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2D sub(final Vector2D v1, final Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2D multiply(final Vector2D v, final float scalar) {
        return new Vector2D(v.x * scalar, v.y * scalar);
    }
}

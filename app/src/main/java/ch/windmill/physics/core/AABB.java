package ch.windmill.physics.core;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class AABB {
    private Vector2D min;
    private Vector2D max;
    private float velocity;
    private float angle;

    public AABB(final Vector2D min, final Vector2D max, final float velocity, final float angle) {
        this.min = min;
        this.max = max;
        this.velocity = velocity;
        this.angle = angle;
    }

    public void move(final Vector2D vector2D) {
        min.add(vector2D);
        max.add(vector2D);
    }

    public Vector2D getMin() {
        return min;
    }

    public void setMin(Vector2D min) {
        this.min = min;
    }

    public Vector2D getMax() {
        return max;
    }

    public void setMax(Vector2D max) {
        this.max = max;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}

package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jaunerc on 15.08.15.
 */
public abstract class Figure {
    public static int screenWidth, screenHeight;
    public final int pixelsPerMeter = 10;

    public Vector2D pos;
    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected long lastTimeMs, frameDuration;
    protected int mass;

    public Object LOCK = new Object();

    public Figure(final Vector2D pos) {
        this(pos, new Vector2D(0,0), new Vector2D(0,0));
    }

    public Figure(final Vector2D pos, final Vector2D velocity) {
        this(pos, velocity, new Vector2D(0,0));
    }

    public Figure(final Vector2D pos, final Vector2D velocity, final Vector2D acceleration) {
        this.pos = pos;
        this.velocity = velocity;
        this.acceleration = acceleration;
        // initial value for the time
        lastTimeMs = -1;
        // pseudo mass
        mass = 1;
    }

    public Vector2D getVelocity() {
        synchronized (LOCK) {
            return velocity;
        }
    }

    public void setVelocity(Vector2D velocity) {
        synchronized (LOCK) {
            this.velocity = velocity;
        }
    }

    public Vector2D getAcceleration() {
        synchronized (LOCK) {
            return acceleration;
        }
    }

    public void setAcceleration(Vector2D acceleration) {
        synchronized (LOCK) {
            this.acceleration = acceleration;
        }
    }

    public void updatePosition() {
        if(screenWidth <= 0 || screenHeight <= 0) {
            // invalid width and height, nothing to do until the GUI comes up
            return;
        }

        long curTime = System.currentTimeMillis();
        if(lastTimeMs < 0) {
            lastTimeMs = curTime;
            // first run, no old time measure available and no position change
            return;
        }

        frameDuration = curTime - lastTimeMs;
        lastTimeMs = curTime;

        // update the velocity
        velocity.x += ((frameDuration * acceleration.x) / 1000) * pixelsPerMeter;
        velocity.y += ((frameDuration * acceleration.y) / 1000) * pixelsPerMeter;

        // update the position
        pos.x += ((velocity.x * frameDuration) / 1000) * pixelsPerMeter;
        pos.y += ((velocity.y * frameDuration) / 1000) * pixelsPerMeter;
    }

    public abstract void collisionDetect(Rectangle rectangle);

    public abstract void collisionDetect(Circle circle);

    public abstract void draw(Canvas canvas, Paint paint);
}

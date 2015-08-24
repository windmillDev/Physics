package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Represents a basic figure. A figure has vectors for position, velocity and acceleration.
 *
 * Created by jaunerc on 15.08.15.
 */
public abstract class Figure {
    // these fields are public to easier access trough calculation
    public static int screenWidth, screenHeight;
    public final int pixelsPerMeter = 10;
    public Vector2D pos;

    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected long lastTimeMs, frameDuration;
    protected int mass;

    // object for synchronisation
    public Object LOCK = new Object();

    /**
     * Create a new figure. Velocity and Acceleration will be set to zero.
     * @param pos the position of the figure
     */
    public Figure(final Vector2D pos) {
        this(pos, new Vector2D(0,0), new Vector2D(0,0));
    }

    /**
     * Create a new figure. Acceleration will be set to zero.
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     */
    public Figure(final Vector2D pos, final Vector2D velocity) {
        this(pos, velocity, new Vector2D(0,0));
    }

    /**
     * Create a new figure.
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     * @param acceleration the acceleration of the figure
     */
    public Figure(final Vector2D pos, final Vector2D velocity, final Vector2D acceleration) {
        this.pos = pos;
        this.velocity = velocity;
        this.acceleration = acceleration;
        // initial value for the time
        lastTimeMs = -1;
        // pseudo mass
        mass = 1;
    }

    /**
     * Returns the velocity.
     * @return the velocity vector
     */
    public Vector2D getVelocity() {
        synchronized (LOCK) {
            return velocity;
        }
    }

    /**
     * Set a new velocity vector.
     * @param velocity vector
     */
    public void setVelocity(Vector2D velocity) {
        synchronized (LOCK) {
            this.velocity = velocity;
        }
    }

    /**
     * Returns the acceleration.
     * @return the acceleration vector
     */
    public Vector2D getAcceleration() {
        synchronized (LOCK) {
            return acceleration;
        }
    }

    /**
     * Set a new acceleration vector.
     * @param acceleration vector
     */
    public void setAcceleration(Vector2D acceleration) {
        synchronized (LOCK) {
            this.acceleration = acceleration;
        }
    }

    /**
     * Returns the mass.
     * @return the mass value
     */
    public int getMass() {
        synchronized (LOCK) {
            return mass;
        }
    }

    /**
     * Set the mass value.
     * @param mass new value
     */
    public void setMass(final int mass) {
        synchronized (LOCK) {
            this.mass = mass;
        }
    }

    /**
     * Updates the position vector based on the velocity and acceleration.
     */
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

    /**
     * Detect collision with a given rectangle figure.
     * @param rectangle figure
     */
    public abstract void collisionDetect(Rectangle rectangle);

    /**
     * Detect collision with a given circle figure.
     * @param circle
     */
    public abstract void collisionDetect(Circle circle);

    /**
     * Draw the figure on the given <code>android.graphics.Canvas</code>.
     * @param canvas to draw to
     * @param paint to draw with
     */
    public abstract void draw(Canvas canvas, Paint paint);
}

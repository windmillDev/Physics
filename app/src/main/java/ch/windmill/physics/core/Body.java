package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Represents a basic body. A body has vectors for position, velocity and acceleration.
 *
 * Created by jaunerc on 15.08.15.
 */
public abstract class Body {
    // these fields are public to easier access trough calculation
    public static int screenWidth, screenHeight;
    public Vector2D pos;

    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected MassData massData;

    // object for synchronisation
    public Object LOCK = new Object();

    /**
     * Create a new figure. Velocity and Acceleration will be set to zero.
     * @param pos the position of the figure
     */
    public Body(final Vector2D pos) {
        this(pos, new Vector2D(0,0), new Vector2D(0,0));
    }

    /**
     * Create a new figure. Acceleration will be set to zero.
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     */
    public Body(final Vector2D pos, final Vector2D velocity) {
        this(pos, velocity, new Vector2D(0,0));
    }

    /**
     * Create a new figure.
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     * @param acceleration the acceleration of the figure
     */
    public Body(final Vector2D pos, final Vector2D velocity, final Vector2D acceleration) {
        this.pos = pos;
        this.velocity = velocity;
        this.acceleration = acceleration;
        calcMass();
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
    public float getMass() {
        return massData.getMass();
    }

    public float getInverseMass() {
        return massData.getInv_mass();
    }

    /**
     * Set the mass value.
     * @param mass new value
     */
    public void setMass(final float mass) {
        massData = new MassData(0,1);
    }

    protected void calcMass() {
        massData = new MassData(3,1);
    }

    /**
     * Updates the position vector based on the velocity and acceleration.
     * @param frameDuration time value for the current movement
     */
    public void updatePosition(final long frameDuration) {
        if(screenWidth <= 0 || screenHeight <= 0) {
            // invalid width and height, nothing to do until the GUI comes up
            return;
        }

        // update the velocity
        velocity.x += ((frameDuration * acceleration.x) / 1000) * Engine.PIXELSPERMETER;
        velocity.y += ((frameDuration * acceleration.y) / 1000) * Engine.PIXELSPERMETER;

        // update the position
        pos.x += ((velocity.x * frameDuration) / 1000) * Engine.PIXELSPERMETER;
        pos.y += ((velocity.y * frameDuration) / 1000) * Engine.PIXELSPERMETER;
    }

    /**
     * Draw the figure on the given <code>android.graphics.Canvas</code>.
     * @param canvas to draw to
     * @param paint to draw with
     */
    public abstract void draw(Canvas canvas, Paint paint);
}

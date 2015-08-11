package ch.windmill.physics.core;


/**
 * Created by jaunerc on 05.08.2015.
 */
public class CircleModel {
    // the ball speed is meters / second. When we draw to the screen,
    // 1 pixel represents 1 meter. That ends up too slow, so multiply
    // by this number. Bigger numbers speeds things up.
    public final float pixelsPerMeter = 10;

    private final int circleRadius;

    // these are public, so make sure you synchronize on LOCK
    // when reading these. I made them public since you need to
    // get both X and Y in pairs, and this is more efficient than
    // getter methods. With two getters, you'd still need to
    // synchronize.
    public float circlePixelX, circlePixelY;

    private int pixelWidth, pixelHeight;

    // values are in meters/second
    private float velocityX, velocityY;

    // typical values range from -10...10, but could be higher or lower if
    // the user moves the phone rapidly
    private float accelX, accelY;

    private static final float rebound = 0.8f;

    // if the ball bounces and the velocity is less than this constant,
    // stop bouncing.
    private static final float STOP_BOUNCING_VELOCITY = 2f;

    private volatile long lastTimeMs = -1;

    public final Object LOCK = new Object();

    public CircleModel(final int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public void setAccel(final float ax, final float ay) {
        synchronized (LOCK) {
            accelX = ax;
            accelY = ay;
        }
    }

    public void setSize(final int width, final int height) {
        synchronized (LOCK) {
            pixelWidth = width;
            pixelHeight = height;
        }
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    /**
     * Call this to move the ball to a particular location on the screen. This
     * resets the velocity to zero, but the acceleration doesn't change so
     * the ball should start falling shortly.
     */
    public void moveCircle(final int circleX, final int circleY) {
        circlePixelX = circleX;
        circlePixelY = circleY;
        velocityX = 10;
        velocityY = 0;
    }

    public void updatePhysics() {
        float lWidth, lHeight, lCircleX, lCircleY, lAx, lAy, lVx, lVy;
        synchronized (LOCK) {
            lWidth = pixelWidth;
            lHeight = pixelHeight;
            lCircleX = circlePixelX;
            lCircleY = circlePixelY;
            lAx = accelX;
            lAy = -accelY;
            lVx = velocityX;
            lVy = velocityY;
        }

        if(pixelWidth <= 0 || pixelHeight <= 0) {
            // invalid width and height, nothing to do until the GUI comes up
            return;
        }

        long curTime = System.currentTimeMillis();
        if(lastTimeMs < 0) {
            lastTimeMs = curTime;
            return;
        }

        long elapsedMs = curTime - lastTimeMs;
        lastTimeMs = curTime;

        // update the velocity
        lVx += ((elapsedMs * lAx) / 1000) * pixelsPerMeter;
        lVy += ((elapsedMs * lAy) / 1000) * pixelsPerMeter;

        // update the position
        lCircleX += ((lVx * elapsedMs) / 1000) * pixelsPerMeter;
        lCircleY += ((lVy * elapsedMs) / 1000) * pixelsPerMeter;

        boolean bouncedX = false;
        boolean bouncedY = false;

        if (lCircleY - circleRadius < 0) {
            lCircleY = circleRadius;
            lVy = -lVy * rebound;
            bouncedY = true;
        } else if (lCircleY + circleRadius > lHeight) {
            lCircleY = lHeight - circleRadius;
            lVy = -lVy * rebound;
            bouncedY = true;
        }
        if (bouncedY && Math.abs(lVy) < STOP_BOUNCING_VELOCITY) {
            lVy = 0;
            bouncedY = false;
        }

        if (lCircleX - circleRadius < 0) {
            lCircleX = circleRadius;
            lVx = -lVx * rebound;
            bouncedX = true;
        } else if (lCircleX + circleRadius > lWidth) {
            lCircleX = lWidth - circleRadius;
            lVx = -lVx * rebound;
            bouncedX = true;
        }
        if (bouncedX && Math.abs(lVx) < STOP_BOUNCING_VELOCITY) {
            lVx = 0;
            bouncedX = false;
        }

        // safely copy local vars back to object fields
        synchronized (LOCK) {
            circlePixelX = lCircleX;
            circlePixelY = lCircleY;

            velocityX = lVx;
            velocityY = lVy;
        }
    }
}

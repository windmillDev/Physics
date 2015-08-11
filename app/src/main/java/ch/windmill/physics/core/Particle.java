package ch.windmill.physics.core;

/**
 * Created by jaunerc on 08.08.15.
 */
public class Particle {
    public static int screenWidth, screenHeight;
    // the speed is meters / second. When we draw to the screen,
    // 1 pixel represents 1 meter. That ends up too slow, so multiply
    // by this number. Bigger numbers speeds things up.
    public final float pixelsPerMeter;
    public float pixelX, pixelY;

    private float velocityX, velocityY;
    private float accelX, accelY;

    private volatile long lastTimeMs;

    public final Object LOCK;

    public Particle() {
        this(0,0,0,0,0,0);
    }

    public Particle(final float pixelX, final float pixelY) {
        this(pixelX, pixelY, 0,0,0,0);
    }

    public Particle(final float pixelX, final float pixelY, final float velocityX, final float velocityY,
                    final float accelX, final float accelY) {
        LOCK = new Object();
        lastTimeMs = -1;
        pixelsPerMeter = 10;

        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.accelX = accelX;
        this.accelY = accelY;
    }

    public void setAccel(final float accelX, final float accelY) {
        synchronized (LOCK) {
            this.accelX = accelX;
            this.accelY = accelY;
        }
    }

    public void setVelocity(final float velocityX, final float velocityY) {
        synchronized (LOCK) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }

    public void setPosition(final float x, final float y) {
        pixelX = x;
        pixelY = y;
        velocityX = 5;
        velocityY = 10;
    }

    public void update() {

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

        long elapsedMs = curTime - lastTimeMs;
        lastTimeMs = curTime;

        // update the velocity
        velocityX += ((elapsedMs * accelX) / 1000) * pixelsPerMeter;
        velocityY += ((elapsedMs * accelY) / 1000) * pixelsPerMeter;

        // update the position
        pixelX += ((velocityX * elapsedMs) / 1000) * pixelsPerMeter;
        pixelY += ((velocityY * elapsedMs) / 1000) * pixelsPerMeter;
    }
}

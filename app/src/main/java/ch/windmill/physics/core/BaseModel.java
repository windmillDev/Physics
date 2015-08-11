package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jaunerc on 10.08.15.
 */
public abstract class BaseModel {
    protected Particle particle;

    public BaseModel(final Particle particle) {
        this.particle = particle;
    }

    public void setParticle(final Particle particle) {
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }

    public void updatePhysics() {
        particle.update();
        collisionDetect();
    }

    public abstract void collisionDetect();

    public abstract void draw(Canvas c, Paint p);
}

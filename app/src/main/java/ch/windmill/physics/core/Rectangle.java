package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jaunerc on 10.08.15.
 */
public class Rectangle extends BaseModel{
    private int height, width;

    public Rectangle(final int height, final int width) {
        this(height, width, 0, 0);
    }

    public Rectangle(final int height, final int width, final float x, final float y) {
        super(new Particle(x,y));
        this.height = height;
        this.width = width;
    }

    @Override
    public void collisionDetect() {

    }

    @Override
    public void draw(final Canvas c, final Paint p) {
        c.drawRect(particle.pixelX, particle.pixelY, (particle.pixelX + width), (particle.pixelY + height), p);
    }
}

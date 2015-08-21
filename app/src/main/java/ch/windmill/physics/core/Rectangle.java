package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jaunerc on 10.08.15.
 */
public class Rectangle extends Figure {
    public Vector2D max;
    private int height, width;

    public Rectangle(final int height, final int width) {
        this(0,0,height,width);
    }

    public Rectangle(final float x, final float y, final int height, final int width) {
        super(new Vector2D(x,y));
        this.height = height;
        this.width = width;
        max = new Vector2D(x+width, y+height);
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        // updates the position of the max vector
        max.x = pos.x + width;
        max.y = pos.y + height;

        if(max.x > screenWidth || pos.x < 0) {
            synchronized (LOCK) {
                velocity.x *= -1;
            }
        }
        if(max.y > screenHeight || pos.y < 0) {
            synchronized (LOCK) {
                velocity.y *= -1;
            }
        }
    }

    @Override
    public void collisionDetect(final Rectangle r) {
        if(vsRect(r)) {
            System.out.println("rectangle collision detected");
            impulseResolution();
        }
    }

    private boolean vsRect(final Rectangle r) {
        if(max.x < r.pos.x || pos.x > r.max.x) { return false; }
        if(max.y < r.pos.y || pos.y > r.max.y) { return false; }
        return true;
    }

    @Override
    public void collisionDetect(final Circle c) {

    }

    public void impulseResolution() {
        synchronized (LOCK) {
            velocity.x = 0;
            velocity.y = 0;
        }
    }


    @Override
    public void draw(final Canvas c, final Paint p) {
        c.drawRect(pos.x, pos.y, max.x, max.y, p);
    }
}

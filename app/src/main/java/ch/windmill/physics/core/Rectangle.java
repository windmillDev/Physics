package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jaunerc on 10.08.15.
 */
public class Rectangle extends Body {
    public Vector2D max;
    private int height, width;

    public Rectangle(final int height, final int width) {
        this(0,0,height,width);
    }

    public Rectangle(final float x, final float y, final int height, final int width) {
        super(new Vector2D(x, y));
        this.height = height;
        this.width = width;
        max = new Vector2D(x+width, y+height);
    }

    @Override
    public void updatePosition(long frameDuration) {
        super.updatePosition(frameDuration);
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
    public void draw(final Canvas c, final Paint p) {
        c.drawRect(pos.x, pos.y, max.x, max.y, p);
    }

    public Vector2D getMiddle() {
        return new Vector2D((max.x - pos.x) / 2, (max.y - pos.y) / 2);
    }
}

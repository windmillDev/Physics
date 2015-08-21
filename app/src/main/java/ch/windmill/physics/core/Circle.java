package ch.windmill.physics.core;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jaunerc on 15.08.15.
 */
public class Circle extends Figure{
    private int radius;

    public Circle(final int radius) {
        this(0,0,radius);
    }

    public Circle(final float x, final float y, final int radius) {
        super(new Vector2D(x,y));
        this.radius = radius;
    }

    @Override
    public void updatePosition() {
        super.updatePosition();

        if((pos.x + radius) > screenWidth || (pos.x - radius) < 0) {
            synchronized (LOCK) {
                velocity.x *= -1;
            }
        }
        if((pos.y + radius) > screenHeight || (pos.y - radius) < 0) {
            synchronized (LOCK) {
                velocity.y *= -1;
            }
        }
    }

    @Override
    public void collisionDetect(Rectangle rectangle) {

    }

    @Override
    public void collisionDetect(Circle circle) {
        if(vsCircle(circle)) {
            System.out.println("Circle collision detected!");
            collide(this, circle, frameDuration);
            /**synchronized (LOCK) {
                velocity.x = 0;
                velocity.y = 0;
                acceleration.x = 0;
                acceleration.y = 0;
            }*/
        }
    }

    private boolean vsCircle(final Circle c) {
        float r = radius + c.radius;
        r *= r;
        return r > (Math.pow((pos.x - c.pos.x),2) + Math.pow((pos.y - c.pos.y),2));
    }

    private static void collide(final Circle c1, final Circle c2, final long duration) {
        Vector2D difference = Vector2D.sub(c1.pos, c2.pos);

        System.out.println("difference: "+difference.x + ";" + difference.y);
        System.out.println("difference: "+difference.length());
        float millisAfterCollision = backToCollisionPoint(duration, c1, c2,difference.length());

        // calculate the translation between the centers of the two circles
        Vector2D normal = difference;
        normal.normalize();

        System.out.println("normal: "+normal.length());

        // rotate the normal vector by 90 degrees -> find the collision plane
        Vector2D collision = new Vector2D(-1 * normal.y, normal.x);

        // calculate the velocities relative to the collision and normal plane
        float n_velocity1 = Vector2D.dot(normal, c1.velocity);
        float c_velocity1 = Vector2D.dot(collision, c1.velocity);
        float n_velocity2 = Vector2D.dot(normal, c2.velocity);
        float c_velocity2 = Vector2D.dot(collision, c2.velocity);

        // calculate the scaler velocities of each circle after the collision
        float n_velocity1_after = ((n_velocity1 * (c1.mass - c2.mass) + (2 * c2.mass * n_velocity2))) / (c1.mass + c2.mass);
        float n_velocity2_after = ((n_velocity2 * (c2.mass - c1.mass) + (2 * c1.mass * n_velocity1))) / (c1.mass + c2.mass);

        // convert the scalers to vectors
        Vector2D vec_n_velocity1 = Vector2D.multiply(normal, n_velocity1_after);
        Vector2D vec_c_velocity1 = Vector2D.multiply(collision, c_velocity1);
        Vector2D vec_n_velocity2 = Vector2D.multiply(normal, n_velocity2_after);
        Vector2D vec_c_velocity2 = Vector2D.multiply(collision, c_velocity2);

        // combine the vectors back into a single vector in 2D
        Vector2D velocity1_res = Vector2D.add(vec_n_velocity1, vec_c_velocity1);
        Vector2D velocity2_res = Vector2D.add(vec_n_velocity2, vec_c_velocity2);

        //c1.pos.x = 100;
        //c1.pos.y = 200;
        c1.pos = Vector2D.add(c1.pos, Vector2D.multiply(velocity1_res, millisAfterCollision));
        c2.pos = Vector2D.add(c2.pos, Vector2D.multiply(velocity2_res, millisAfterCollision));

        System.out.println("old velocity1: " + c1.velocity.length());
        c1.setVelocity(velocity1_res);
        c2.setVelocity(velocity2_res);
        System.out.println("new velocity1: " + c1.velocity.length());
    }

    private static float backToCollisionPoint(final long duration, final Circle c1, final Circle c2,
                                               final float distanceAtFrameEnd) {
        Vector2D circle1PosAtStart = new Vector2D((c1.pos.x - c1.velocity.x * duration), c1.pos.y - c1.velocity.y * duration);
        Vector2D circle2PosAtStart = new Vector2D((c2.pos.x - c2.velocity.x * duration), c2.pos.y - c2.velocity.y * duration);

        // calculate the difference between the circles at the start
        Vector2D diffAtStart = Vector2D.sub(circle2PosAtStart, circle1PosAtStart);

        // calculate the total change in distance during the frame and the required change to reach the collision
        float distanceTotalDelta = distanceAtFrameEnd - diffAtStart.length();
        float distanceDeltaToCollision = (c1.radius + c2.radius) - diffAtStart.length();

        // calculate the percentage change to the collision and after the collision
        float percentageDeltaToCollision = distanceDeltaToCollision / distanceTotalDelta;
        float percentageDeltaAfterCollision = 1 - percentageDeltaToCollision;

        // calculate the time before and after the collision
        double millisToCollision = duration * percentageDeltaToCollision;
        float millisAfterCollision = duration * percentageDeltaAfterCollision;

        // move the circles to their positions at the point of collision
        c1.pos = new Vector2D((float) (circle1PosAtStart.x + c1.velocity.x * millisToCollision),
                (float)(circle1PosAtStart.y + c1.velocity.y * millisToCollision));
        c2.pos = new Vector2D((float) (circle2PosAtStart.x + c2.velocity.x * millisToCollision),
                (float)(circle2PosAtStart.y + c2.velocity.y * millisToCollision));

        return millisAfterCollision;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(pos.x, pos.y, radius, paint);
    }
}

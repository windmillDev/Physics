package ch.windmill.physics.core;

import java.util.ArrayList;

/**
 * Created by jaunerc on 24.08.15.
 */
public class Engine {
    // the speed is meters / second. When we draw to the screen,
    // 1 pixel represents 1 meter. That ends up too slow, so multiply
    // by this number. Bigger numbers speeds things up.
    public static final int PIXELSPERMETER = 10;

    private ArrayList<Body> bodies;
    private long startTime, lastTimeMs, frameDuration;

    public Engine() {
        bodies = new ArrayList<>();
        // initial values for time variables
        startTime = -1;
        lastTimeMs = -1;
        frameDuration = -1;
    }

    public ArrayList<Body> getBodies() {
        return bodies;
    }

    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastTimeMs() {
        return lastTimeMs;
    }

    public void setLastTimeMs(long lastTimeMs) {
        this.lastTimeMs = lastTimeMs;
    }

    public long getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(long frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void addFigure(final Body f) {
        bodies.add(f);
    }

    public void handleFrame() {
        long curTime = System.currentTimeMillis();

        if(lastTimeMs < 0) {
            lastTimeMs = curTime;
            return;
        }

        frameDuration = curTime - lastTimeMs;
        lastTimeMs = curTime;

        for(Body f : bodies) {
            f.updatePosition(frameDuration);
        }

        for(int i = 0; i < bodies.size(); i++) {
            for(int j = 0; j < bodies.size(); j++) {
                if(j != i) {
                    Body b1 = bodies.get(i);
                    Body b2 = bodies.get(j);
                    Collision c = new Collision(b1,b2);

                    if(collisionDetect(b1, b2, c)) {
                        System.out.println("Collision detected!");
                        //Vector2D n = Vector2D.sub(b2.pos, b1.pos);
                        //n.normalize();
                        //resolveCollision(b1, b2, n);
                        c.resolveCollision();
                    }
                }
            }
        }
    }

    private boolean collisionDetect(final Body b1, final Body b2, final Collision c) {
        if(b1 instanceof Rectangle) {
            if(b2 instanceof Rectangle) {
                return RectVsRect((Rectangle) b1, (Rectangle) b2, c);
            } else if(b2 instanceof Circle) {
                return RectVsCircle((Rectangle) b1, (Circle) b2);
            }
        } else if(b1 instanceof Circle) {
            if(b2 instanceof Circle) {
                return CircleVsCircle((Circle) b1, (Circle) b2, c);
            } else if(b2 instanceof Rectangle) {
                return RectVsCircle((Rectangle) b2, (Circle) b1);
            }
        }

        return false;
    }

    private boolean RectVsRect(final Rectangle r1, final Rectangle r2, final Collision collision) {
        /**if(r1.max.x < r2.pos.x || r1.pos.x > r2.max.x) { return false; }
        if(r1.max.y < r2.pos.y || r1.pos.y > r2.max.y) { return false; }
        return true;*/

        Vector2D normal = Vector2D.sub(r2.getMiddle(), r1.getMiddle());

        // Calculate half extents along x axis for each body
        float b1_extent = (r1.max.x - r1.pos.x) / 2;
        float b2_extent = (r2.max.x - r2.pos.x) / 2;

        // Calculate overlap on x axis
        float x_overlap = b1_extent + b2_extent - Math.abs(normal.x);

        if(x_overlap > 0) {
            
        }

        return false;
    }

    private boolean CircleVsCircle(final Circle c1, final Circle c2, final Collision collision) {
        Vector2D normal = Vector2D.sub(c2.pos, c1.pos);

        float r = c1.radius + c2.radius;

        r *= r;

        if(normal.lengthSquared() > r) {
            return false;
        }
        //return r > (Math.pow((c1.pos.x - c2.pos.x),2) + Math.pow((c1.pos.y - c2.pos.y),2));
        float distance = normal.length();

        collision.setPenetration(r - distance);
        normal.normalize();
        collision.setNormal(normal);

        return true;
    }

    private boolean RectVsCircle(final Rectangle r1, final Circle c1) {
        return false;
    }

    private void resolveCollision(final Body f1, final Body f2, final Vector2D normal) {
        Vector2D relVel = Vector2D.sub(f2.velocity, f1.velocity);

        // calculate relative velocity in terms of the normal direction
        float scalarAlongNormal = Vector2D.dot(relVel, normal);

        // do not resolve if velocities are separating
        if(scalarAlongNormal > 0) {
            return;
        }

        // calculate restitution
        float e = 0.1f;

        // calculate impulse scalar
        float j = -(1 + e) * scalarAlongNormal;
        j /= 1 / f1.mass + 1 / f2.mass;

        // apply impulse
        Vector2D impulse = Vector2D.multiply(normal, j);
        f1.velocity = Vector2D.sub(f1.velocity, Vector2D.multiply(impulse, 1/f1.mass));
        f2.velocity = Vector2D.add(f2.velocity, Vector2D.multiply(impulse, 1/f2.mass));
    }
}

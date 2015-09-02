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
                        c.positionalCorrection();
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
                return RectVsCircle((Rectangle) b1, (Circle) b2, c);
            }
        } else if(b1 instanceof Circle) {
            if(b2 instanceof Circle) {
                return CircleVsCircle((Circle) b1, (Circle) b2, c);
            } else if(b2 instanceof Rectangle) {
                return RectVsCircle((Rectangle) b2, (Circle) b1, c);
            }
        }

        return false;
    }

    private boolean RectVsRect(final Rectangle r1, final Rectangle r2, final Collision collision) {
        // Calculate the vector from the middle position of r1 to the middle position of r2.
        Vector2D normal = Vector2D.sub(r2.getCenter(), r1.getCenter());

        // Calculate half extents along x axis for each body
        float b1_extent = (r1.max.x - r1.pos.x) / 2;
        float b2_extent = (r2.max.x - r2.pos.x) / 2;

        // Calculate overlap on x axis
        float x_overlap = b1_extent + b2_extent - Math.abs(normal.x);

        // SAT test x axis
        if(x_overlap > 0) {
            // Calculate half extents along y axis for each body
            b1_extent = (r1.max.y - r1.pos.y) / 2;
            b2_extent = (r2.max.y - r2.pos.y) / 2;

            // Calculate overlap on y axis
            float y_overlap = b1_extent + b2_extent - Math.abs(normal.y);

            // SAT test y axis
            if(y_overlap > 0) {
                // find out the axis with least penetration
                if(x_overlap < y_overlap) {
                    if(normal.x < 0) {
                        collision.setNormal(new Vector2D(-1, 0));
                    } else {
                        collision.setNormal(new Vector2D(1, 0));
                    }
                    collision.setPenetration(x_overlap);
                    return true;
                } else {
                    if(normal.y < 0) {
                        collision.setNormal(new Vector2D(0,-1));
                    } else {
                        collision.setNormal(new Vector2D(0, 1));
                    }
                    collision.setPenetration(y_overlap);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean CircleVsCircle(final Circle c1, final Circle c2, final Collision collision) {
        Vector2D normal = Vector2D.sub(c2.pos, c1.pos);

        float r = c1.radius + c2.radius;

        if(normal.lengthSquared() > (r*r)) {
            return false;
        }
        //return r > (Math.pow((c1.pos.x - c2.pos.x),2) + Math.pow((c1.pos.y - c2.pos.y),2));
        float distance = normal.length();

        collision.setPenetration(r - distance);
        normal.normalize();
        collision.setNormal(normal);

        return true;
    }

    private boolean RectVsCircle(final Rectangle r1, final Circle c1, final Collision collision) {
        // vector from rect to circle
        Vector2D difference = Vector2D.sub(c1.pos, r1.getCenter());
        Vector2D closest = new Vector2D();

        // calculate half extents for each axis
        float x_extent = (r1.max.x - r1.pos.x) / 2;
        float y_extent = (r1.max.y - r1.pos.y) / 2;

        // clamp point to edges of the AABB
        closest.x = clamp(-x_extent, x_extent, difference.x);
        closest.y = clamp(-y_extent, y_extent, difference.y);

        boolean inside = false;

        // Circle is inside the AABB, so we need to clamp the circle's center
        // to the closest edge
        if(Vector2D.equals(closest, difference)) {
            inside = true;

            // find closest axis
            if(Math.abs(difference.x) > Math.abs(difference.y)) {
                if(closest.x > 0) {
                    closest.x = x_extent;
                } else {
                    closest.x = -x_extent;
                }
            } else {
                if(closest.y > 0) {
                    closest.y = x_extent;
                } else {
                    closest.y = -x_extent;
                }
            }
        }

        Vector2D normal = Vector2D.sub(difference, closest);
        float d = normal.lengthSquared();
        float r = c1.radius;

        if(d > (r * r) && !inside) {
            return false;
        }

        // avoid sqrt until we needed
        d = (float) Math.sqrt(d);
        normal.normalize();
        if(inside) {
            collision.setNormal(Vector2D.multiply(normal, -1));
        } else {
            collision.setNormal(normal);
        }

        collision.setPenetration(r - d);

        return true;
    }

    private float clamp(final float f, final float low, final float high) {
        return Math.max(Math.min(f, high), low);
    }
}

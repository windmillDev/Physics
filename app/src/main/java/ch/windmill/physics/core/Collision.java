package ch.windmill.physics.core;

/**
 * Created by jaunerc on 30.08.15.
 */
public class Collision {
    private Body b1, b2;
    private float penetration;
    private Vector2D normal;

    public Collision(final Body b1, final Body b2) {
        this.b1 = b1;
        this.b2 = b2;
        penetration = 0;
        normal = new Vector2D();
    }

    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }

    public void setNormal(Vector2D normal) {
        this.normal = normal;
    }

    public void resolveCollision() {
        // Calculate the relative velocity
        Vector2D relVelocity = Vector2D.sub(b2.velocity, b1.velocity);

        // calculate relative velocity in terms of the normal direction
        float scalarAlongNormal = Vector2D.dot(relVelocity, normal);

        // do not resolve if velocities are separating
        if(scalarAlongNormal > 0) {
            return;
        }

        // calculate restitution
        float e = 0.5f;

        // calculate impulse scalar
        float j = -(1 + e) * scalarAlongNormal;
        j /= 1 / b1.mass + 1 / b2.mass;

        // apply impulse
        Vector2D impulse = Vector2D.multiply(normal, j);
        b1.velocity = Vector2D.sub(b1.velocity, Vector2D.multiply(impulse, 1/b1.mass));
        b2.velocity = Vector2D.add(b2.velocity, Vector2D.multiply(impulse, 1/b2.mass));
    }
}

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
        float e = 1f;

        // calculate impulse scalar
        float j = -(1 + e) * scalarAlongNormal;
        j /= 1 / b1.getMass() + 1 / b2.getMass();

        // apply impulse
        Vector2D impulse = Vector2D.multiply(normal, j);
        b1.velocity = Vector2D.sub(b1.velocity, Vector2D.multiply(impulse, b1.getInverseMass()));
        b2.velocity = Vector2D.add(b2.velocity, Vector2D.multiply(impulse, b2.getInverseMass()));
    }

    public void positionalCorrection() {
        float percent = 0.4f;
        float slop = 0.01f;

        //float correctionScalar = Math.max(penetration -slop, 0.0f) / (b2.getInverseMass() + b1.getInverseMass() * percent);
        Vector2D correction = Vector2D.multiply(normal,penetration / (b1.getInverseMass() + b2.getInverseMass()));
        correction = Vector2D.multiply(correction, percent);
        //Vector2D correction = Vector2D.multiply(normal, correctionScalar);
        //Vector2D correction = Vector2D.multiply(normal, correctionScalar);

        b1.pos = Vector2D.sub(b1.pos, Vector2D.multiply(correction, b1.getInverseMass()));
        //b1.pos = Vector2D.sub(b1.pos, correction);
        b2.pos = Vector2D.add(b2.pos, Vector2D.multiply(correction, b2.getInverseMass()));
        //b2.pos = Vector2D.add(b2.pos, correction);

        System.out.println("penetration: "+ penetration);
        System.out.println("normal: "+normal.x+" ; "+normal.y);
    }
}

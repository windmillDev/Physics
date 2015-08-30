package ch.windmill.physics.core;

/**
 * Created by jaunerc on 30.08.15.
 */
public class MassData {
    private float mass;
    private float inv_mass;

    public MassData(final float density, final float area) {
        mass = density * area;
        inv_mass = 1 / mass;
    }

    public float getMass() {
        return mass;
    }

    public float getInv_mass() {
        return inv_mass;
    }
}

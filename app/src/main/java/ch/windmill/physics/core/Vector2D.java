package ch.windmill.physics.core;

/**
 * This class represents a 2D vector. A vector is defined by a magnitude and a direction. There
 * are methods for basic calculating with vectors.
 *
 * Created by jaunerc on 04.08.2015.
 */
public class Vector2D {
    // the fields are public to access easier through calculation
    public float x;
    public float y;

    /**
     * Calculate the dot product of the two given vectors.
     * @param v1 vector 1
     * @param v2 vector 2
     * @return the dot product
     */
    public static float dot(final Vector2D v1, final Vector2D v2) {
        return (v1.x * v2.x + v1.y * v2.y);
    }

    /**
     * Add the two given vectors.
     * @param v1 vector 1
     * @param v2 vector 2
     * @return result vector of the addition
     */
    public static Vector2D add(final Vector2D v1, final Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * Subtract the second vector from the first vector.
     * @param v1 vector 1
     * @param v2 vector 2
     * @return result vector of the subtraction
     */
    public static Vector2D sub(final Vector2D v1, final Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * Multiply the scalar value with the given vector.
     * @param v vector
     * @param scalar value
     * @return result vector of the multiplication
     */
    public static Vector2D multiply(final Vector2D v, final float scalar) {
        return new Vector2D(v.x * scalar, v.y * scalar);
    }

    /**
     * Check if two vectors have the same x and y value.
     * @param v1 vector 1
     * @param v2 vector 2
     * @return if both vectors are equals.
     */
    public static boolean equals(final Vector2D v1, final Vector2D v2) {
        return (v1.x == v2.x && v1.y == v2.y);
    }

    /**
     * Creates a new vector object.
     */
    public Vector2D() {
        this(0,0);
    }

    /**
     * Creates a new vector object.
     * @param x value of the x axis.
     * @param y value of the y axis.
     */
    public Vector2D(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the magnitude / length of the vector. The calculation is sqrt(x^2 + y^2).
     * @return the magnitude / length of the vector.
     */
    public float length() {
        return (float) Math.sqrt((x*x + y*y));
    }

    /**
     * Calculates the square of the length.
     * @return the square of the length.
     */
    public float lengthSquared() {
        return (x*x + y*y);
    }

    /**
     * Normalize the vector. This means, set the length of the vector to 1. The direction will not
     * be changed.
     */
    public void normalize() {
        float len = length();
        x = x / len;
        y = y / len;
    }
}

package hr.fer.zemris.math;

/**
 * This class models a vector in three-dimensional cartesian coordinate
 * system. The vector is represented by it's three components: it's x, y
 * and z coordinates.
 * <p>
 * A {@link Vector3} object is immutable, meaning that it cannot be modified
 * once it's created. This also means that all operations performed on a
 * {@link Vector3} object return a new object which represents the result
 * of the operation.
 *
 * @author Luka Čupić
 */
public class Vector3 {

    /**
     * Represents the x coordinate of this vector.
     */
    private double x;

    /**
     * Represents the y coordinate of this vector.
     */
    private double y;

    /**
     * Represents the z coordinate of this vector.
     */
    private double z;

    /**
     * Creates a new {@link Vector3} object.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the length of this vector, defined as the
     * length from the point (x, y, z) to the origin of
     * the coordinate system (0, 0, 0).
     *
     * @return the length of this vector
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns a normalized vector of this vector.
     * A normalized vector is defined as a vector in
     * the same direction as the original vector, but
     * of norm with length {@code 1}.
     *
     * @return a normalized vector of this vector
     */
    public Vector3 normalized() {
        double norm = norm();

        if (norm == 0) {
            return new Vector3(0, 0, 0);
        }

        double x = this.x / norm;
        double y = this.y / norm;
        double z = this.z / norm;

        return new Vector3(x, y, z);
    }

    /**
     * Performs a vector addition operation between this
     * vector and the specified vector.
     *
     * @param other the vector
     * @return a new {@link Vector3} object representing
     * the result of the addition operation
     */
    public Vector3 add(Vector3 other) {
        checkVector(other);

        double x = this.x + other.getX();
        double y = this.y + other.getY();
        double z = this.z + other.getZ();

        return new Vector3(x, y, z);
    }

    /**
     * Performs a vector subtraction operation between this
     * vector and the specified vector.
     *
     * @param other the vector
     * @return a new {@link Vector3} object representing
     * the result of subtraction operation
     */
    public Vector3 sub(Vector3 other) {
        checkVector(other);

        double x = this.x - other.getX();
        double y = this.y - other.getY();
        double z = this.z - other.getZ();

        return new Vector3(x, y, z);
    }

    /**
     * Returns the dot product of this vector and the
     * specified vector.
     *
     * @param other the vector
     * @return the value of the dot product
     */
    public double dot(Vector3 other) {
        checkVector(other);

        double xProduct = getX() * other.getX();
        double yProduct = getY() * other.getY();
        double zProduct = getZ() * other.getZ();

        return xProduct + yProduct + zProduct;
    }

    /**
     * Returns the cross product of this vector and the
     * specified vector.
     *
     * @param other the vector
     * @return a new {@link Vector3} object representing
     * the result of the cross product operation
     */
    public Vector3 cross(Vector3 other) {
        checkVector(other);

        double x = getY() * other.getZ() - getZ() * other.getY();
        double y = getZ() * other.getX() - getX() * other.getZ();
        double z = getX() * other.getY() - getY() * other.getX();

        return new Vector3(x, y, z);
    }

    /**
     * Returns a new vector whose length is equal to the
     * length of this vector multiplied by the specified
     * factor, and whose direction is the same as the
     * direction of this vector.
     * <p>
     * For factors smaller than {@code 1.0}, the scaled
     * vector will be smaller in length than this vector,
     * and for factors larger than {@code 1.0}, the scaled
     * vector will be larger in length than this vector.
     * Scaling this vector with the factor {@code 1.0}
     * returns a vector of exactly the same length as this
     * vector.
     *
     * @param factor the factor to scale this vector with
     * @return a new {@link Vector3}, representing the
     * scaled version of this vector
     */
    public Vector3 scale(double factor) {

        double x = getX() * factor;
        double y = getY() * factor;
        double z = getZ() * factor;

        return new Vector3(x, y, z);
    }

    /**
     * Returns the cosine of the angle between this
     * vector and the specified vector.
     *
     * @param other the vector
     * @return the cosine of the angle between this
     * vector and the specified vector
     */
    public double cosAngle(Vector3 other) {
        checkVector(other);

        return Math.cos(angle(other));
    }

    /**
     * Returns the angle between this vector and the
     * specified vector.
     *
     * @return the angle between this vector and the
     * specified vector.
     */
    private double angle(Vector3 other) {
        return Math.acos(dot(other) / (this.norm() * other.norm()));
    }

    /**
     * A helper method for checking whether the given
     * {@link Vector3} object is null.
     *
     * @param other the vector object to check
     * @throws IllegalArgumentException if the given object is null
     */
    private void checkVector(Vector3 other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Vector cannot be null!");
        }
    }

    /**
     * Returns the x component of this vector.
     *
     * @return the x coordinate of this vector
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y component of this vector.
     *
     * @return the y coordinate of this vector
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the z component of this vector.
     *
     * @return the z coordinate of this vector
     */
    public double getZ() {
        return z;
    }

    /**
     * Converts this vector into an array, where each
     * element of the array corresponds to a component
     * of the vector.
     *
     * @return an array representation of this vector
     */
    public double[] toArray() {
        return new double[]{x, y, z};
    }

    /**
     * Returns a string representation of this vector.
     *
     * @return a string representation of this vector.
     */
    public String toString() {
        return String.format("(%6f, %6f, %6f)", x, y, z);
    }
}

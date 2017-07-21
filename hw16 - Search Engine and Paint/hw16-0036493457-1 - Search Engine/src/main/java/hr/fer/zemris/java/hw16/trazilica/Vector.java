package hr.fer.zemris.java.hw16.trazilica;

/**
 * This class represents an immutable N-dimensional vector.
 *
 * @author Luka Čupić
 */
public class Vector {

	/**
	 * Represents the values of this vector.
	 */
	private double[] values;

	/**
	 * Creates a new N-dimensional vector, where the dimension is
	 * obtained from the number of provided elements.
	 *
	 * @param values the values to go in to the vector
	 */
	public Vector(double... values) {
		this.values = values;
	}

	/**
	 * Gets the norm of the vector.
	 *
	 * @return the norm of the vector
	 */
	public double norm() {
		double sum = 0;
		for (double value : values) {
			sum += value * value;
		}
		return Math.sqrt(sum);
	}

	/**
	 * Returns the dot product of this vector and the specified vector.
	 *
	 * @param other the other vector
	 * @return the value of the dot product
	 */
	public double dot(Vector other) {
		double sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i] * other.getValues()[i];
		}
		return sum;
	}

	/**
	 * Returns the cosine of the angle between this vector and
	 * the specified vector.
	 *
	 * @param other the vector
	 */
	public double cos(Vector other) {
		return Math.cos(angle(other));
	}

	/**
	 * Returns the angle between this vector and the specified vector.
	 *
	 * @return the angle between this vector and the specified vector
	 */
	public double angle(Vector other) {
		return Math.acos(this.dot(other) / (this.norm() * other.norm()));
	}

	/**
	 * Multiplies the corresponding components of the given vectors
	 * and returns the product vector as the result. In other words,
	 * for the two given vectors of the same number of elements, e.g.
	 * for the given vectors V1 and V2 where: {@code V1 = [a1, a2, ...,
	 * an]} and {@code V2 = [b1, b2, ..., bn]} the vector V is returned
	 * where {@code V = [a1*b1, a2*b2, ..., an*bn]}.
	 *
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * @return a new vector, where each element represents the product
	 * of the elements from the given vectors
	 */
	public static Vector multiply(Vector v1, Vector v2) {
		if (v1.getValues().length != v2.getValues().length) {
			throw new IllegalArgumentException("Vectors must have the same" +
				"number of elements!");
		}

		double[] values = new double[v1.getValues().length];
		for (int i = 0; i < v1.getValues().length; i++) {
			values[i] = v1.getValues()[i] * v2.getValues()[i];
		}
		return new Vector(values);
	}


	/**
	 * Gets the values of this vector.
	 *
	 * @return the values of this vector
	 */
	public double[] getValues() {
		return values;
	}
}
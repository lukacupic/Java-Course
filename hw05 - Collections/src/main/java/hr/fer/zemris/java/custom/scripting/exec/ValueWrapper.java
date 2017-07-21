package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Instance of this class represent a wrapper that wraps an {@link Object}.
 * The allowed values to store are: {@link String}, {@link Integer}, {@link Double}
 * and null. ALl other types of objects are not permitted and will result in a
 * {@link RuntimeException} while trying to create the object.
 * <p>
 * While performing arithmetic operations, if the wrapped value and the passed
 * {@link ValueWrapper} object are not compatible (i.e. the operation cannot be
 * performed on these two objects), a {@link RuntimeException} will be thrown.
 *
 * @author Luka Čupić
 */
public class ValueWrapper {

	/**
	 * The value of the object.
	 */
	private Object value;

	/**
	 * Represents the return value of the {@link ValueWrapper#numCompare(Object)}
	 * method which is calculated in the {@link ValueWrapper#checkArgument(Object)}
	 * method.
	 */
	private int compares;

	/**
	 * Creates a new instance of this class which will store the passed value.
	 *
	 * @param value the value which this object will represent.
	 */
	public ValueWrapper(Object value) {
		checkArgument(value);

		this.value = value;
	}

	/**
	 * Adds the passed value to the value of this object.
	 *
	 * @param incValue the value to add to this object.
	 * @throws RuntimeException if the arithmetic operation cannot
	 *                          be performed.
	 */
	public void add(Object incValue) {
		checkArgument(value);

		performOperation(value, incValue, "add");
	}

	/**
	 * Subtracts the passed value from the value of this object.
	 *
	 * @param decValue the value to subtract from this object.
	 * @throws RuntimeException if the arithmetic operation cannot
	 *                          be performed.
	 */
	public void subtract(Object decValue) {
		checkArgument(value);

		performOperation(value, decValue, "subtract");
	}

	/**
	 * Multiplies this object by the passed value.
	 *
	 * @param mulValue the value to multiply this object by.
	 * @throws RuntimeException if the arithmetic operation cannot
	 *                          be performed.
	 */
	public void multiply(Object mulValue) {
		checkArgument(value);

		performOperation(value, mulValue, "multiply");
	}

	/**
	 * Divides this object by the passed value.
	 *
	 * @param divValue the value to divide this object by.
	 * @throws RuntimeException if the arithmetic operation cannot
	 *                          be performed.
	 */
	public void divide(Object divValue) {
		checkArgument(value);

		performOperation(value, divValue, "divide");
	}

	/**
	 * Numerically compares this object by the passed value.
	 *
	 * @param withValue the value to compare this object to.
	 * @throws RuntimeException if the comparison operation cannot
	 *                          be performed.
	 */
	public int numCompare(Object withValue) {
		performOperation(value, withValue, "compare");
		return compares;
	}

	/**
	 * Gets the value of this object.
	 *
	 * @return the value of this object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value of this object to a new value.
	 *
	 * @param value the new value of this object.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * A helper method for checking if the passed argument is allowed
	 * as a value for performing arithmetic operations. The allowed values
	 * are either {@link String}, {@link Integer}, {@link Double} or null.
	 *
	 * @param value the value to be checked.
	 * @throws IllegalArgumentException if the argument is illegal.
	 */
	private boolean checkArgument(Object value) {
		if (value == null) return true;
		if (value instanceof String) return true;
		if (value instanceof Integer) return true;
		if (value instanceof Double) return true;

		throw new RuntimeException(
			"The passed value is not allowed in arithmetic operations!"
		);
	}

	/**
	 * Performs a mathematical operation (denoted by the {@param operation})
	 * between the two passed objects.
	 *
	 * @param o1        the first operand.
	 * @param o2        the second operand.
	 * @param operation a string representing the operation. Legal operations are:
	 *                  "add", "subtract", "divide", "multiply" and "compare". All
	 *                  other operations will cause an {@link IllegalArgumentException}.
	 */
	private void performOperation(Object o1, Object o2, String operation) {
		Integer o1Int = null, o2Int = null;
		Double o1Double = null, o2Double = null;

		if (o1 == null) {
			o1Int = 0;
		}

		if (o2 == null) {
			o2Int = 0;
		}

		if (o1 instanceof String) {
			String o1String = (String) o1;

			// try to convert the string to an int; if that fails,
			// then it can only be a double; if that fails too,
			// the method call will throw an exception
			try {
				o1Int = Integer.parseInt(o1String);
			} catch (NumberFormatException ex) {
				o1Double = convertString(o1String);
			}
		}

		if (o2 instanceof String) {
			String o2String = (String) o2;

			// try to convert the string to an int; if that fails,
			// then it can only be a double; if that fails too,
			// the method call will throw an exception
			try {
				o2Int = Integer.parseInt(o2String);
			} catch (NumberFormatException ex) {
				o2Double = convertString(o2String);
			}
		}

		if (o1 instanceof Integer) {
			o1Int = (Integer) o1;
		}

		if (o2 instanceof Integer) {
			o2Int = (Integer) o2;
		}

		if (o1 instanceof Double) {
			o1Double = (Double) o1;
		}

		if (o2 instanceof Double) {
			o2Double = (Double) o2;
		}

        /*
		now we have the o1 and o2 values properly stored in some two
        out of the four objects from the top of the method
        */

		// store these two values into doubles; if we later determine
		// that they are integers, the result will be converted
		Double o1Final = o1Int != null ? o1Int : o1Double;
		Double o2Final = o2Int != null ? o2Int : o2Double;

		Double result;
		switch (operation) {
			case "add":
				result = o1Final + o2Final;
				break;
			case "subtract":
				result = o1Final - o2Final;
				break;
			case "multiply":
				result = o1Final * o2Final;
				break;
			case "divide":
				result = (double) o1Final / o2Final;
				break;
			case "compare":
				compares = o1Final.compareTo(o2Final);
				return;
			default:
				throw new IllegalArgumentException("Operation not supported!");
		}

		// if either object is a double, or if the operation is division the result will be a double
		if (o1Double != null || o2Double != null || "divide".equals(operation)) {
			value = result;

			// both values are integers, so the result is an integer
		} else {
			value = result.intValue();
		}
	}

	/**
	 * A helper method for converting a string into a double value.
	 * If the conversion fails, the method call will result with an
	 * instance of the {@link RuntimeException}.
	 *
	 * @param string the string to convert.
	 */
	private double convertString(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException exc) {
			throw new RuntimeException(
				"The passed value is not allowed in arithmetic operations!"
			);
		}
	}
}

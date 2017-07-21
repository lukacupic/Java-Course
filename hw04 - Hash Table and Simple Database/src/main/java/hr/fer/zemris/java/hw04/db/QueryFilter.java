package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Represents a filter of {@link StudentRecord} objects.
 * <p>
 * Creation of this class is possible only by passing a list of
 * {@link ConditionalExpression} objects which will represent the filtering
 * conditions for the {@link StudentRecord} objects. Only those objects which
 * satisfy the conditions placed by the expressions will be regarded as "accepted".
 *
 * @author Luka Čupić
 */
public class QueryFilter implements IFilter {

	private List<ConditionalExpression> expressionList;

	/**
	 * Creates a new filter by passing a list of conditional expressions.
	 *
	 * @param expressionList the expressions representing the filtering conditions.
	 */
	public QueryFilter(List<ConditionalExpression> expressionList) {
		this.expressionList = expressionList;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : expressionList) {
			// check if the passed record satisfies the condition placed
			// by the conditional expression
			boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
			);

			if (!recordSatisfies) {
				return false;
			}
		}
		return true;
	}
}

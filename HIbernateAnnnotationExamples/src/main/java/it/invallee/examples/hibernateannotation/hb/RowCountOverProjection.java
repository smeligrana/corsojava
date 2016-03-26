package it.invallee.examples.hibernateannotation.hb;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.SimpleProjection;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionRegistry;
import org.hibernate.type.Type;

public class RowCountOverProjection extends SimpleProjection {
//	private static final List ARGS = java.util.Collections.singletonList("*");
	private static final List ARGS = java.util.Collections.EMPTY_LIST;

	public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		final Type countFunctionReturnType = getFunction(criteriaQuery).getReturnType(null, criteriaQuery.getFactory());
		return new Type[] { countFunctionReturnType };
	}

	public String toSqlString(Criteria criteria, int position, CriteriaQuery criteriaQuery) throws HibernateException {
		return getFunction(criteriaQuery).render(null, ARGS, criteriaQuery.getFactory()) + " as y" + position + '_';
	}

	protected SQLFunction getFunction(CriteriaQuery criteriaQuery) {
		final SQLFunctionRegistry sqlFunctionRegistry = criteriaQuery.getFactory().getSqlFunctionRegistry();
		final SQLFunction function = sqlFunctionRegistry.findSQLFunction("countover");
		if (function == null) {
			throw new HibernateException("Unable to locate count function mapping");
		}
		return function;
	}

	@Override
	public String toString() {
		return "count(*) over()";
	}
}

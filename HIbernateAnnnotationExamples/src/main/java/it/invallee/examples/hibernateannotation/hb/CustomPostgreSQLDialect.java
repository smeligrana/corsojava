package it.invallee.examples.hibernateannotation.hb;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {

	public CustomPostgreSQLDialect() {
		super();
		
		registerFunction( "countover", new NoArgSQLFunction( "count(*) over", StandardBasicTypes.LONG ) );
	}

}

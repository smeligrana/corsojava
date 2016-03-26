package it.invallee.examples.hibernateannotation.hb;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomHSQLDialect extends HSQLDialect {

	public CustomHSQLDialect() {
		super();
		
		registerFunction( "countover", new NoArgSQLFunction( "count(*) over", StandardBasicTypes.STRING ) );
	}

}

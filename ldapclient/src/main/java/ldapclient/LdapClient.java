package ldapclient;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.control.SortControlDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.AttributesMapperCallbackHandler;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.SearchExecutor;
import org.springframework.ldap.core.support.AggregateDirContextProcessor;
import org.springframework.ldap.core.support.SingleContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;

public class LdapClient {
	private static final Name BASE = LdapUtils.emptyLdapName();
	private static final String FILTER_STRING = "(&(objectclass=person))";

	private LdapTemplate ldapTemplate;
	private SearchControls searchControls;
	private CollectingNameClassPairCallbackHandler callbackHandler;

	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public void execute() {
		long start = System.currentTimeMillis();
		
		final AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));

		SearchExecutor searchExecutor = new SearchExecutor() {
			public NamingEnumeration executeSearch(DirContext ctx) throws NamingException {
				return ctx.search(BASE, filter.encode(), searchControls);
			}
		};

//		AttributesMapper mapper = new PersonAttributesMapper();
		AttributesMapper mapper = new ContactAttributeMapper();
		searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		SortControlDirContextProcessor sortControlDirContextProcessor = new SortControlDirContextProcessor("cn");
		sortControlDirContextProcessor.setCritical(false);

		int i = 0;
		PagedResultsDirContextProcessor pagedResultsDirContextProcessor = null;
		PagedResultsCookie cookie = null;
		do {
			i++;

			pagedResultsDirContextProcessor = new PagedResultsDirContextProcessor(1, cookie);

			AggregateDirContextProcessor aggregateDirContextProcessor = new AggregateDirContextProcessor();
			aggregateDirContextProcessor.addDirContextProcessor(sortControlDirContextProcessor);
			aggregateDirContextProcessor.addDirContextProcessor(pagedResultsDirContextProcessor);

			callbackHandler = new AttributesMapperCallbackHandler(mapper);
			ldapTemplate.search(searchExecutor, callbackHandler, aggregateDirContextProcessor);
			cookie = pagedResultsDirContextProcessor.getCookie();

			List list = callbackHandler.getList();
			System.out.println(i + " " + list.size());
		} while (pagedResultsDirContextProcessor.hasMore());

		long end = System.currentTimeMillis();
		System.out.println("Finito "+(end-start)+" ms");
	}

	private void init() {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:389/dc=example,dc=com");
		env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=example,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		DirContext ctx;
		try {
			ctx = new InitialLdapContext(env, null);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		SingleContextSource contextSource = new SingleContextSource(ctx);
		ldapTemplate = new LdapTemplate(contextSource);
	}


	public static void main(String[] args) {
		executeSpring();
//		executeSimple();
	}
	
	private static void executeSimple() {
		LdapClient ldapClient = new LdapClient();
		ldapClient.init();
		ldapClient.execute();
	}

	private static void executeSpring() {
		try {
			Resource resource = new ClassPathResource("springldap.xml");
			BeanFactory factory = new XmlBeanFactory(resource);
			LdapClient ldapClient = (LdapClient) factory.getBean("ldapClient");
			ldapClient.execute();
		} catch (DataAccessException e) {
			System.out.println("Error occured " + e.getCause());
		}
	}

}

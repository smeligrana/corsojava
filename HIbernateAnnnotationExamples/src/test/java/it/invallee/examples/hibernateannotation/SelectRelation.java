package it.invallee.examples.hibernateannotation;

import it.invallee.examples.hibernateannotation.hb.Persona;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SelectRelation {
	private static SessionFactory factory;

	@BeforeClass
	public static void setUp() {
		Initializer initializer = new Initializer();
		factory = initializer.setUp();
	}
	
	@Test(expected=LazyInitializationException.class)
	public void listPersoneCriteria() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Persona> persone = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			persone = criteria.list();
			tx.commit();

			Assert.assertEquals(3, persone.size());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		for (Persona persona : persone) {
			System.out.println(persona);
			System.out.println(persona.getIndirizzi());
		}
	}
}

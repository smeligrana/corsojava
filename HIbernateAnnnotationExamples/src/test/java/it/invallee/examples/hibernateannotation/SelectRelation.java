package it.invallee.examples.hibernateannotation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.invallee.examples.hibernateannotation.hb.Persona;

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
	
	@Test
	public void listPersoneCriteriaLoadRelationWithJoin() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Persona> persone = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			criteria.setFetchMode("indirizzi", FetchMode.JOIN);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
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
	
	@Test
	public void listPersoneCriteriaLoadRelationWithInitialize() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Persona> persone = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			persone = criteria.list();
			
			for (Persona persona : persone) {
				Hibernate.initialize(persona.getIndirizzi());
			}
			
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

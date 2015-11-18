package it.invallee.examples.hibernateannotation;

import it.invallee.examples.hibernateannotation.hb.Persona;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleSelect {
	private static SessionFactory factory;

	@BeforeClass
	public static void setUp() {
		Initializer initializer = new Initializer();
		factory = initializer.setUp();
	}

	@Test
	public void listPersoneHql() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Persona> persone = session.createQuery("FROM Persona").list();
			for (Persona persona : persone) {
				System.out.println(persona);
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
	}

	@Test
	public void listPersoneCriteria() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			List<Persona> persone = criteria.list();
			for (Persona persona : persone) {
				System.out.println(persona);
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
	}
	
	@Test
	public void listPersonaCriteria() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			criteria.add(Restrictions.ilike("cognome", "Meligrana", MatchMode.ANYWHERE));
			List<Persona> persone = criteria.list();
			for (Persona persona : persone) {
				System.out.println(persona);
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
	}

	@Test
	public void listPersoneIndirizzoHql() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Persona> persone = session.createQuery(
					"select distinct p FROM Persona p inner join p.indirizzi")
					.list();
			for (Persona persona : persone) {
				System.out.println(persona);
			}
			tx.commit();

			Assert.assertEquals(1, persone.size());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Test
	public void listPersoneIndirizzoCriteria() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			criteria.createAlias("indirizzi", "indirizzo");
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			List<Persona> persone = criteria.list();
			for (Persona persona : persone) {
				System.out.println(persona);
			}
			tx.commit();

			Assert.assertEquals(1, persone.size());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Test
	public void listPersoneIndirizzoCriteriaDistinct() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			DetachedCriteria detachedCriteria = DetachedCriteria
					.forClass(Persona.class);
			detachedCriteria.createAlias("indirizzi", "indirizzo");
			detachedCriteria.setProjection(Projections.distinct(Projections
					.id()));

			Criteria criteria = session.createCriteria(Persona.class);
			criteria.add(Subqueries.propertyIn("idPersona", detachedCriteria));

			List<Persona> persone = criteria.list();
			for (Persona persona : persone) {
				System.out.println(persona);
			}
			tx.commit();

			Assert.assertEquals(1, persone.size());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Test
	public void listPersoneIndirizzoMultiTuplesHql() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Object[]> tuples = session.createQuery(
					"select p, i  FROM Persona p inner join p.indirizzi i")
					.list();
			for (Object[] tupla : tuples) {
				System.out.println(tupla[0]);
				System.out.println(tupla[1]);
			}
			tx.commit();

			Assert.assertEquals(2, tuples.size());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}

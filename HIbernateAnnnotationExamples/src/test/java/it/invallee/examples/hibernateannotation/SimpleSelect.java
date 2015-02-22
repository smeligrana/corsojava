package it.invallee.examples.hibernateannotation;

import it.invallee.examples.hibernateannotation.hb.Indirizzo;
import it.invallee.examples.hibernateannotation.hb.Persona;
import it.invallee.examples.hibernateannotation.hb.TipoIndirizzo;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleSelect {
	private static SessionFactory factory;
	
	@BeforeClass
	public static void setUp() {
		try {
			factory = new AnnotationConfiguration()
					.configure()
//					.addPackage("it.invallee.examples.hibernateannotation.hb")
					.addAnnotatedClass(Persona.class).addAnnotatedClass(Indirizzo.class).addAnnotatedClass(TipoIndirizzo.class)
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		
		Long idPersona1 = addPersona("Meligrana", "Sergio");
		addIndirizzo("Neyran Dessus 65A", "Brissogne", idPersona1);
		addIndirizzo("Via Che Guevara 7", "Drapia", idPersona1);
		
		addPersona("Meligrana", "Alice");
		addPersona("Meligrana", "Arianna");
	}
	
	public static Long addPersona(String cognome, String nome) {
		Session session = factory.openSession();
		Transaction tx = null;
		Long personaId = null;
		try {
			tx = session.beginTransaction();
			Persona persona = new Persona();
			persona.setCognome(cognome);
			persona.setNome(nome);
			personaId = (Long) session.save(persona);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return personaId;
	}
	
	public static Long addIndirizzo(String comune, String via, Long idPersona) {
		Session session = factory.openSession();
		Transaction tx = null;
		Long personaId = null;
		try {
			Persona persona = (Persona)session.get(Persona.class, idPersona);
			
			tx = session.beginTransaction();
			Indirizzo indirizzo = new Indirizzo();
			indirizzo.setComune(comune);
			indirizzo.setVia(via);
			indirizzo.setPersona(persona);
			personaId = (Long) session.save(indirizzo);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return personaId;
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
			Criteria criteria= session.createCriteria(Persona.class);
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
			List<Persona> persone = session.createQuery("select distinct p FROM Persona p inner join p.indirizzi").list();
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
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Persona.class);
			detachedCriteria.createAlias("indirizzi", "indirizzo");
			detachedCriteria.setProjection(Projections.distinct(Projections.id()));
			
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
}

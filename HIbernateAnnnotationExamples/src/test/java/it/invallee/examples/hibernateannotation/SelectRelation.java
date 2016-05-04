package it.invallee.examples.hibernateannotation;

import it.invallee.examples.hibernateannotation.hb.Persona;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.AliasToBeanResultTransformer;
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
	
	@Test
	public void listPersoneAlias() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Persona> persone = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
//			criteria.createCriteria("indirizzi", Criteria.LEFT_JOIN).setFetchMode("indirizzi", FetchMode.LAZY);
			criteria.createAlias("indirizzi", "indirizzi", Criteria.LEFT_JOIN);
			criteria.createAlias("indirizzi.tipoIndirizzo", "tipoIndirizzo", Criteria.LEFT_JOIN);
			
//			criteria.add(Restrictions.eq("tipoIndirizzo.idTipoIndirizzo", 1L));
//			criteria.setResultTransformer(Transformers.aliasToBean(Persona.class));
			
			criteria.setProjection(getProjectionList(factory, Persona.class));
			criteria.setResultTransformer(new AliasToBeanResultTransformer(Persona.class));
			
			persone = criteria.list();
			tx.commit();

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
	
	private ProjectionList getProjectionList(SessionFactory sessionFactory, Class entityClass){
		ProjectionList projectionList = Projections.projectionList();
	    String id = sessionFactory.getClassMetadata(entityClass)
	            .getIdentifierPropertyName();
	    projectionList.add(Projections.alias(Projections.property(id),id));
	    for (String prop: sessionFactory.getClassMetadata(entityClass).getPropertyNames()) {
	    	projectionList.add(Projections.alias(Projections.property(prop), prop));
	    }
	    return projectionList;
	}
	
}

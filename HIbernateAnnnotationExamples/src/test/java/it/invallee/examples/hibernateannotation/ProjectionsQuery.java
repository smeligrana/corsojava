package it.invallee.examples.hibernateannotation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.SetType;
import org.hibernate.type.Type;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.invallee.examples.hibernateannotation.hb.Persona;
import it.invallee.examples.hibernateannotation.hb.PersonaWrapper;
import it.invallee.examples.hibernateannotation.hb.RowCountOverProjection;

public class ProjectionsQuery {
	private static SessionFactory factory;

	@BeforeClass
	public static void setUp() {
		InitializerPostgresql initializer = new InitializerPostgresql();
		factory = initializer.setUp();
	}

	@Test
	public void listPersoneCriteriaWithProjections() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);

			RowCountOverProjection countOver = new RowCountOverProjection();
			
			criteria.setProjection(Projections.projectionList().add(Projections.property("cognome"), "cognome")
					.add(countOver, "count"));
			criteria.setResultTransformer(Transformers.aliasToBean(Persona.class));

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
	public void listPersoneIndirizzoCriteria() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Persona.class);
			criteria.createAlias("documentoIdentita", "documentoIdentita");
			// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			tx.commit();

			List<Persona> persone = criteria.list();
			for (Persona persona : persone) {
				System.out.println(persona);
				System.out.println(persona.getDocumentoIdentita());
			}

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
	public void listPersoneCriteriaWithProjections2() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProjectionList projectionList = Projections.projectionList();
			
			ClassMetadata metadata = factory.getClassMetadata(Persona.class);
			
			String idProperty = metadata.getIdentifierPropertyName();
			projectionList.add(Projections.alias(Projections.property(idProperty), idProperty));
			
			for(String property : metadata.getPropertyNames()){
				Type type= metadata.getPropertyType(property);
				System.out.println(property+"="+metadata.getPropertyType(property));
				if(!(type instanceof SetType)){
					projectionList.add(Projections.alias(Projections.property(property), property));
				}
			}
			
			RowCountOverProjection countOver = new RowCountOverProjection();
			projectionList.add(countOver, "count");
			
			Criteria criteria = session.createCriteria(Persona.class);
			criteria.setProjection(projectionList);

			criteria.setResultTransformer(new AliasToBeanResultTransformer(PersonaWrapper.class));
			
			List l = criteria.list();
			
			for (Object object : l) {
				System.out.println(object);
			}
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void listPersoneCriteriaWithProjections3() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Query q = session.createQuery("select p, countover() from Persona as p");
			
			List<Object[]> l = q.list();
			
			for (Object[] object : l) {
				System.out.println(object[0]+" "+ object[1]);
			}
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void listPersoneCriteriaWithProjections4() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Query q = session.createQuery("select p from Persona as p");
			
			List l = q.list();
			
			for (Object object : l) {
				System.out.println(object);
			}
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}

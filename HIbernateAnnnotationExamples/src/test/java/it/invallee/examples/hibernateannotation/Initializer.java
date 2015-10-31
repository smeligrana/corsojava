package it.invallee.examples.hibernateannotation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import it.invallee.examples.hibernateannotation.hb.Indirizzo;
import it.invallee.examples.hibernateannotation.hb.Persona;
import it.invallee.examples.hibernateannotation.hb.TipoIndirizzo;

public class Initializer {
	private static SessionFactory factory = null;

	public SessionFactory setUp() {
		if (factory == null) {
			// SessionFactory factory = null;
			try {
				factory = new Configuration().configure()
						// .addPackage("it.invallee.examples.hibernateannotation.hb")
						.addAnnotatedClass(Persona.class).addAnnotatedClass(Indirizzo.class)
						.addAnnotatedClass(TipoIndirizzo.class).buildSessionFactory();
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object." + ex);
				throw new ExceptionInInitializerError(ex);
			}

			Long idPersona1 = addPersona("Meligrana", "Sergio", factory);
			addIndirizzo("Neyran Dessus 65A", "Brissogne", idPersona1, factory);
			addIndirizzo("Via Che Guevara 7", "Drapia", idPersona1, factory);

			addPersona("Meligrana", "Alice", factory);
			addPersona("Meligrana", "Arianna", factory);

			System.out.println("Inizializzazione terminata");
		}
		return factory;
	}

	public Long addPersona(String cognome, String nome, SessionFactory factory) {
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

	public Long addIndirizzo(String comune, String via, Long idPersona, SessionFactory factory) {
		Session session = factory.openSession();
		Transaction tx = null;
		Long personaId = null;
		try {
			Persona persona = (Persona) session.get(Persona.class, idPersona);

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
}

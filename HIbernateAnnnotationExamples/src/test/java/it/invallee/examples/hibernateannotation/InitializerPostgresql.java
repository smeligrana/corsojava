package it.invallee.examples.hibernateannotation;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import it.invallee.examples.hibernateannotation.hb.DocumentoIdentita;
import it.invallee.examples.hibernateannotation.hb.Indirizzo;
import it.invallee.examples.hibernateannotation.hb.Persona;
import it.invallee.examples.hibernateannotation.hb.TipoIndirizzo;

public class InitializerPostgresql {
	private static SessionFactory factory = null;

	public SessionFactory setUp() {
		if (factory == null) {
			// SessionFactory factory = null;
			try {
				factory = new Configuration().configure("postgres-hibernate.cfg.xml")
						// .addPackage("it.invallee.examples.hibernateannotation.hb")
						.addAnnotatedClass(Persona.class).addAnnotatedClass(Indirizzo.class)
						.addAnnotatedClass(TipoIndirizzo.class).addAnnotatedClass(DocumentoIdentita.class)
						.buildSessionFactory();
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object." + ex);
				throw new ExceptionInInitializerError(ex);
			}

			Long idPersona1 = addPersona("Meligrana", "Sergio", null, factory);
			addIndirizzo("Neyran Dessus 65A", "Brissogne", idPersona1, factory);
			addIndirizzo("Via Che Guevara 7", "Drapia", idPersona1, factory);

			addPersona("Meligrana", "Alice", null, factory);
			addPersona("Meligrana", "Arianna", addDocumentoIdentita("123", new Date(), factory), factory);

			System.out.println("Inizializzazione terminata");
		}
		return factory;
	}

	public Long addPersona(String cognome, String nome, DocumentoIdentita documentoIdentita, SessionFactory factory) {
		Session session = factory.openSession();
		Transaction tx = null;
		Long personaId = null;
		try {
			tx = session.beginTransaction();
			Persona persona = new Persona();
			persona.setCognome(cognome);
			persona.setNome(nome);
			persona.setDocumentoIdentita(documentoIdentita);
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
	
	public DocumentoIdentita addDocumentoIdentita(String numero, Date dataRilascio, SessionFactory factory) {
		Session session = factory.openSession();
		Transaction tx = null;
		DocumentoIdentita documentoIdentita =null;
		try {
			tx = session.beginTransaction();
			documentoIdentita = new DocumentoIdentita();
			documentoIdentita.setNumero(numero);
			documentoIdentita.setDataRilascio(dataRilascio);
			session.save(documentoIdentita);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return documentoIdentita;
	}
}

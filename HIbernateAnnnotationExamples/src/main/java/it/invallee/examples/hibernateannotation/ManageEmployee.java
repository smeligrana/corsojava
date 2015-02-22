package it.invallee.examples.hibernateannotation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class ManageEmployee {
	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new AnnotationConfiguration()
					.configure()
					.
					// addPackage("com.xyz") //add package if used.
					addAnnotatedClass(Employee.class)
					.addAnnotatedClass(Indirizzo.class)
					.addAnnotatedClass(Tipo.class).buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		ManageEmployee ME = new ManageEmployee();

		/* Add few employee records in database */
		Integer empID1 = ME.addEmployee("Zara", "Ali", 1000);
		Integer empID2 = ME.addEmployee("Daisy", "Das", 5000);
		Integer empID3 = ME.addEmployee("John", "Paul", 10000);

		// /* List down all the employees */
		// ME.listEmployees();
		//
		// /* Update employee's records */
		// ME.updateEmployee(empID1, 5000);
		//
		// /* Delete an employee from the database */
		// ME.deleteEmployee(empID2);
		//
		// /* List down new list of the employees */
		// ME.listEmployees();

		// ME.listEmployeesGroupBy();

		// ME.list2Entity();

		ME.list2Entity2CriteriaBuilder();
	}

	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee();
			employee.setFirstName(fname);
			employee.setLastName(lname);
			employee.setSalary(salary);
			employeeID = (Integer) session.save(employee);

			Indirizzo indirizzo = new Indirizzo();
			indirizzo.setDescrizione("AAAA");
			indirizzo.setEmployee(employee);
			session.save(indirizzo);

			Indirizzo indirizzo2 = new Indirizzo();
			indirizzo2.setDescrizione("BBBB");
			indirizzo2.setEmployee(employee);
			session.save(indirizzo2);

			Tipo tipo = new Tipo();
			tipo.setDescrizione("tipo 1");
			tipo.setIndirizzo(indirizzo);
			session.save(tipo);

			Tipo tipo2 = new Tipo();
			tipo2.setDescrizione("tipo 2");
			tipo2.setIndirizzo(indirizzo);
			session.save(tipo2);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}

	/* Method to READ all the employees */
	public void listEmployees() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM Employee").list();
			for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print("First Name: " + employee.getFirstName());
				System.out.print("  Last Name: " + employee.getLastName());
				System.out.println("  Salary: " + employee.getSalary());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to UPDATE salary for an employee */
	public void updateEmployee(Integer EmployeeID, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class,
					EmployeeID);
			employee.setSalary(salary);
			session.update(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to DELETE an employee from the records */
	public void deleteEmployee(Integer EmployeeID) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class,
					EmployeeID);
			session.delete(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void listEmployeesGroupBy() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			DetachedCriteria grouped = DetachedCriteria
					.forClass(Employee.class, "grouped")
					.createAlias("indirizzi", "indirizzo")
					.setProjection(
							Projections.distinct(Projections.property("id")));
			// .setProjection(Projections.groupProperty("id"));

			Criteria criteria = session.createCriteria(Employee.class,
					"employee").add(
					Subqueries.propertyIn("employee.id", grouped));

			List employees = criteria.list();
			for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
				Employee employee = (Employee) iterator.next();
				System.out.print("Id: " + employee.getId());
				System.out.print(" First Name: " + employee.getFirstName());
				System.out.print("  Last Name: " + employee.getLastName());
				System.out.println("  Salary: " + employee.getSalary());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void list2Entity() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Criteria criteria = session.createCriteria(Employee.class,
			// "employee");
			// List list = criteria.list();

			String hql = "select employee, indirizzo from Employee employee, Indirizzo indirizzo";
			Query query = session.createQuery(hql);
			List list = query.list();

			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				System.out.println("Class: " + object.getClass());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void list2Entity2() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Criteria criteria = session.createCriteria(Employee.class,
			// "employee");
			// List list = criteria.list();

			StringBuilder hql = new StringBuilder();
			hql.append("select employee, tipo from Employee employee ");
			hql.append("inner join employee.indirizzi indirizzo ");
			hql.append("inner join indirizzo.tipi tipo ");
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				System.out.println("Class: " + Arrays.toString(object));
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void list2Entity2Criteria() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Employee.class,
					"employee").createCriteria("employee.indirizzi",
					"indirizzo").createCriteria("indirizzo.tipi", "tipo");

			List list = criteria.list();

			StringBuilder hql = new StringBuilder();
			hql.append("select employee, tipo from Employee employee ");
			hql.append("inner join employee.indirizzi indirizzo ");
			hql.append("inner join indirizzo.tipi tipo ");
			Query query = session.createQuery(hql.toString());
//			List list = query.list();

			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				System.out.println("Class: " + Arrays.toString(object));
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void list2Entity2CriteriaBuilder() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-pu");
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			
			CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
			
			Root<Employee> employeeRoot = criteria.from( Employee.class );
			Root<Tipo> tipoRoot = criteria.from( Tipo.class );
			
			Join<Employee, Indirizzo> joinIndirizzo = employeeRoot.join("indirizzi");
			Join<Indirizzo, Tipo> joinTipo =joinIndirizzo.join("tipi");
			
			criteria.multiselect(employeeRoot, tipoRoot);

			List<Tuple> list = em.createQuery(criteria).getResultList();

//			StringBuilder hql = new StringBuilder();
//			hql.append("select employee, tipo from Employee employee ");
//			hql.append("inner join employee.indirizzi indirizzo ");
//			hql.append("inner join indirizzo.tipi tipo ");
//			Query query = session.createQuery(hql.toString());
//			List list = query.list();

			for (Iterator<Tuple> iterator = list.iterator(); iterator.hasNext();) {
				Tuple object =(Tuple) iterator.next();
				System.out.println("Class: " + object.get(0));
				System.out.println("Class: " + object.get(1));
				System.out.println();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
//			session.close();
		}
	}

}

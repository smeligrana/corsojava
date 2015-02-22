package it.invallee.examples.hibernateannotation;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="indirizooOld")
@Table(name = "INDIRIZZO")
public class Indirizzo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private int id;

	@JoinColumn(name = "employee")
	@ManyToOne
	private Employee employee;

	@Column(name = "descrizione")
	private String descrizione;
	
	@OneToMany(mappedBy="indirizzo")
	private Set<Tipo> tipi;


	public Indirizzo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Set<Tipo> getTipi() {
		return tipi;
	}

	public void setTipi(Set<Tipo> tipi) {
		this.tipi = tipi;
	}

	
}

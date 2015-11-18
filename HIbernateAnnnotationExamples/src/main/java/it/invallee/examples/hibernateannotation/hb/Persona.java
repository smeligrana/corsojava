package it.invallee.examples.hibernateannotation.hb;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

@Entity @EntityListeners(PersonaListener.class)
@Table(name = "PERSONA")
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "idPersona")
	private Long idPersona;
	
	@Column(name = "cognome")
	private String cognome;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "cognome_nome")
	private String cognomeNome;
	
	@OneToMany(mappedBy="persona")
	@BatchSize(size=10)
	private Set<Indirizzo> indirizzi;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private DocumentoIdentita documentoIdentita;

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Set<Indirizzo> getIndirizzi() {
		return indirizzi;
	}

	public void setIndirizzi(Set<Indirizzo> indirizzi) {
		this.indirizzi = indirizzi;
	}
	
	public String getCognomeNome(){
		return cognomeNome;
	}
	
	protected void setCognomeNome(String cognomeNome){
		this.cognomeNome = cognomeNome;
	}

	@Override
	public String toString() {
		return "Persona [idPersona=" + idPersona + ", cognome=" + cognome
				+ ", nome=" + nome + ", cognomeNome="+cognomeNome+"]";
	}
	
	
}

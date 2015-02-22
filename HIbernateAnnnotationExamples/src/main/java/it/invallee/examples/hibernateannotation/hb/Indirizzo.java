package it.invallee.examples.hibernateannotation.hb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="INDIRIZZO")
public class Indirizzo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "idIndirizzo")
	private Long idIndirizzo;
	
	@ManyToOne
	@JoinColumn(name="idPersona")
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name="idTipoIndirizzo")
	private TipoIndirizzo tipoIndirizzo;
	
	@Column(name = "via")
	private String via;
	
	@Column(name = "comune")
	private String comune;

	public Long getIdIndirizzo() {
		return idIndirizzo;
	}

	public void setIdIndirizzo(Long idIndirizzo) {
		this.idIndirizzo = idIndirizzo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public TipoIndirizzo getTipoIndirizzo() {
		return tipoIndirizzo;
	}

	public void setTipoIndirizzo(TipoIndirizzo tipoIndirizzo) {
		this.tipoIndirizzo = tipoIndirizzo;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	@Override
	public String toString() {
		return "Indirizzo [idIndirizzo=" + idIndirizzo + ", persona=" + persona
				+ ", tipoIndirizzo=" + tipoIndirizzo + ", via=" + via
				+ ", comune=" + comune + "]";
	}
	
	
}


package it.invallee.examples.hibernateannotation.hb;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_INDIRIZZO")
public class TipoIndirizzo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "idTipoIndirizzo")
	private Long idTipoIndirizzo;

	@Column(name = "codice")
	private String codice;

	@Column(name = "descrizione")
	private String descrizione;

	@OneToMany(mappedBy = "tipoIndirizzo")
	private Set<Indirizzo> indirizzi;

	public Long getIdTipoIndirizzo() {
		return idTipoIndirizzo;
	}

	public void setIdTipoIndirizzo(Long idTipoIndirizzo) {
		this.idTipoIndirizzo = idTipoIndirizzo;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Set<Indirizzo> getIndirizzi() {
		return indirizzi;
	}

	public void setIndirizzi(Set<Indirizzo> indirizzi) {
		this.indirizzi = indirizzi;
	}

}

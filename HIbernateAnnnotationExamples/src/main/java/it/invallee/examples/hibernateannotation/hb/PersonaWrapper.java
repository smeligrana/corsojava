package it.invallee.examples.hibernateannotation.hb;

import java.util.Set;

public class PersonaWrapper {
	private Persona persona;
	private Long count;
	
	public PersonaWrapper(){
		persona = new Persona();
	}

	public Long getIdPersona() {
		return persona.getIdPersona();
	}

	public void setIdPersona(Long idPersona) {
		persona.setIdPersona(idPersona);
	}

	public String getCognome() {
		return persona.getCognome();
	}

	public void setCognome(String cognome) {
		persona.setCognome(cognome);
	}

	public String getNome() {
		return persona.getNome();
	}

	public void setNome(String nome) {
		persona.setNome(nome);
	}

	public Set<Indirizzo> getIndirizzi() {
		return persona.getIndirizzi();
	}

	public void setIndirizzi(Set<Indirizzo> indirizzi) {
		persona.setIndirizzi(indirizzi);
	}

	public void setCognomeNome(String cognomeNome) {
		persona.setCognomeNome(cognomeNome);
	}
	
	public String getCognomeNome() {
		return persona.getCognomeNome();
	}

	public DocumentoIdentita getDocumentoIdentita() {
		return persona.getDocumentoIdentita();
	}

	public void setDocumentoIdentita(DocumentoIdentita documentoIdentita) {
		persona.setDocumentoIdentita(documentoIdentita);
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "PersonaWrapper [persona=" + persona + ", count=" + count + "]";
	}

	
	
}

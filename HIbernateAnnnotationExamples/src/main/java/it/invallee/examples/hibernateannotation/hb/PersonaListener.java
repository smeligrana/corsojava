package it.invallee.examples.hibernateannotation.hb;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PersonaListener {

	@PreUpdate
    @PrePersist
    public void setLastUpdate(Persona persona) {
		System.out.println("************************Your audit code here");
        persona.setCognomeNome(persona.getCognome()+" "+persona.getNome());
    }
}

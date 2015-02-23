package corso.rubrica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import corso.rubrica.bean.Contatto;
import corso.rubrica.helpers.ContattiHelper;

public class ExecutorCmd {
	private List<Contatto> contatti = new ArrayList<>();

	public String execute(String cmdLine){
		String rtn = null;
		String[] arguments = cmdLine.split(" ");
		
		switch (arguments[0]) {
		case "add":
			rtn = add(arguments);
			break;
		
		case "list":
			rtn = list(arguments);
			break;
		
		case "help":
			rtn = help(arguments);
			break;
		
		case "delete":
			rtn = delete(arguments);
			break;
		
		case "save":
			rtn = save(arguments);
			break;
			
		case "read":
			rtn = read(arguments);
			break;

		default:
			rtn = "errore comando sconosciuto";
			break;
		}
		
		return rtn;
	}
	
	private String read(String[] arguments) {
		String msg;
		try {
			contatti = ContattiHelper.read( "contatti.xml");
			msg = "Lettura dei contatti da filesystem avventa con successo";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Errore durante la lettura dei contatti da filesystem";
		}
		return msg;
	}

	private String save(String[] arguments) {
		String msg;
		try {
			ContattiHelper.write(contatti, "contatti.xml");
			msg = "Salvataggio avvenuto con successo";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Errore durante il salvataggio";
		}
		return msg;
	}

	private String list(String[] arguments) {
		StringBuilder sb = new StringBuilder();
		sb.append("Elenco contatti: \n");
		
		for (Contatto contatto : contatti) {
			sb.append(" nome: ").append(contatto.getNome());
			sb.append(" cognome: ").append(contatto.getCognome());
			sb.append(" numero: ").append(contatto.getNumero());
			sb.append("\n");
		}
		
		
		return sb.toString();
	}

	private String help(String[] arguments) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Elenco dei comandi \n");
		sb.append("\t add cognome nome numero \n");
		sb.append("\t delete cognome\n");
		sb.append("\t list \n");
		sb.append("\t save \n");
		sb.append("\t read \n");
		sb.append("\t quit \n");
		
		return sb.toString();
	}

	private String add(String[] arguments){
		String rtn;
		if(arguments.length != 4){
			return  "Errore nei parametri";
		}
		
		Contatto contatto = new Contatto(arguments[2], arguments[1], arguments[3]);
		contatti.add(0, contatto);
		rtn = "Ok, contatto aggiunto con successo";
		
		return rtn;
	}
	
	private String delete(String[] arguments){
		String rtn;
		if(arguments.length != 2){
			return  "Errore nei parametri";
		}
		
		boolean cancellato = false;
		for (int i=0; i< contatti.size(); i++) {
			Contatto contatto = contatti.get(i);
			if(contatto.getCognome().equals(arguments[1])){
				contatti.remove(contatto);
				cancellato = true;
				i--;
			}
		}
//		Iterator<Contatto> it = contatti.iterator();
//		while (it.hasNext()) {
//			Contatto contatto = it.next();
//			if(contatto.getCognome().equals(arguments[1])){
//				it.remove();
//				cancellato = true;
//			}
//		}
		
		if(cancellato){
			rtn = "Ok, contatto cancellato con successo";
		} else {
			rtn = "contatto non trovato";
		}
		
		return rtn;
	}
}

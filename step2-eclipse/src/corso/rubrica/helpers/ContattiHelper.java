package corso.rubrica.helpers;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import corso.rubrica.bean.Contatto;

public class ContattiHelper {
	public static void write(List<Contatto> contatti, String filename)
			throws Exception {
		
		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(filename)));
		encoder.writeObject(contatti);
		encoder.close();
	}

	public static List<Contatto> read(String filename) throws Exception {
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
				new FileInputStream(filename)));
		List<Contatto> contatti = (List<Contatto>) decoder.readObject();
		decoder.close();
		return contatti;
	}
}

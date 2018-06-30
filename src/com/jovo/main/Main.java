package com.jovo.main;

import java.util.HashMap;

import javax.swing.JOptionPane;

import com.jovo.gui.GlavniProzor;

public class Main {

	public static void main(String[] args) {
		HashMap<StanjeAkcija, Double> qVrednosti = RadSaPodacima.ucitajStanjaAkcijeIQVrednostiIzFajla();
		if(qVrednosti == null) {
			JOptionPane.showMessageDialog(null, "Problem sa ucitavanjem podataka!", "Greska", JOptionPane.ERROR_MESSAGE);
		}
		else {
			TipPolja igracNaPotezu = TipPolja.PLAVO;
			Controller con = new Controller(igracNaPotezu, qVrednosti);
			GlavniProzor gp = new GlavniProzor(con);
			gp.setVisible(true);
		}

	}

}

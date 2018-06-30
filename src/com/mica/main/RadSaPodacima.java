package com.mica.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class RadSaPodacima {
	
	private static String nazivFajlaZaPodatke = "podaci.txt";
	
	public static void sacuvajStanjaAkcijeIQVrednostiUFajl(HashMap<StanjeAkcija, Integer> qVrednosti) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(nazivFajlaZaPodatke));
			
			pw.println("# stanje | akcija | q vrednost");
			pw.println();
			
			for (StanjeAkcija sa : qVrednosti.keySet()) {
				pw.println(sa + "|" + qVrednosti.get(sa));
			}
			
			pw.close();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Problem sa cuvanjem podataka!", "Greska", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	@SuppressWarnings("resource")
	public static HashMap<StanjeAkcija, Double> ucitajStanjaAkcijeIQVrednostiIzFajla() {
		HashMap<StanjeAkcija, Double> qVrednosti = new HashMap<StanjeAkcija, Double>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(nazivFajlaZaPodatke));
			
			String linija;
			String[] tokeni;
			String[] tokeniStanje;
			
			while((linija = br.readLine()) != null) {
				linija = linija.trim();
				
				if(linija.startsWith("#") || linija.equals("")) continue;
				
				tokeni = linija.split("\\|");
				if(tokeni.length != 3) return null;
				else {
					tokeniStanje = tokeni[0].trim().split(";");
					if(tokeniStanje.length != 4) return null;
					
					Stanje stanje = Stanje.kreirajStanje(tokeniStanje);
					if(stanje == null) return null;
					
					Akcija akcija = Akcija.valueOf(tokeni[1]);
					StanjeAkcija sa = new StanjeAkcija(stanje, akcija);
					double qVrednost = Double.parseDouble(tokeni[2]);
					qVrednosti.put(sa, qVrednost);
					
				}
			}
			
			br.close();
			
			return qVrednosti;
		}
		catch(Exception e) {
			return null;
		}

	}
}

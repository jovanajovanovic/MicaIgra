package com.mica.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class RadSaPodacima {
	
	private static String nazivFajlaZaPodatke = "podaci.txt";
	private static String nazivFajlaZaBrojMenjanjaQvrednosti = "broj_menjanja_q_vrednosti.txt";
	private static String nazivFajlaZaRezultate = "rezultati.txt";
	
	public static void sacuvajStanjaAkcijeIQVrednostiUFajl(HashMap<StanjeAkcija, Double> qVrednosti) {
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
					tokeniStanje = tokeni[0].trim().split(":");
					if(tokeniStanje.length != 4) return null;
					
					Stanje stanje = Stanje.kreirajStanje(tokeniStanje);
					if(stanje == null) return null;
					stanje.podesiPoljaUTaramaIVanNjih();
					
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
	
	
	public static void sacuvajStanjaAkcijeIBrojIzmenaUFajl(HashMap<StanjeAkcija, Integer> brojMenjanjaQVrednosti) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(nazivFajlaZaBrojMenjanjaQvrednosti));
			
			pw.println("# stanje | akcija | broj menjanja q vrednosti");
			pw.println();
			
			for (StanjeAkcija sa : brojMenjanjaQVrednosti.keySet()) {
				pw.println(sa + "|" + brojMenjanjaQVrednosti.get(sa));
			}
			
			pw.close();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Problem sa cuvanjem podataka!", "Greska", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	@SuppressWarnings("resource")
	public static HashMap<StanjeAkcija, Integer> ucitajStanjaAkcijeIBrojIzmenaIzFajla() {
		HashMap<StanjeAkcija, Integer> brojMenjanjaQVrednosti = new HashMap<StanjeAkcija, Integer>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(nazivFajlaZaBrojMenjanjaQvrednosti));
			
			String linija;
			String[] tokeni;
			String[] tokeniStanje;
			
			while((linija = br.readLine()) != null) {
				linija = linija.trim();
				
				if(linija.startsWith("#") || linija.equals("")) continue;
				
				tokeni = linija.split("\\|");
				if(tokeni.length != 3) return null;
				else {
					tokeniStanje = tokeni[0].trim().split(":");
					if(tokeniStanje.length != 4) return null;
					
					Stanje stanje = Stanje.kreirajStanje(tokeniStanje);
					if(stanje == null) return null;
					
					Akcija akcija = Akcija.valueOf(tokeni[1]);
					StanjeAkcija sa = new StanjeAkcija(stanje, akcija);
					int brojIzmena = Integer.parseInt(tokeni[2]);
					brojMenjanjaQVrednosti.put(sa, brojIzmena);
					
				}
			}
			
			br.close();
			
			return brojMenjanjaQVrednosti;
		}
		catch(Exception e) {
			return null;
		}

	}
	
	
	public static void upisiKrajnjiRezultatUFajl(Rezultat rezultat) {
		try {
			String tekstZaUpis = "POBEDNIK: " + rezultat.getPobednik() + ", " + rezultat.getAlgoritamPobednika() 
			+ "  |  GUBITNIK: " + rezultat.getGubitnik() + ", " + rezultat.getAlgoritamGubitnika() + "  |  BROJ_POTEZA: " + rezultat.getBrojPoteza() +"\n";
			
		    Files.write(Paths.get(nazivFajlaZaRezultate), tekstZaUpis.getBytes(), StandardOpenOption.APPEND);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem sa rezultatima!", "Greska", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
}

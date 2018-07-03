package com.mica.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

import com.mica.main.Akcija;
import com.mica.main.Akcije;
import com.mica.main.Controller;
import com.mica.main.Polje;
import com.mica.main.PoljeAkcija;
import com.mica.main.Potez;
import com.mica.main.Pozicija;
import com.mica.main.QVrednostSelektovanoPoljeAkcija;
import com.mica.main.RadSaPodacima;
import com.mica.main.Stanje;
import com.mica.main.StanjeAkcija;
import com.mica.main.TipPolja;

public class ReinforcementLearning {
	private HashMap<StanjeAkcija, Double> qVrednosti;
	private HashMap<StanjeAkcija, Integer> brojMenjanjaQVrednosti;
	private double epsilon; 
	private double zanemarivanje =  0.9;
	
	private Random random = new Random();

	public ReinforcementLearning() {
		this.qVrednosti = RadSaPodacima.ucitajStanjaAkcijeIQVrednostiIzFajla();
		this.brojMenjanjaQVrednosti = RadSaPodacima.ucitajStanjaAkcijeIBrojIzmenaIzFajla();
		
		if(this.qVrednosti == null) {
			this.qVrednosti = new HashMap<StanjeAkcija, Double>();
			JOptionPane.showMessageDialog(null, "Problem sa ucitavanjem podataka - qvrednosti!", "Greska", JOptionPane.ERROR_MESSAGE);
		}
		if(this.brojMenjanjaQVrednosti == null) {
			this.brojMenjanjaQVrednosti = new HashMap<StanjeAkcija, Integer>();
			JOptionPane.showMessageDialog(null, "Problem sa ucitavanjem podataka - brojMenjanjaQVrednosti!", "Greska", JOptionPane.ERROR_MESSAGE);
		}
		
		this.epsilon = 0.3;
	}
	
	public HashMap<StanjeAkcija, Double> getqVrednosti() {
		return qVrednosti;
	}

	public void setqVrednosti(HashMap<StanjeAkcija, Double> qVrednosti) {
		this.qVrednosti = qVrednosti;
	}

	public HashMap<StanjeAkcija, Integer> getBrojMenjanjaQVrednosti() {
		return brojMenjanjaQVrednosti;
	}

	public void setBrojMenjanjaQVrednosti(HashMap<StanjeAkcija, Integer> brojMenjanjaQVrednosti) {
		this.brojMenjanjaQVrednosti = brojMenjanjaQVrednosti;
	}

	public boolean baciNovcic(double epsilon) {	
	    return this.random.nextFloat() < epsilon;
	}
	
	public ArrayList<PoljeAkcija> getMogucaSelektovanaPoljaIAkcijeZaDatoStanje(Stanje stanje) {
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = new ArrayList<PoljeAkcija>();
		
		Polje[][] polja = stanje.getPolja();
		Polje selektovanoPolje;
		int indeksAkcijePostavljanja;
			 
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				selektovanoPolje = polja[i][j];
				
				if(stanje.daLiSuSveFigurePostavljene()) {
					if(selektovanoPolje.getTipPolja() == stanje.getIgracNaPotezu()) {
						if(stanje.getPlaviIgrac().getBrojPreostalihFigura() == 3) {
							proveriDaLiSuSkokoviMoguci(stanje.getPolja(), mogucaSelektovanaPoljaIAkcije, selektovanoPolje);
						}
						proveriDaLiSuGDLDAkcijeMoguce(stanje.getPolja(), mogucaSelektovanaPoljaIAkcije, selektovanoPolje);
					}
				}
				else {
					if(selektovanoPolje.getTipPolja() == TipPolja.ZUTO) {
						indeksAkcijePostavljanja = selektovanoPolje.getPozicija().getX()*Controller.BROJ_POLJA_U_KRUGU + selektovanoPolje.getPozicija().getY();
						mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.POSTAVLJANJE[indeksAkcijePostavljanja]));
					}
				}
				
			}
		}
		
		return mogucaSelektovanaPoljaIAkcije;
	}
	
	private void proveriDaLiSuGDLDAkcijeMoguce(Polje[][] polja, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije, Polje selektovanoPolje) {
		Pozicija pozicija, koraci;
		int slojZaNovoPolje, indeksUSlojuZaNovoPolje;
		
		for(Akcija akcija: Akcije.GDLD) {
			pozicija = selektovanoPolje.getPozicija();
			if(pozicija.getY()% 2 == 0 && (akcija == Akcija.GORE || akcija == Akcija.DOLE)) continue; 
			
			koraci = Akcije.mapiranjeGDLDAkcijaNaKorake.get(akcija);
			slojZaNovoPolje = pozicija.getX() + koraci.getX();
			if( slojZaNovoPolje < 0 || slojZaNovoPolje > (Controller.BROJ_KRUGOVA-1)) continue;
			
			indeksUSlojuZaNovoPolje = (pozicija.getY() + koraci.getY()) % Controller.BROJ_POLJA_U_KRUGU;
			if (indeksUSlojuZaNovoPolje == -1) indeksUSlojuZaNovoPolje = Controller.BROJ_POLJA_U_KRUGU - 1;
			
			if(polja[slojZaNovoPolje][indeksUSlojuZaNovoPolje].getTipPolja() != TipPolja.ZUTO) continue;
			
			mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, akcija));
		}
		
	}


	private void proveriDaLiSuSkokoviMoguci(Polje[][] polja, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije, Polje selektovanoPolje) {
		for (int k = 0; k < Akcije.SKOKOVI.length; k++) {
			if(polja[k/Controller.BROJ_POLJA_U_KRUGU][k%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() != TipPolja.ZUTO) {
				continue;
			}
			
			mogucaSelektovanaPoljaIAkcije.add(new PoljeAkcija(selektovanoPolje, Akcije.SKOKOVI[k]));
		}
		
	}
	
	public PoljeAkcija getSelektovanoPoljeIAkcija(Stanje stanje) {
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = getMogucaSelektovanaPoljaIAkcijeZaDatoStanje(stanje);
		if(mogucaSelektovanaPoljaIAkcije.isEmpty()) return null;
		
		
        if (baciNovcic(this.epsilon)) {
        	int randomIndeks = this.random.nextInt(mogucaSelektovanaPoljaIAkcije.size());
        	
        	return mogucaSelektovanaPoljaIAkcije.get(randomIndeks);
        }
            
        return getAkcijaPoPolotici(stanje, mogucaSelektovanaPoljaIAkcije);
	}
	
	
	public PoljeAkcija getAkcijaPoPolotici(Stanje stanje, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije) {
		return izracunajNajboljuAkcijuPoQvrednostima(stanje, mogucaSelektovanaPoljaIAkcije).getSelektovanoPoljeAkcija();
	}
	
	
	public void postaviNovuQVrednost(Stanje stanje, Akcija akcija, Stanje sledeceStanje, double nagrada) {
		StanjeAkcija stanjeAkcija = new StanjeAkcija(stanje, akcija);
		System.out.println(stanjeAkcija.hashCode());
		double alfa;
		Integer n = this.brojMenjanjaQVrednosti.get(stanjeAkcija);
		if(n == null) {
			alfa = 1;
			n = 0;
		}
		else {	
			alfa = 1 / n;
		}
		
		ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije = getMogucaSelektovanaPoljaIAkcijeZaDatoStanje(sledeceStanje);
		QVrednostSelektovanoPoljeAkcija maksimumQVrednostSelektovanoPoljeAkcija = izracunajNajboljuAkcijuPoQvrednostima(sledeceStanje, mogucaSelektovanaPoljaIAkcije);
		double novaQVrednost = (1 - alfa) * getQVrednost(stanje, akcija) + alfa * (nagrada + this.zanemarivanje*maksimumQVrednostSelektovanoPoljeAkcija.getqVrednost());

		this.qVrednosti.put(stanjeAkcija, novaQVrednost);
		this.brojMenjanjaQVrednosti.put(stanjeAkcija, n+1);
	}
		
	public QVrednostSelektovanoPoljeAkcija izracunajNajboljuAkcijuPoQvrednostima(Stanje stanje, ArrayList<PoljeAkcija> mogucaSelektovanaPoljaIAkcije) {
		double maksimumQVrednost = -9999;
		double qVrednost;
        PoljeAkcija maksimumSelektovanoPoljeAkcija = null;

        for(PoljeAkcija selektovanoPoljeAkcija : mogucaSelektovanaPoljaIAkcije) {
        	stanje.getPlaviIgrac().setSelektovanoPolje(selektovanoPoljeAkcija.getPolje());
        	qVrednost = getQVrednost(stanje, selektovanoPoljeAkcija.getAkcija());
            if (qVrednost > maksimumQVrednost) {
            	maksimumQVrednost = qVrednost;
            	maksimumSelektovanoPoljeAkcija = selektovanoPoljeAkcija;
            }

        }

        return new QVrednostSelektovanoPoljeAkcija(maksimumQVrednost, maksimumSelektovanoPoljeAkcija);
	}

	public double getQVrednost(Stanje stanje, Akcija akcija) {
		StanjeAkcija key = new StanjeAkcija(stanje, akcija);
		
		if(this.qVrednosti.containsKey(key)) {
			System.out.println("Pronasao vec postojecu qvrednost");
			return this.qVrednosti.get(key);
		}
		
		return 0;
	}
	
	public Potez noviPotez(Stanje stanje) {
		Polje[][] polja = stanje.getPolja();
		
		PoljeAkcija selektovanoPoljeAkcija = getSelektovanoPoljeIAkcija(stanje);
		Akcija akcija = selektovanoPoljeAkcija.getAkcija();
		String akcijaName = akcija.name();
		Polje polje;
		Polje selektovanoPolje;
		
		if(akcijaName.contains("POSTAVI")) {
			selektovanoPolje = null;
			polje = selektovanoPoljeAkcija.getPolje();
		}
		else {
			selektovanoPolje = selektovanoPoljeAkcija.getPolje();
			
			if(akcijaName.contains("SKOK")) {
				int indeks = Integer.parseInt(akcijaName.split("_")[1]);
				polje = polja[indeks/Controller.BROJ_POLJA_U_KRUGU][indeks%Controller.BROJ_POLJA_U_KRUGU];
			}
			else { // GORE, DOLE, LEVO, DESNO
				Pozicija koraci = Akcije.mapiranjeGDLDAkcijaNaKorake.get(akcija);
				
				int slojZaNovoPolje = selektovanoPolje.getPozicija().getX() + koraci.getX();
				int indeksUSlojuZaNovoPolje = (selektovanoPolje.getPozicija().getY() + koraci.getY()) %Controller.BROJ_POLJA_U_KRUGU;
				if(indeksUSlojuZaNovoPolje == -1) indeksUSlojuZaNovoPolje = Controller.BROJ_POLJA_U_KRUGU - 1;
				try {
					polje = polja[slojZaNovoPolje][indeksUSlojuZaNovoPolje];
				}
				catch (Exception e) {
					System.err.println(e.getMessage() + " (selektovano polje: ("+ selektovanoPolje.getPozicija().getX() + "," + selektovanoPolje.getPozicija().getY() +"), akcija" 
							+ akcija.name() + " polje: (" + slojZaNovoPolje +","  + indeksUSlojuZaNovoPolje + ")" );
					return null;
				}
				
			}
		}
		
		return new Potez(polje, selektovanoPolje, akcija);
	}
	
}

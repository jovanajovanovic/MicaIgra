package com.mica.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

import com.mica.main.Akcija;
import com.mica.main.QVrednostAkcija;
import com.mica.main.RadSaPodacima;
import com.mica.main.Stanje;
import com.mica.main.StanjeAkcija;

public class ReinforcementLearning {
	private HashMap<StanjeAkcija, Double> qVrednosti;
	private double epsilon; 
	private double zanemarivanje =  0.9;
	
	private Random random = new Random();

	public ReinforcementLearning() {
		this.qVrednosti = RadSaPodacima.ucitajStanjaAkcijeIQVrednostiIzFajla();
		if(qVrednosti == null) {
			qVrednosti = new HashMap<StanjeAkcija, Double>();
			JOptionPane.showMessageDialog(null, "Problem sa ucitavanjem podataka!", "Greska", JOptionPane.ERROR_MESSAGE);
		}
		this.epsilon = 0.1;
	}
	
	
	public boolean baciNovcic(double epsilon) {	
	    return this.random.nextFloat() < epsilon;
	}
	
	public ArrayList<Akcija> getMoguceAkcijeZaDatoStanje(Stanje stanje) {
		ArrayList<Akcija> akcije = new ArrayList<Akcija>();
		
		/*int pocetniIndeks, krajnjiIndeks;
		
		if(daLiSuSveFigurePostavljene()) {
			pocetniIndeks = 24;
			
			if(stanje.getPlaviIgrac().getBrojPreostalihFigura() <= 3) {
				krajnjiIndeks = 51;
			}
			else {
				krajnjiIndeks = 27;
			}
		}
		else {
			pocetniIndeks = 0;
			krajnjiIndeks = 23;
			
			for (int i = pocetniIndeks; i <= krajnjiIndeks; i++) {
				if(stanje.getPolja()[i/BROJ_POLJA_U_KRUGU][i%BROJ_POLJA_U_KRUGU].getTipPolja() == TipPolja.ZUTO) {
					novoStanje = new Stanje(stanje);
					novoStanje.getPolja()[i/BROJ_POLJA_U_KRUGU][i%BROJ_POLJA_U_KRUGU].setTipPolja(stanje.getIgracNaPotezu());
				}
			}
			
		}*/
		
		return akcije;
	}
	
	public Akcija getAkcija(Stanje stanje) {
		ArrayList<Akcija> moguceAkcije = getMoguceAkcijeZaDatoStanje(stanje);
		if(moguceAkcije.isEmpty()) return null;
		
		
        if (baciNovcic(this.epsilon)) {
           int indeks = this.random.nextInt(moguceAkcije.size());         
           return moguceAkcije.get(indeks);
        }
            
        return getAkcijaPoPolotici(moguceAkcije);
	}
	
	
	public Akcija getAkcijaPoPolotici(ArrayList<Akcija> moguceAkcije) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void postaviNovuQVrednost(Stanje stanje, Akcija akcija, Stanje sledeceStanje, int nagrada) {
        /*
        The parent class calls this to observe a
        state = action => nextState and reward transition.
        You should do your Q-Value update here

        NOTE: You should never call this function,
        it will be called on your behalf
      */
      // YOUR CODE HERE
	
		double alfa = 1; //TODO
		ArrayList<Akcija> moguceAkcijeIzSledecegStanja = getMoguceAkcijeZaDatoStanje(sledeceStanje);
		QVrednostAkcija maksimumQVrednostAkcija =  getMaksimalnaQVrednostIAkcija(sledeceStanje, moguceAkcijeIzSledecegStanja);
		double novaQVrednost = (1 - alfa) * getQVrednost(stanje, akcija) + alfa * (nagrada + this.zanemarivanje*maksimumQVrednostAkcija.getqVrednost());

		this.qVrednosti.put(new StanjeAkcija(stanje, akcija), novaQVrednost);
	}
		
	public QVrednostAkcija  getMaksimalnaQVrednostIAkcija(Stanje stanje, ArrayList<Akcija> moguceAkcije) {
		/*int pocetniIndeks, krajnjiIndeks;
		
		if(daLiSuSveFigurePostavljene()) {
			pocetniIndeks = 24;
			
			if(trenutnoStanje.getPlaviIgrac().getBrojPreostalihFigura() <= 3) {
				krajnjiIndeks = 51;
			}
			else {
				krajnjiIndeks = 27;
			}
		}
		else {
			pocetniIndeks = 0;
			krajnjiIndeks = 23;
		}
		
		
		Stanje novoStanje;
		
		for (int i = pocetniIndeks; i <= krajnjiIndeks; i++) {
					if(trenutnoStanje.getPolja()[i/BROJ_POLJA_U_KRUGU][i%BROJ_POLJA_U_KRUGU].getTipPolja() == TipPolja.ZUTO) {
						novoStanje = new Stanje(trenutnoStanje);
						novoStanje.getPolja()[i/BROJ_POLJA_U_KRUGU][i%BROJ_POLJA_U_KRUGU].setTipPolja(trenutnoStanje.getIgracNaPotezu());
					}
				
			}
			
			if(Akcija.values()[i].)
		}*/
		
		
		double maksimumQVrednost = -9999;
		double qVrednost;
        Akcija maksimumAkcija = null;

        for(Akcija akcija : moguceAkcije) {
        	qVrednost = getQVrednost(stanje, akcija);
            if (qVrednost > maksimumQVrednost) {
            	maksimumQVrednost = qVrednost;
                maksimumAkcija = akcija;
            }

        }

        return new QVrednostAkcija(maksimumQVrednost, maksimumAkcija);
	}

	public double getQVrednost(Stanje stanje, Akcija akcija) {
		StanjeAkcija key = new StanjeAkcija(stanje, akcija);
		
		if(this.qVrednosti.containsKey(key)) {
			return this.qVrednosti.get(key);
		}
		
		return 0;
	}
	
}

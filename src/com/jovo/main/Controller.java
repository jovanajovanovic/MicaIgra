package com.jovo.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.jovo.gui.Polje;
import com.jovo.gui.PomocniPanel;

public class Controller {
	public static final int BROJ_KRUGOVA = 3;
	public static final int BROJ_POLJA_U_KRUGU = 8;
	public static final int BROJ_FIGURA = 9;
	
	private double zanemarivanje =  0.9f;
	
	private boolean pojediPlavog;
	private boolean pojediCrvenog;
	
	String pobednik = null;
	
	private PomocniPanel pomocniPanel;
	
	private Stanje trenutnoStanje;
	
	private HashMap<StanjeAkcija, Double> qVrednosti;
	private double epsilon; 
	
	private Random random = new Random();
	
	public Controller(TipPolja igracNaPotezu, HashMap<StanjeAkcija, Double> qVrednosti) {
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		this.pojediPlavog = false;
		this.pojediCrvenog = false;
		
		this.qVrednosti = qVrednosti;
		this.epsilon = 0.1;
		
		
	}
	
	public boolean daLiSuSveFigurePostavljene() {
		return trenutnoStanje.getPlaviIgrac().getBrojNepostavljenihFigura() == 0 && trenutnoStanje.getCrveniIgrac().getBrojNepostavljenihFigura() == 0;
	}

	public boolean krajIgre() {
		Polje[][] polja = trenutnoStanje.getPolja();
		TipPolja igracNaPotezu = trenutnoStanje.getIgracNaPotezu();
		Igrac crveniIgrac = trenutnoStanje.getCrveniIgrac();
		Igrac plaviIgrac = trenutnoStanje.getPlaviIgrac();
		
		if (plaviIgrac.getBrojPreostalihFigura() == 2) {
			pobednik = "CRVENI";
			return true;
		}
		else if(crveniIgrac.getBrojPreostalihFigura() == 2) {
			pobednik = "PLAVI";
			return true;
		}
		
		
		Igrac igrac;
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) {
			igrac = plaviIgrac;
		}
		else {
			igrac = crveniIgrac;
		}
		
		
		if(igrac.getBrojPreostalihFigura() <= 3) {
			return false;
		}
		
		// moze li se protivnik mrdati?
		for (int i = 0; i < polja.length; i++) {
			for (int j = 0; j < polja[i].length; j++) {
				if(polja[i][j].getTipPolja() != igracNaPotezu) continue;
				
				if(j % 2 == 1) {
					if(polja[i][(j+1) % polja[i].length].getTipPolja() == TipPolja.ZUTO || polja[i][j-1].getTipPolja() == TipPolja.ZUTO) {
						return false;
					}
					
					if(i == 0 || i == 2) {
						if(polja[1][j].getTipPolja() == TipPolja.ZUTO) {
							return false;
						}
					}
					
					if(i == 1) {
						if(polja[0][j].getTipPolja() == TipPolja.ZUTO || polja[2][j].getTipPolja() == TipPolja.ZUTO) {
							return false;
						}
					}
				}
				else {
					int a = j-1;
					if(a < 0) {
						a += polja[i].length;
					}
					if(polja[i][j+1].getTipPolja() == TipPolja.ZUTO || polja[i][a].getTipPolja() == TipPolja.ZUTO ) {
						return false;
					}
				}
			}
		}
		
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) pobednik = "CRVENI";
		else pobednik = "PLAVI";

		
		return true;
	}
	
	public String getPobednik() {
		return pobednik;
	}
	
	public void resetujSveZaNovuIgru() {
		TipPolja igracNaPotezu = TipPolja.PLAVO;
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		pomocniPanel.resetujSveZaNovuIgru(trenutnoStanje);
	}

	public void noviPotez() {
		TipPolja igracNaPotezu = trenutnoStanje.getIgracNaPotezu();
		Igrac crveniIgrac = trenutnoStanje.getCrveniIgrac();
		Igrac plaviIgrac = trenutnoStanje.getPlaviIgrac();
		
		if (igracNaPotezu == TipPolja.PLAVO) {
			if (!daLiSuSveFigurePostavljene()) plaviIgrac.umanjiBrojNepostavljenihFigura();
			pomocniPanel.setBrojPlavihNepostavljenihFigura(plaviIgrac.getBrojNepostavljenihFigura());
			
			igracNaPotezu = TipPolja.CRVENO;
			trenutnoStanje.setIgracNaPotezu(igracNaPotezu);
			pomocniPanel.setNaPotezu(igracNaPotezu);
		}
		else if(igracNaPotezu == TipPolja.CRVENO) {
			if (!daLiSuSveFigurePostavljene()) crveniIgrac.umanjiBrojNepostavljenihFigura();
			pomocniPanel.setBrojCrvenihNepostavljenihFigura(crveniIgrac.getBrojNepostavljenihFigura());
			
			igracNaPotezu = TipPolja.PLAVO;
			trenutnoStanje.setIgracNaPotezu(igracNaPotezu);
			pomocniPanel.setNaPotezu(igracNaPotezu);
		}
		
		
	}
	
	public boolean daLiJeIspravanPotez(Pozicija staraPozicija, Pozicija novaPozicija) {
		if (staraPozicija.equals(novaPozicija)) {
			return false;
		}
		
		// skakanje kad neki igrac ostane sa tri figure
		if((trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO && trenutnoStanje.getPlaviIgrac().getBrojPreostalihFigura() == 3) || (trenutnoStanje.getIgracNaPotezu() == TipPolja.CRVENO && trenutnoStanje.getCrveniIgrac().getBrojPreostalihFigura() == 3)) {
			
			return true;
			
		}
		
		if(staraPozicija.getX() != novaPozicija.getX()) {
			if(Math.abs(staraPozicija.getX() - novaPozicija.getX()) == 1) {
				if(staraPozicija.getY() == novaPozicija.getY()) {
					return true;
				}
			}
			
		}
		else {
			if(Math.abs(staraPozicija.getY() - novaPozicija.getY()) == 1) {
				return true;
			}
			if((staraPozicija.getY() == 0 && novaPozicija.getY() == 7) || (staraPozicija.getY() == 7 && novaPozicija.getY() == 0)) {
				return true;
			}
		}
		
		return false;
	}
	
	public PomocniPanel getPomocniPanel() {
		return pomocniPanel;
	}
	
	public void setPomocniPanel(PomocniPanel pomocniPanel) {
		this.pomocniPanel = pomocniPanel;
	}

	public boolean isPojediPlavog() {
		return pojediPlavog;
	}

	public void setPojediPlavog(boolean pojediPlavog) {
		this.pojediPlavog = pojediPlavog;
	}

	public boolean isPojediCrvenog() {
		return pojediCrvenog;
	}

	public void setPojediCrvenog(boolean pojediCrvenog) {
		this.pojediCrvenog = pojediCrvenog;
	}
	
	public Stanje getTrenutnoStanje() {
		return trenutnoStanje;
	}

	public void setTrenutnoStanje(Stanje trenutnoStanje) {
		this.trenutnoStanje = trenutnoStanje;
	}

	public void spremiJedenje() {
		String figuraZaJedenje;
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			figuraZaJedenje = "CRVENU";
			pojediCrvenog = true;
		}
		else {
			figuraZaJedenje = "PLAVU";
			pojediPlavog = true;
		}
		
		pomocniPanel.setPojediLabel(figuraZaJedenje);
		pomocniPanel.getPojediLabel().setVisible(true);
	}
	
	public void zavrsiJedenje() {
		Igrac crveniIgrac = trenutnoStanje.getCrveniIgrac();
		Igrac plaviIgrac = trenutnoStanje.getPlaviIgrac();
		
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			crveniIgrac.umanjiBrojPreostalihFigura();
			pomocniPanel.setBrojCrvenihPreostalihFigura(crveniIgrac.getBrojPreostalihFigura());
			pojediCrvenog = false;
		}
		else {
			plaviIgrac.umanjiBrojPreostalihFigura();
			pomocniPanel.setBrojPlavihPreostalihFigura(plaviIgrac.getBrojPreostalihFigura());
			pojediPlavog = false;
		}
	
		pomocniPanel.getPojediLabel().setVisible(false);
		
		noviPotez();
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
	
	public Akcija getAkcija() {
		ArrayList<Akcija> moguceAkcije = getMoguceAkcijeZaDatoStanje(trenutnoStanje);
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

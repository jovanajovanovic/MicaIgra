package com.mica.main;

import com.mica.algorithms.ReinforcementLearning;
import com.mica.gui.Polje;
import com.mica.gui.PomocniPanel;

public class Controller {
	public static final int BROJ_KRUGOVA = 3;
	public static final int BROJ_POLJA_U_KRUGU = 8;
	public static final int BROJ_FIGURA = 9;
	
	private boolean pojediPlavog;
	private boolean pojediCrvenog;
	
	String pobednik = null;
	
	private PomocniPanel pomocniPanel;
	
	private Stanje trenutnoStanje;
	
	private ReinforcementLearning reinforcementLearning;
	
	
	
	public Controller(TipPolja igracNaPotezu) {
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		this.pojediPlavog = false;
		this.pojediCrvenog = false;
		
		this.reinforcementLearning = new ReinforcementLearning();
		
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
	
}

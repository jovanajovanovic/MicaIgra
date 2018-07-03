package com.mica.main;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import com.mica.algorithms.ReinforcementLearning;
import com.mica.gui.PomocniPanel;
import com.mica.gui.Tabla;

public class Controller {
	public static final int BROJ_KRUGOVA = 3;
	public static final int BROJ_POLJA_U_KRUGU = 8;
	public static final int BROJ_FIGURA = 9;
	
	private boolean pojediPlavog;
	private boolean pojediCrvenog;
	
	private Tabla tabla;
	private PomocniPanel pomocniPanel;
	
	private Stanje trenutnoStanje;
	
	private ReinforcementLearning reinforcementLearning;
	
	private boolean proveriKrajIgre;
	
	private Random random = new Random();
	
	
	
	public Controller(TipPolja igracNaPotezu) {
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		this.pojediPlavog = false;
		this.pojediCrvenog = false;
		
		this.proveriKrajIgre = false;
		
		this.reinforcementLearning = new ReinforcementLearning();
		
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
			if (!trenutnoStanje.daLiSuSveFigurePostavljene()) plaviIgrac.umanjiBrojNepostavljenihFigura();
			pomocniPanel.setBrojPlavihNepostavljenihFigura(plaviIgrac.getBrojNepostavljenihFigura());
			
			igracNaPotezu = TipPolja.CRVENO;
			trenutnoStanje.setIgracNaPotezu(igracNaPotezu);
			pomocniPanel.setNaPotezu(igracNaPotezu);
		}
		else if(igracNaPotezu == TipPolja.CRVENO) {
			if (!trenutnoStanje.daLiSuSveFigurePostavljene()) crveniIgrac.umanjiBrojNepostavljenihFigura();
			pomocniPanel.setBrojCrvenihNepostavljenihFigura(crveniIgrac.getBrojNepostavljenihFigura());
			
			igracNaPotezu = TipPolja.PLAVO;
			trenutnoStanje.setIgracNaPotezu(igracNaPotezu);
			pomocniPanel.setNaPotezu(igracNaPotezu);
			
			//try { Thread.sleep(2000); } catch (InterruptedException e) {} // na pocetku sacekaj 2 sec
			
			// sad je bot na redu da odigra
			//boteOdigrajPotez(igracNaPotezu);
		}
		
		
	}
	
	public void boteOdigrajPotez() {
		trenutnoStanje.getPlaviIgrac().setSelektovanoPolje(null);
		Potez potez = reinforcementLearning.noviPotez(trenutnoStanje);
		Stanje staroStanje = new Stanje(trenutnoStanje);
		napraviPotez(potez.getPolje(), potez.getSelektovanoPolje());
		
		if(pojediPlavog || pojediCrvenog) {
			try { Thread.sleep(1000); } catch (InterruptedException e) {} // sacekaj 1 sec
			ArrayList<Polje> protivnikovaPolja = getProtivnikovaPolja();
			
			int indeksPoljaKojeTrebaPojesti;
			
			while(pojediPlavog || pojediCrvenog) { // pokusavaj random da pojedes protivnikovu figuru dok ne nabodes ispravnu
				indeksPoljaKojeTrebaPojesti = random.nextInt(protivnikovaPolja.size());
				pojediFiguru(protivnikovaPolja.get(indeksPoljaKojeTrebaPojesti));
			}
		}
		
		String selektovanoStr;
		if(potez.getSelektovanoPolje() == null) selektovanoStr = "null";
		else selektovanoStr = "(" + potez.getSelektovanoPolje().getPozicija().getX() + "," + potez.getSelektovanoPolje().getPozicija().getY() + ")";
		String poljeStr;
		if(potez.getPolje() == null) poljeStr = "null";
		else poljeStr = "(" + potez.getPolje().getPozicija().getX() + "," + potez.getPolje().getPozicija().getY() + ")";
		System.out.println(selektovanoStr + " --> " + potez.getAkcija() + " --> " + poljeStr);
		
		//trenutnoStanje.getPlaviIgrac().setSelektovanoPolje(null);
		double nagrada = trenutnoStanje.izracunajScore(trenutnoStanje.getIgracNaPotezu()) - staroStanje.getScore();
		reinforcementLearning.postaviNovuQVrednost(staroStanje, potez.getAkcija(), trenutnoStanje, nagrada);
		
		RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
		RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
		
		if(proveriKrajIgre) {
			String pobednik = trenutnoStanje.krajIgre();
			if(pobednik != null) {
				RadSaPodacima.upisiPobednikaUFajl(pobednik);
				//RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(controller.getReinforcementLearning().getqVrednosti());
				//RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(controller.getReinforcementLearning().getBrojMenjanjaQVrednosti());
				
				int res = JOptionPane.showConfirmDialog(null, pobednik + " je pobednik! Želite li da igrate novu igru?", "Kraj igre", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(res == JOptionPane.YES_OPTION) {
					//resetujTabluZaNovuIgru();
					resetujSveZaNovuIgru();
					//refresh();
				}
				
				return;
			}
			
			proveriKrajIgre = false;
		}
		
		tabla.refresh();
		
		//noviPotez();
	}

	private ArrayList<Polje> getProtivnikovaPolja() {
		ArrayList<Polje> protivnikovaPolja = new ArrayList<Polje>();

		Polje[][] polja = trenutnoStanje.getPolja();
		
		for (int i = 0; i < BROJ_KRUGOVA; i++) {
			for (int j = 0; j < BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() == TipPolja.ZUTO || polja[i][j].getTipPolja() == trenutnoStanje.getIgracNaPotezu()) {
					continue;
				}	
				
				protivnikovaPolja.add(polja[i][j]);
			}	
		}
		
		return protivnikovaPolja;
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
	
	public Tabla getTabla() {
		return tabla;
	}

	public void setTabla(Tabla tabla) {
		this.tabla = tabla;
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

	public boolean isProveriKrajIgre() {
		return proveriKrajIgre;
	}

	public void setProveriKrajIgre(boolean proveriKrajIgre) {
		this.proveriKrajIgre = proveriKrajIgre;
	}
	
	public ReinforcementLearning getReinforcementLearning() {
		return reinforcementLearning;
	}

	public void setReinforcementLearning(ReinforcementLearning reinforcementLearning) {
		this.reinforcementLearning = reinforcementLearning;
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

	public void napraviPotez(Polje polje, Polje selektovanoPolje) {
		if(selektovanoPolje == null)  { // jos nisu sve figure postavljene, i sada postavljamo
			polje.setTipPolja(trenutnoStanje.getIgracNaPotezu());
		}
		else { // sve figure su vec postavljene
			polje.setTipPolja(selektovanoPolje.getTipPolja());
			selektovanoPolje.setTipPolja(TipPolja.ZUTO);
		}
		
		boolean daLijeUTariStaro = polje.isDaLiJeUTari();
		podesiPoljaUTaramaIVanNjih();
		
		if(!daLijeUTariStaro && polje.isDaLiJeUTari())  {
			spremiJedenje(); // kasnije prilikom jedenja bice setovano - proveriKrajIgre = true;
		}
		else {
			noviPotez();
			
			if(selektovanoPolje == null) {
				if (trenutnoStanje.daLiSuSveFigurePostavljene()) proveriKrajIgre = true;
			}
			else {
				proveriKrajIgre = true;
			}
			
		}
		
	}
	
	public void podesiPoljaUTaramaIVanNjih() {
		Polje[][] polja = trenutnoStanje.getPolja();
		
		TipPolja tipPolja;
		
		for (int i = 0; i < BROJ_KRUGOVA; i++) {
			for (int j = 0; j < BROJ_POLJA_U_KRUGU; j+=2) {
				tipPolja = polja[i][j].getTipPolja();
				
				if(polja[i][j+1].getTipPolja() == tipPolja && polja[i][(j+2)%BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
					polja[i][j].setDaLiJeUTari(true);
					polja[i][j+1].setDaLiJeUTari(true);
					polja[i][(j+2)%BROJ_POLJA_U_KRUGU].setDaLiJeUTari(true);
				}
				else {
					if(j == 0) polja[i][j].setDaLiJeUTari(false);
					polja[i][j+1].setDaLiJeUTari(false);
					if(j != 6) polja[i][(j+2)%BROJ_POLJA_U_KRUGU].setDaLiJeUTari(false);
				}
			}
		}
		
		for (int k = 1; k < BROJ_POLJA_U_KRUGU; k+=2) {
			tipPolja = polja[0][k].getTipPolja();
			
			if(polja[1][k].getTipPolja() == tipPolja && polja[2][k].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
				polja[0][k].setDaLiJeUTari(true);
				polja[1][k].setDaLiJeUTari(true);
				polja[2][k].setDaLiJeUTari(true);
			}
			/*else {
				polja[0][k].setDaLiJeUTari(false);
				polja[1][k].setDaLiJeUTari(false);
				polja[2][k].setDaLiJeUTari(false);
			}*/
		}
		
	}

	public void pojediFiguru(Polje polje) {
		boolean uslov = true;
		boolean dlsspput;
		
		if(pojediPlavog) {
			if(polje.getTipPolja() == TipPolja.PLAVO) {
				uslov = true;
				dlsspput = daLiSuSvaProtivnickaPoljaUTari(TipPolja.PLAVO);
				if(!dlsspput) {
					if (polje.isDaLiJeUTari()) {
						uslov = false;
					}
				}
				
				if (uslov) {
					polje.setTipPolja(TipPolja.ZUTO);
					zavrsiJedenje();
					if(dlsspput) podesiPoljaUTaramaIVanNjih();
					proveriKrajIgre = true;
				}
				
			}
			//return;
		}
		
		if (pojediCrvenog) {
			if(polje.getTipPolja() == TipPolja.CRVENO) {
				uslov = true;
				dlsspput = daLiSuSvaProtivnickaPoljaUTari(TipPolja.CRVENO);
				if(!dlsspput) {
					if (polje.isDaLiJeUTari()) {
						uslov = false;
					}
				}
				
				if (uslov) {
					polje.setTipPolja(TipPolja.ZUTO);
					zavrsiJedenje();
					if(dlsspput) podesiPoljaUTaramaIVanNjih();
					proveriKrajIgre = true;
				}
				
				//refresh();
				
			}
			//return;
		}
	}
	
	public boolean daLiSuSvaProtivnickaPoljaUTari(TipPolja tipPolja) {
		Polje[][] polja = trenutnoStanje.getPolja();
		
		for (int i = 0; i < BROJ_KRUGOVA; i++) {
			for (int j = 0; j < BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() != tipPolja) continue;
				
				if(!polja[i][j].isDaLiJeUTari()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
}

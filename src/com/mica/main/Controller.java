package com.mica.main;

import javax.swing.JOptionPane;

import com.mica.algorithms.MiniMax;
import com.mica.algorithms.ReinforcementLearning;
import com.mica.gui.GlavniProzor;
import com.mica.gui.PomocniPanel;
import com.mica.gui.Tabla;

public class Controller {
	public static final int BROJ_KRUGOVA = 3;
	public static final int BROJ_POLJA_U_KRUGU = 8;
	public static final int BROJ_FIGURA = 9;
	public static final int BROJ_MICA_KOMBINACIJA = 16;
	public static final int BROJ_FIGURA_U_MICI = 3;

	private boolean potezNapravljen = false;
	
	private Tabla tabla;
	private PomocniPanel pomocniPanel;
	
	private Stanje trenutnoStanje;
	
	private ReinforcementLearning reinforcementLearning;
	
	private MiniMax miniMaxAlgoritam;
	
	private boolean proveriKrajIgre;
	
	private int brojPartijaTrening;
	private Algoritam algoritamPlaviTrening;
	private Algoritam algoritamCrveniTrening;
	
	private int brojPoteza;
	
	public Controller(TipPolja igracNaPotezu) {
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		
		
		this.reinforcementLearning = new ReinforcementLearning();
		this.miniMaxAlgoritam = new MiniMax();
	}
	
	public void zapocniNovuIgru() {
		this.brojPartijaTrening = -1;
		int[] indeksi;
		int indeksPlavi = 0;
		int indeksCrveni = 0;
		Algoritam algoritamPlavi = null;
		Algoritam algoritamCrveni;
		
		do {
			indeksi = GlavniProzor.dialogZaNovuIgru();
			
			if(indeksi[0] != -1 && indeksi[1] != -1) {
				indeksPlavi = indeksi[0];
				indeksCrveni = indeksi[1];
				
				if(indeksPlavi >= 3 || indeksCrveni >= 3) {
					JOptionPane.showMessageDialog(null, "Algoritam koji ste izabrali jos nije implementiran!", "Nije implementiran...", JOptionPane.ERROR_MESSAGE);;	
				}
				else {
					resetujSveZaNovuIgru();
					tabla.refresh();
					
					algoritamPlavi = Igrac.algoritmi[indeksPlavi];
					algoritamCrveni = Igrac.algoritmi[indeksCrveni];
					
					trenutnoStanje.getPlaviIgrac().setAlgoritam(algoritamPlavi);
					trenutnoStanje.getCrveniIgrac().setAlgoritam(algoritamCrveni);
					
				}
				
			}
			else {
				dialogPocetni(null);
			}
		}
		while(indeksPlavi >= 3 || indeksCrveni >= 3);
		
		this.brojPoteza = 1;
		if(algoritamPlavi != Algoritam.COVEK) boteOdigrajPotez(algoritamPlavi); 
		// plavi uvek prvi pocinje igru, zato ispitujemo njegov algoritam
		
	}
	
	public void resetujSveZaNovuIgru() {
		this.proveriKrajIgre = false;
		this.brojPoteza = 1;
		
		TipPolja igracNaPotezu = TipPolja.PLAVO;
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		pomocniPanel.resetujSveZaNovuIgru(trenutnoStanje);
	}

	public void noviPotez(Stanje stanje, boolean azurirajPomocniPanel) {
		TipPolja igracNaPotezu = stanje.getIgracNaPotezu();
		Igrac crveniIgrac = stanje.getCrveniIgrac();
		Igrac plaviIgrac = stanje.getPlaviIgrac();
		
		if (igracNaPotezu == TipPolja.PLAVO) {
			if (!stanje.daLiSuSveFigurePostavljene()) {
				plaviIgrac.umanjiBrojNepostavljenihFigura();
				if(azurirajPomocniPanel) pomocniPanel.setBrojPlavihNepostavljenihFigura(plaviIgrac.getBrojNepostavljenihFigura());
			}
			
			igracNaPotezu = TipPolja.CRVENO;
			stanje.setIgracNaPotezu(igracNaPotezu);
			if(azurirajPomocniPanel) pomocniPanel.setNaPotezu(igracNaPotezu);
		}
		else if(igracNaPotezu == TipPolja.CRVENO) {
			if (!stanje.daLiSuSveFigurePostavljene()) {
				crveniIgrac.umanjiBrojNepostavljenihFigura();
				if(azurirajPomocniPanel) pomocniPanel.setBrojCrvenihNepostavljenihFigura(crveniIgrac.getBrojNepostavljenihFigura());
			}
			
			igracNaPotezu = TipPolja.PLAVO;
			stanje.setIgracNaPotezu(igracNaPotezu);
			if(azurirajPomocniPanel) pomocniPanel.setNaPotezu(igracNaPotezu);
			
		}
		
		
	}
	
	public void boteOdigrajPotez(Algoritam algoritam) {
		if (algoritam == Algoritam.RL) {
			
			Stanje staroStanje = new Stanje(trenutnoStanje);
			double stariScore = staroStanje.izracunajScore();
			
			Potez potez = odigrajPotezIEventualnoPojediProtivnikovuFiguru(trenutnoStanje, true);
			// nakon sto je potez napravljen, promenjen je igracNaPotezu
	
			if(potez != null) {
				Stanje potencijalnoNovoStanje = predvidiProtivnikovPotezINapraviNovoStanje(trenutnoStanje);
				
				double nagrada = trenutnoStanje.izracunajScore() - stariScore;
				reinforcementLearning.postaviNovuQVrednost(staroStanje, potez.getAkcija(), potencijalnoNovoStanje, nagrada);
				
				//RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
				//RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
			}
			
		} 
		else if(algoritam == Algoritam.MINI_MAX){
			// mini max algoritam 
			Stanje staroStanje = new Stanje(trenutnoStanje);
			double stariScore = staroStanje.getScore();
			
			Potez potez = odigrajPotezMiniMax(trenutnoStanje, true);
			
		}
		
		tabla.refresh();
		
		if(proveriKrajIgre) {
			proveriKrajIgre = false;
			
			Rezultat rezultat = trenutnoStanje.krajIgre();
			if(rezultat != null) {
				rezultat.setBrojPoteza(this.brojPoteza);
				RadSaPodacima.upisiKrajnjiRezultatUFajl(rezultat);
				//RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
				//RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
				
				String poruka = rezultat.getPobednik() + " (" + rezultat.getAlgoritamPobednika()  + ") je pobednik!";
				
				this.brojPartijaTrening--;
				pomocniPanel.setBrojPreostalihPartijaLabel(this.brojPartijaTrening);
				
				if(this.brojPartijaTrening <= 0) {
					RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
					RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
					
					pomocniPanel.ukloniBrojPreostalihPartijaLabel();
					dialogPocetni(poruka);
				}
				else {
					// spremi sve i zapocni sledecu partiju u okviru treninga 
					resetujSveZaNovuIgru();
					tabla.refresh();
				
					trenutnoStanje.getPlaviIgrac().setAlgoritam(this.algoritamPlaviTrening);
					trenutnoStanje.getCrveniIgrac().setAlgoritam(this.algoritamCrveniTrening);
					
					boteOdigrajPotez(this.algoritamPlaviTrening);
				}
				
				return;
				//controller.setPotezNapravljen(false); // ovo postavljamo 
			}
			
		}
		
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				//vec je promenjen igrac na potezu
				Algoritam algoritamNovogIgraca;
				if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
					algoritamNovogIgraca = trenutnoStanje.getPlaviIgrac().getAlgoritam();
					brojPoteza++;
				}
				else {
					algoritamNovogIgraca = trenutnoStanje.getCrveniIgrac().getAlgoritam();
				}
				
				if (algoritamNovogIgraca != Algoritam.COVEK) {
					//try { Thread.sleep(2000); } catch (InterruptedException e) {} // na pocetku sacekaj 2 sec
					
					// novi igrac je isto bot
					boteOdigrajPotez(algoritamNovogIgraca);
				}
				
			}
		});
		t.start();
	}

	private Potez odigrajPotezMiniMax(Stanje trenutnoStanje, boolean b) {
		// mini max algoritam 
		Igrac igra = null;
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO){
			igra =  trenutnoStanje.getPlaviIgrac();
		}else {
			igra = trenutnoStanje.getCrveniIgrac();
		}
		this.miniMaxAlgoritam.setDubinaStabla(5);
		this.miniMaxAlgoritam.setIgrac(igra);
		Potez potez =  miniMaxAlgoritam.noviPotez(trenutnoStanje);
		if(potez == null){return null;}
		napraviPotez(trenutnoStanje, potez.getPolje(), potez.getSelektovanoPolje(), b);
		
		if(trenutnoStanje.isPojedi()){
			pojediMinFiguru(trenutnoStanje, b);
		}
		
		ispisiPotezNaKonzoli(potez);
		return potez;
	}

	
	
	private void pojediMinFiguru(Stanje trenutnoStanje2, boolean b) {
		//jedenje figure minimax algoritam 
		
		Potez potez = miniMaxAlgoritam.noviPotezJediFiguru(trenutnoStanje2);
		if(potez != null) {	
			Stanje staroStanje = new Stanje(trenutnoStanje2);
			double stariScore = staroStanje.getScore();
					
			izvrsiJedenjeFigure(trenutnoStanje2, potez.getPolje(), b);
			
			ispisiPotezNaKonzoli(potez);
			
		}
		
	}

	private Potez odigrajPotezIEventualnoPojediProtivnikovuFiguru(Stanje stanje, boolean azurirajPomocniPanel) {
		/*Igrac igrac;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) igrac = stanje.getPlaviIgrac();
		else igrac = stanje.getCrveniIgrac();
			
		igrac.setSelektovanoPolje(null);*/
		
		Potez potez = reinforcementLearning.noviPotez(stanje);
		if(potez == null) return null;
		
		napraviPotez(stanje, potez.getPolje(), potez.getSelektovanoPolje(), azurirajPomocniPanel);
		
		if(stanje.isPojedi()) {
			pojediFiguru(stanje, azurirajPomocniPanel);
		}
		
		ispisiPotezNaKonzoli(potez);
		
		return potez;
	}

	private Stanje predvidiProtivnikovPotezINapraviNovoStanje(Stanje stanje) {
		Stanje novoStanje = new Stanje(stanje);
		
		odigrajPotezIEventualnoPojediProtivnikovuFiguru(novoStanje, false);
		
		return novoStanje;
	}

	private void ispisiPotezNaKonzoli(Potez potez) {
		String selektovanoStr;
		if(potez.getSelektovanoPolje() == null) selektovanoStr = "null";
		else selektovanoStr = "(" + potez.getSelektovanoPolje().getPozicija().getX() + "," + potez.getSelektovanoPolje().getPozicija().getY() + ")";
		String poljeStr;
		if(potez.getPolje() == null) poljeStr = "null";
		else poljeStr = "(" + potez.getPolje().getPozicija().getX() + "," + potez.getPolje().getPozicija().getY() + ")";
		System.out.println(selektovanoStr + " --> " + potez.getAkcija() + " --> " + poljeStr);
	}

	/*private ArrayList<Polje> getProtivnikovaPolja(Stanje stanje) {
		ArrayList<Polje> protivnikovaPolja = new ArrayList<Polje>();

		Polje[][] polja = stanje.getPolja();
		
		for (int i = 0; i < BROJ_KRUGOVA; i++) {
			for (int j = 0; j < BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() == TipPolja.ZUTO || polja[i][j].getTipPolja() == stanje.getIgracNaPotezu()) {
					continue;
				}	
				
				protivnikovaPolja.add(polja[i][j]);
			}	
		}
		
		return protivnikovaPolja;
	}*/

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
	
	public boolean isPotezNapravljen() {
		return potezNapravljen;
	}

	public void setPotezNapravljen(boolean potezNapravljen) {
		this.potezNapravljen = potezNapravljen;
	}
	
	public void uvecajBrojPoteza() {
		this.brojPoteza++;
	}
	
	public int getBrojPoteza() {
		return brojPoteza;
	}

	public void setBrojPoteza(int brojPoteza) {
		this.brojPoteza = brojPoteza;
	}

	public void spremiJedenje(Stanje stanje, boolean azurirajPomocniPanel) {
		String figuraZaJedenje;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			figuraZaJedenje = "CRVENU";
		}
		else {
			figuraZaJedenje = "PLAVU";
		}
		
		stanje.setPojedi(true);
		
		if(azurirajPomocniPanel) {
			pomocniPanel.setPojediLabel(figuraZaJedenje);
			pomocniPanel.getPojediLabel().setVisible(true);
		}
	
	}
	
	public void zavrsiJedenje(Stanje stanje, boolean azurirajPomocniPanel) {
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			Igrac crveniIgrac = stanje.getCrveniIgrac();
			crveniIgrac.umanjiBrojPreostalihFigura();
			if(azurirajPomocniPanel) pomocniPanel.setBrojCrvenihPreostalihFigura(crveniIgrac.getBrojPreostalihFigura());
		}
		else {
			Igrac plaviIgrac = stanje.getPlaviIgrac();
			plaviIgrac.umanjiBrojPreostalihFigura();
			if(azurirajPomocniPanel) pomocniPanel.setBrojPlavihPreostalihFigura(plaviIgrac.getBrojPreostalihFigura());
		}
		
		stanje.setPojedi(false);
	
		if(azurirajPomocniPanel) pomocniPanel.getPojediLabel().setVisible(false);
		
		noviPotez(stanje, azurirajPomocniPanel);
	}

	public void napraviPotez(Stanje stanje, Polje polje, Polje selektovanoPolje, boolean azurirajPomocniPanel) {
		if(selektovanoPolje == null)  { // jos nisu sve figure postavljene, i sada postavljamo
			polje.setTipPolja(stanje.getIgracNaPotezu());
		}
		else { // sve figure su vec postavljene
			polje.setTipPolja(selektovanoPolje.getTipPolja());
			selektovanoPolje.setTipPolja(TipPolja.ZUTO);
		}
		
		boolean daLijeUTariStaro = polje.isDaLiJeUTari();
		stanje.podesiPoljaUTaramaIVanNjih();
		
		if(!daLijeUTariStaro && polje.isDaLiJeUTari())  {
			spremiJedenje(stanje, azurirajPomocniPanel); // kasnije prilikom jedenja bice setovano - proveriKrajIgre = true;
		}
		else {
			noviPotez(stanje, azurirajPomocniPanel);
			
			if(selektovanoPolje == null) {
				if (stanje.daLiSuSveFigurePostavljene()) proveriKrajIgre = true;
			}
			else {
				proveriKrajIgre = true;
			}
			
		}
		
	}
	
	private void pojediFiguru(Stanje stanje, boolean azurirajPomocniPanel) {
		//try { Thread.sleep(1000); } catch (InterruptedException e) {} // sacekaj 1 sec
		/*ArrayList<Polje> protivnikovaPolja = getProtivnikovaPolja(stanje);
		
		int indeksPoljaKojeTrebaPojesti;
		
		while(pojediPlavog || pojediCrvenog) { // pokusavaj random da pojedes protivnikovu figuru dok ne nabodes ispravnu
			indeksPoljaKojeTrebaPojesti = random.nextInt(protivnikovaPolja.size());
			JedenjeFigure(stanje, protivnikovaPolja.get(indeksPoljaKojeTrebaPojesti), azurirajPomocniPanel);
		}*/
		
		Potez potez = reinforcementLearning.noviPotez(stanje);
		if(potez != null) {	
			Stanje staroStanje = new Stanje(stanje);
			double stariScore = staroStanje.getScore();
					
			izvrsiJedenjeFigure(stanje, potez.getPolje(), azurirajPomocniPanel);
			
			Stanje potencijalnoNovoStanje = predvidiProtivnikovPotezINapraviNovoStanje(stanje);
			
			double nagrada = stanje.izracunajScore() - stariScore;
			reinforcementLearning.postaviNovuQVrednost(staroStanje, potez.getAkcija(), potencijalnoNovoStanje, nagrada);
		}
	}

	public void izvrsiJedenjeFigure(Stanje stanje, Polje polje, boolean azurirajPomocniPanel) {
		boolean uslov = true;
		boolean dlsspput;
		
		TipPolja tipPoljaZaJedenje;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) tipPoljaZaJedenje = TipPolja.CRVENO;
		else tipPoljaZaJedenje = TipPolja.PLAVO;
		
		if(polje.getTipPolja() == tipPoljaZaJedenje) {
			uslov = true;
			dlsspput = stanje.daLiSuSvaProtivnickaPoljaUTari(tipPoljaZaJedenje);
			if(!dlsspput) {
				if (polje.isDaLiJeUTari()) {
					uslov = false;
				}
			}
			
			if (uslov) {
				polje.setTipPolja(TipPolja.ZUTO);
				zavrsiJedenje(stanje, azurirajPomocniPanel);
				if(dlsspput) stanje.podesiPoljaUTaramaIVanNjih();
				proveriKrajIgre = true;
			}
			
		}
		
	}

	public void dialogPocetni(String poruka) {
		String naslov = "Mica";
	    String[] opcije = {"Nova Igra", "Trening", "Izlaz"};
		int res = JOptionPane.showOptionDialog(null, poruka, naslov, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcije, opcije[0]);
		if(res == 0) {	
			//indeksi = GlavniProzor.dialogZaNovuIgru();
			
			//if(indeksi[0] != -1 && indeksi[1] != -1) {
				//resetujSveZaNovuIgru();
				//tabla.refresh();
			zapocniNovuIgru();
			//}
			
		}
		else if(res == 1) {
			zapocniTrening();
		}
		else {
			//RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
			//RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
			System.exit(0);
		}
		
	}

	private void zapocniTrening() {
		int[] indeksi;
		int indeksPlavi = 0;
		int indeksCrveni = 0;
		
		do {
			indeksi = GlavniProzor.dialogZaTreniranje();
			
			if(indeksi[0] != -1 && indeksi[1] != -1 && indeksi[2] != -1) {
				indeksPlavi = indeksi[0];
				indeksCrveni = indeksi[1];
				this.brojPartijaTrening = indeksi[2];
				
				if(indeksPlavi >= 1 || indeksCrveni >= 1) {
					JOptionPane.showMessageDialog(null, "Algoritam koji ste izabrali jos nije implementiran!", "Nije implementiran...", JOptionPane.ERROR_MESSAGE);;	
				}
				else {
					resetujSveZaNovuIgru();
					tabla.refresh();
					
					this.algoritamPlaviTrening = Igrac.algoritmiZaTrening[indeksPlavi];
					this.algoritamCrveniTrening = Igrac.algoritmiZaTrening[indeksCrveni];
					
					trenutnoStanje.getPlaviIgrac().setAlgoritam(this.algoritamPlaviTrening);
					trenutnoStanje.getCrveniIgrac().setAlgoritam(this.algoritamCrveniTrening);
					
				}
				
			}
			else {
				dialogPocetni(null);
			}
		}
		while(indeksPlavi >= 1 || indeksCrveni >= 1);
		
		pomocniPanel.setBrojPreostalihPartijaLabel(this.brojPartijaTrening);
		pomocniPanel.prikaziBrojPreostalihPartijaLabel();
		this.brojPoteza = 1;
		boteOdigrajPotez(this.algoritamPlaviTrening); 
		// plavi uvek prvi pocinje igru, zato ispitujemo njegov algoritam
	}
	
}

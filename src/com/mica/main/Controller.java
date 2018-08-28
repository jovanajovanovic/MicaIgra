package com.mica.main;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import com.mica.algorithms.Algoritam;
import com.mica.algorithms.MiniMax;
import com.mica.algorithms.ReinforcementLearning;
import com.mica.gui.GlavniProzor;

public class Controller {
	public static final int BROJ_KRUGOVA = 3;
	public static final int BROJ_POLJA_U_KRUGU = 8;
	public static final int BROJ_FIGURA = 9;
	public static final int BROJ_MICA_KOMBINACIJA = 16;
	public static final int BROJ_FIGURA_U_MICI = 3;
	
	private boolean koristioReinforcmentLearning;

	private boolean potezNapravljen = false;
	
	private GlavniProzor glavniProzor;
	
	private Stanje trenutnoStanje;
	
	private ReinforcementLearning reinforcementLearning;
	
	private MiniMax miniMaxAlgoritam;
	
	private boolean proveriKrajIgre;
	
	private int brojPartijaTrening;
	private Algoritam algoritamPlaviTrening;
	private Algoritam algoritamCrveniTrening;
	private int brojPobedaPlavogTening;
	private int brojPobedaCrvenogTening;
	
	private RezimIgre rezimIgre;
	
	private int brojSekundiZaSpavanje;

	public static int brojPoteza;
	
	private boolean prikaziDialogTabelaAkcijaIQVrednosti;
	
	public Controller() {
		trenutnoStanje = new Stanje();
		
		this.reinforcementLearning = new ReinforcementLearning();
		this.miniMaxAlgoritam = new MiniMax();
		
		koristioReinforcmentLearning = false;
	}
	
	public void zapocniNovuIgru() {
		this.brojSekundiZaSpavanje = 0;
		int[] indeksi;
		int indeksPlavi = 0;
		int indeksCrveni = 0;
		Algoritam algoritamPlavi = null;
		Algoritam algoritamCrveni = null;
		
		do {
			indeksi = glavniProzor.dialogZaNovuIgru();
			
			if(indeksi[0] != -1 && indeksi[1] != -1) {
				indeksPlavi = indeksi[0];
				indeksCrveni = indeksi[1];
				
				if(indeksPlavi >= 3 || indeksCrveni >= 3) {
					JOptionPane.showMessageDialog(null, "Algoritam koji ste izabrali jos nije implementiran!", "Nije implementiran...", JOptionPane.ERROR_MESSAGE);;	
				}
				
			}
			else {
				glavniProzor.dialogPocetni(null);
			}
		}
		while(indeksPlavi >= 3 || indeksCrveni >= 3);
		
		resetujSveZaNovuIgru();
		glavniProzor.osveziTablu();
		
		algoritamPlavi = Igrac.getEnumAlgoritam(Igrac.algoritmi[indeksPlavi]);
		algoritamCrveni = Igrac.getEnumAlgoritam(Igrac.algoritmi[indeksCrveni]);
		
		if(algoritamPlavi == Algoritam.RL || algoritamCrveni == Algoritam.RL) {
			koristioReinforcmentLearning = true;
			glavniProzor.prikaziPanelZaEpsilon();
			glavniProzor.prikaziCheckBoxZaPrikazTabelePanel();
		}
		
		trenutnoStanje.getPlaviIgrac().setAlgoritam(algoritamPlavi);
		trenutnoStanje.getCrveniIgrac().setAlgoritam(algoritamCrveni);
		
		brojPoteza = 1;
		
		rezimIgre = RezimIgre.NOVA_IGRA;
		
		if(algoritamPlavi != Algoritam.COVEK) {
			glavniProzor.prikaziPanelZaSekunde();
			// plavi uvek prvi pocinje igru, zato ispitujemo njegov algoritam
			boteOdigrajPotez(algoritamPlavi); 
		}
		else if(algoritamCrveni != Algoritam.COVEK) glavniProzor.prikaziPanelZaSekunde();
		//else glavniProzor.sakrijPanelZaSekunde();
		
		glavniProzor.prikaziNaPotezuPanel();
		glavniProzor.prikaziPanelZaBrojPlavihNepostavljenihFigura();
		glavniProzor.prikaziPanelZaBrojPlavihPreostalihFigura();
		glavniProzor.prikaziPanelZaBrojCrvenihNepostavljenihFigura();
		glavniProzor.prikaziPanelZaBrojCrvenihPreostalihFigura();
		//glavniProzor.prikaziCheckBoxZaPrikazTabelePanel();
	}
	
	public void zapocniTrening() {
		this.brojSekundiZaSpavanje = 0;
		int[] indeksi;
		int indeksPlavi = 0;
		int indeksCrveni = 0;
		
		do {
			indeksi = glavniProzor.dialogZaTreniranje();
			
			if(indeksi[0] != -1 && indeksi[1] != -1 && indeksi[2] != -1) {
				indeksPlavi = indeksi[0];
				indeksCrveni = indeksi[1];
				this.brojPartijaTrening = indeksi[2];
				
				if(indeksPlavi >= 2 || indeksCrveni >= 2) {
					JOptionPane.showMessageDialog(null, "Algoritam koji ste izabrali jos nije implementiran!", "Nije implementiran...", JOptionPane.ERROR_MESSAGE);;	
				}
				
			}
			else {
				glavniProzor.dialogPocetni(null);
			}
		}
		while(indeksPlavi >= 2 || indeksCrveni >= 2);
		
		resetujSveZaNovuIgru();
		glavniProzor.osveziTablu();
		
		this.algoritamPlaviTrening = Igrac.getEnumAlgoritam(Igrac.algoritmiZaTrening[indeksPlavi]);
		this.algoritamCrveniTrening = Igrac.getEnumAlgoritam(Igrac.algoritmiZaTrening[indeksCrveni]);
		
		if(this.algoritamPlaviTrening == Algoritam.RL || this.algoritamCrveniTrening == Algoritam.RL) {
			koristioReinforcmentLearning = true;
			glavniProzor.prikaziPanelZaEpsilon();
			glavniProzor.prikaziCheckBoxZaPrikazTabelePanel();
		}
		
		trenutnoStanje.getPlaviIgrac().setAlgoritam(this.algoritamPlaviTrening);
		trenutnoStanje.getCrveniIgrac().setAlgoritam(this.algoritamCrveniTrening);
		
		glavniProzor.prikaziPanelZaSekunde();
		
		glavniProzor.prikaziNaPotezuPanel();
		glavniProzor.prikaziPanelZaBrojPlavihNepostavljenihFigura();
		glavniProzor.prikaziPanelZaBrojPlavihPreostalihFigura();
		glavniProzor.prikaziPanelZaBrojCrvenihNepostavljenihFigura();
		glavniProzor.prikaziPanelZaBrojCrvenihPreostalihFigura();
		glavniProzor.podesiBrojPreostalihPartijaLabel(this.brojPartijaTrening);
		glavniProzor.prikaziPanelZaBrojPreostalihPartija();
		glavniProzor.prikaziCheckBoxZaPrikazTabelePanel();
		
		brojPoteza = 1;
		
		rezimIgre = RezimIgre.TRENING;
		
		brojPobedaPlavogTening = 0;
		brojPobedaCrvenogTening = 0;
		
		boteOdigrajPotez(this.algoritamPlaviTrening); 
		// plavi uvek prvi pocinje igru, zato ispitujemo njegov algoritam
	}
	
	public void resetujSveZaNovuIgru() {
		this.proveriKrajIgre = false;
		brojPoteza = 1;
		
		TipPolja igracNaPotezu = TipPolja.PLAVO;
		trenutnoStanje = new Stanje(igracNaPotezu);
		
		glavniProzor.resetujPomocniPanelZaNovuIgru(trenutnoStanje);
	}

	public void noviPotez(Stanje stanje, boolean predvidjanjeProtivnikovogPoteza) {
		TipPolja igracNaPotezu = stanje.getIgracNaPotezu();
		Igrac crveniIgrac = stanje.getCrveniIgrac();
		Igrac plaviIgrac = stanje.getPlaviIgrac();
		
		if (igracNaPotezu == TipPolja.PLAVO) {
			if (!stanje.daLiSuSveFigurePostavljene()) {
				plaviIgrac.umanjiBrojNepostavljenihFigura();
				if(!predvidjanjeProtivnikovogPoteza) glavniProzor.podesiBrojPlavihNepostavljenihFigura(plaviIgrac.getBrojNepostavljenihFigura());
			}
			
			igracNaPotezu = TipPolja.CRVENO;
			stanje.setIgracNaPotezu(igracNaPotezu);
			if(!predvidjanjeProtivnikovogPoteza) glavniProzor.podesiNaPotezu(igracNaPotezu);
		}
		else if(igracNaPotezu == TipPolja.CRVENO) {
			if (!stanje.daLiSuSveFigurePostavljene()) {
				crveniIgrac.umanjiBrojNepostavljenihFigura();
				if(!predvidjanjeProtivnikovogPoteza) glavniProzor.podesiBrojCrvenihNepostavljenihFigura(crveniIgrac.getBrojNepostavljenihFigura());
			}
			
			igracNaPotezu = TipPolja.PLAVO;
			stanje.setIgracNaPotezu(igracNaPotezu);
			if(!predvidjanjeProtivnikovogPoteza) glavniProzor.podesiNaPotezu(igracNaPotezu);
			
		}
		
		
	}
	
	public void boteOdigrajPotez(Algoritam algoritam) {
		if (algoritam == Algoritam.RL) {
			
			Stanje staroStanje = new Stanje(trenutnoStanje);
			double stariScore = staroStanje.izracunajScore(staroStanje.getIgracNaPotezu());
			
			Potez potez = odigrajPotezIEventualnoPojediProtivnikovuFiguru(trenutnoStanje, false);
			// nakon sto je potez napravljen, promenjen je igracNaPotezu
			
			//if(prikaziDialogTabelaAkcijaIQVrednosti) glavniProzor.prikaziTabeluAkcijaiQVrednosti(reinforcementLearning.getqVrednostiZaMoguceAkcije());
	
			if(potez != null) {
				if(potez.getAkcija().name().contains("POSTAVI")) staroStanje.setSelektovanoPolje(potez.getPolje());
				else staroStanje.setSelektovanoPolje(potez.getSelektovanoPolje());
				
				Stanje potencijalnoNovoStanje = predvidiProtivnikovPotezINapraviNovoStanje(trenutnoStanje);
				
				double nagrada = trenutnoStanje.izracunajScore(staroStanje.getIgracNaPotezu()) - stariScore;
				reinforcementLearning.postaviNovuQVrednost(staroStanje, potez.getAkcija(), potencijalnoNovoStanje, nagrada);
				
				//RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
				//RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
			}
			else {
				JOptionPane.showMessageDialog(null, "Potez == null", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			//if(prikaziDialogTabelaAkcijaIQVrednosti) glavniProzor.prikaziTabeluAkcijaiQVrednosti(reinforcementLearning.getqVrednostiZaMoguceAkcije());
		} 
		else if(algoritam == Algoritam.MINI_MAX){
			// mini max algoritam 
			Stanje staroStanje = new Stanje(trenutnoStanje);
			double stariScore = staroStanje.getScore();
			
			Potez potez = odigrajPotezMiniMax(trenutnoStanje, false);
			
			if(potez == null){
				JOptionPane.showOptionDialog(null, "Minimax nije pronasao akciju za ovo stanje", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);				
				return;
			}
			
		}
		
		glavniProzor.osveziTablu();
		
		if(proveriKrajIgre) {
			proveriKrajIgre = false;
			
			Rezultat rezultat = trenutnoStanje.krajIgre();
			if(rezultat != null) {
				rezultat.setBrojPoteza(brojPoteza);
				RadSaPodacima.upisiKrajnjiRezultatUFajl(rezultat);
				/*if(trenutnoStanje.getPlaviIgrac().getAlgoritam() == Algoritam.RL || trenutnoStanje.getCrveniIgrac().getAlgoritam() == Algoritam.RL) {
					RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
					RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
				}*/
				
				String poruka;
				
				if(rezimIgre == RezimIgre.NOVA_IGRA) {
					poruka = rezultat.getPobednik() + " (" + Igrac.getStrAlgoritam(rezultat.getAlgoritamPobednika())  + ") je pobednik!";
					glavniProzor.dialogPocetni(poruka);
				}
				else {
					this.brojPartijaTrening--;
					glavniProzor.podesiBrojPreostalihPartijaLabel(this.brojPartijaTrening);
					
					if(rezultat.getPobednik().equals("Plavi")) brojPobedaPlavogTening++;
					else brojPobedaCrvenogTening++;
					
					if(this.brojPartijaTrening <= 0) {
						/*if(trenutnoStanje.getPlaviIgrac().getAlgoritam() == Algoritam.RL || trenutnoStanje.getCrveniIgrac().getAlgoritam() == Algoritam.RL) {
							RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
							RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(reinforcementLearning.getBrojMenjanjaQVrednosti());
						}*/
						
						glavniProzor.sakrijPanelZaBrojPreostalihPartija();
						poruka = "Plavi (" + this.algoritamPlaviTrening + ")   " + brojPobedaPlavogTening + " : " + brojPobedaCrvenogTening + "   Crveni (" + this.algoritamCrveniTrening + ")";
						glavniProzor.dialogPocetni(poruka);
					}
					else {
						// spremi sve i zapocni sledecu partiju u okviru treninga 
						resetujSveZaNovuIgru();
						glavniProzor.osveziTablu();
					
						trenutnoStanje.getPlaviIgrac().setAlgoritam(this.algoritamPlaviTrening);
						trenutnoStanje.getCrveniIgrac().setAlgoritam(this.algoritamCrveniTrening);
						
						boteOdigrajPotez(this.algoritamPlaviTrening);
					}
					
				}
				
				return;
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
					if(brojSekundiZaSpavanje > 0) {
						try { Thread.sleep(brojSekundiZaSpavanje * 1000); } catch (InterruptedException e) {} // sacekaj onoliko sekundi koliko je zadato
					}
					
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
		Igrac protivnik = null;
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO){
			igra =  trenutnoStanje.getPlaviIgrac();
			protivnik = trenutnoStanje.getCrveniIgrac();
		}else {
			igra = trenutnoStanje.getCrveniIgrac();
			protivnik = trenutnoStanje.getPlaviIgrac();
		}
		this.miniMaxAlgoritam.setDubinaStabla(4);
		this.miniMaxAlgoritam.setIgrac(igra);
		//this.miniMaxAlgoritam.setProtivnik(protivnik);
		
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
		Igrac igra = null;
		Igrac protivnik = null;
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO){
			igra =  trenutnoStanje.getPlaviIgrac();
			protivnik = trenutnoStanje.getCrveniIgrac();
		}else {
			igra = trenutnoStanje.getCrveniIgrac();
			protivnik = trenutnoStanje.getPlaviIgrac();
		}
		this.miniMaxAlgoritam.setDubinaStabla(4);
		this.miniMaxAlgoritam.setIgrac(igra);
		this.miniMaxAlgoritam.setProtivnik(protivnik);
		
		
		Potez potez = miniMaxAlgoritam.noviPotezJediFiguru(trenutnoStanje2);
		if(potez != null) {	
			Stanje staroStanje = new Stanje(trenutnoStanje2);
			double stariScore = staroStanje.getScore();
					
			izvrsiJedenjeFigure(trenutnoStanje2, potez.getPolje(), b);
			
			ispisiPotezNaKonzoli(potez);
			
		}
		
	}

	public static Potez pretvoriSelektovanoPoljeiAkcijuUPotez(Stanje stanje, PoljeAkcija novoSelektovanoPoljeIAkcija) {
		Polje[][] polja = stanje.getPolja();
		
		Akcija akcija = novoSelektovanoPoljeIAkcija.getAkcija();
		String akcijaName = akcija.name();
		Polje polje;
		Polje selektovanoPolje;
		
		if(stanje.isPojedi() || akcijaName.contains("POSTAVI")) {
			selektovanoPolje = null;
			polje = novoSelektovanoPoljeIAkcija.getPolje();
		}
		else {
			selektovanoPolje = novoSelektovanoPoljeIAkcija.getPolje();
			
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
	
	private Potez odigrajPotezIEventualnoPojediProtivnikovuFiguru(Stanje stanje, boolean predvidjanjeProtivnikovogPoteza) {
		/*Igrac igrac;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) igrac = stanje.getPlaviIgrac();
		else igrac = stanje.getCrveniIgrac();
			
		igrac.setSelektovanoPolje(null);*/
		PoljeAkcija novoSelektovanoPoljeIAkcija = reinforcementLearning.getNovoSelektovanoPoljeIAkcija(stanje);
		
		if(prikaziDialogTabelaAkcijaIQVrednosti && !predvidjanjeProtivnikovogPoteza) glavniProzor.prikaziTabeluAkcijaiQVrednosti(reinforcementLearning.getqVrednostiZaMoguceAkcije(), novoSelektovanoPoljeIAkcija);
		
		if(novoSelektovanoPoljeIAkcija == null) {
			return null;
		}
		
		Potez potez = pretvoriSelektovanoPoljeiAkcijuUPotez(stanje, novoSelektovanoPoljeIAkcija);
		if(potez == null) return null;
		
		if(potez.getSelektovanoPolje() != null) {
			stanje.setSelektovanoPolje(potez.getSelektovanoPolje());
			
			if(!predvidjanjeProtivnikovogPoteza) {
				glavniProzor.osveziTablu();
				
				if(brojSekundiZaSpavanje > 0) {
					try { Thread.sleep(brojSekundiZaSpavanje*1000); } catch (InterruptedException e) {} // sacekaj onoliko sekundi koliko je zadato
				}
			}
			
			/*Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			    			if(brojSekundiZaSpavanje > 0) {
			    				try { Thread.sleep(brojSekundiZaSpavanje * 1000); } catch (InterruptedException e) {} // sacekaj onoliko sekundi koliko je zadato
			    			}
			    			
			    			napraviPotez(stanje, potez.getPolje(), potez.getSelektovanoPolje(), azurirajPomocniPanel);
			    			
			    			if(stanje.isPojedi()) {
			    				pojediFiguru(stanje, azurirajPomocniPanel);
			    			}
			    			
			    			//glavniProzor.osveziTablu();
			            }
			        });
					
				}
			});
			t.start();*/
				
		}
		
		napraviPotez(stanje, potez.getPolje(), potez.getSelektovanoPolje(), predvidjanjeProtivnikovogPoteza);
		
		if(stanje.isPojedi()) {
			pojediFiguru(stanje, predvidjanjeProtivnikovogPoteza);
		}
		
		ispisiPotezNaKonzoli(potez);
		
		return potez;
	}

	private Stanje predvidiProtivnikovPotezINapraviNovoStanje(Stanje stanje) {
		Stanje novoStanje = new Stanje(stanje);
		
		odigrajPotezIEventualnoPojediProtivnikovuFiguru(novoStanje, true);
		
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
	
	public Stanje getTrenutnoStanje() {
		return trenutnoStanje;
	}

	public GlavniProzor getGlavniProzor() {
		return glavniProzor;
	}

	public void setGlavniProzor(GlavniProzor glavniProzor) {
		this.glavniProzor = glavniProzor;
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

	public int getBrojSekundiZaSpavanje() {
		return brojSekundiZaSpavanje;
	}

	public void setBrojSekundiZaSpavanje(int brojSekundiZaSpavanje, boolean azurirajVrednostUPomocnomPanelu) {
		this.brojSekundiZaSpavanje = brojSekundiZaSpavanje;
		if(azurirajVrednostUPomocnomPanelu) glavniProzor.setBrojSekundiUPomocnomPanelu(this.brojSekundiZaSpavanje);
	}
	
	public void spremiJedenje(Stanje stanje, boolean predvidjanjeProtivnikovogPoteza) {
		String figuraZaJedenje;
		TipPolja tipFigureKojuTrebaPojesti;
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			figuraZaJedenje = "CRVENU";
			tipFigureKojuTrebaPojesti = TipPolja.CRVENO;
		}
		else {
			figuraZaJedenje = "PLAVU";
			tipFigureKojuTrebaPojesti = TipPolja.PLAVO;
		}
		
		stanje.setPojedi(true);
		
		if(!predvidjanjeProtivnikovogPoteza) {
			glavniProzor.podesiPojediLabel(figuraZaJedenje, tipFigureKojuTrebaPojesti);
			glavniProzor.prikaziPanelZaPojediLabel();
		}
	
	}
	
	public void zavrsiJedenje(Stanje stanje, boolean predvidjanjeProtivnikovogPoteza) {
		if(stanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			Igrac crveniIgrac = stanje.getCrveniIgrac();
			crveniIgrac.umanjiBrojPreostalihFigura();
			if(!predvidjanjeProtivnikovogPoteza) glavniProzor.podesiBrojCrvenihPreostalihFigura(crveniIgrac.getBrojPreostalihFigura());
		}
		else {
			Igrac plaviIgrac = stanje.getPlaviIgrac();
			plaviIgrac.umanjiBrojPreostalihFigura();
			if(!predvidjanjeProtivnikovogPoteza) glavniProzor.podesiBrojPlavihPreostalihFigura(plaviIgrac.getBrojPreostalihFigura());
		}
		
		stanje.setPojedi(false);
	
		if(!predvidjanjeProtivnikovogPoteza) glavniProzor.sakrijPanelZaPojediLabel();
		
		noviPotez(stanje, predvidjanjeProtivnikovogPoteza);
	}

	public void napraviPotez(Stanje stanje, Polje polje, Polje selektovanoPolje, boolean predvidjanjeProtivnikovogPoteza) {
		izvrsiPostavljanjeIliPomeranjeFigura(stanje, polje, selektovanoPolje);
		
		boolean daLijeUTariStaro = polje.isDaLiJeUTari();
		stanje.podesiPoljaUTaramaIVanNjih();
		
		if(!daLijeUTariStaro && polje.isDaLiJeUTari())  {
			spremiJedenje(stanje, predvidjanjeProtivnikovogPoteza); // kasnije prilikom jedenja bice setovano - proveriKrajIgre = true;
		}
		else {
			noviPotez(stanje, predvidjanjeProtivnikovogPoteza);
			
			if(selektovanoPolje == null) {
				if (stanje.daLiSuSveFigurePostavljene()) proveriKrajIgre = true;
			}
			else {
				proveriKrajIgre = true;
			}
			
		}
		
	}
	
	public static void izvrsiPostavljanjeIliPomeranjeFigura(Stanje stanje, Polje polje, Polje selektovanoPolje) {
		if(selektovanoPolje == null)  { // jos nisu sve figure postavljene, i sada postavljamo
			polje.setTipPolja(stanje.getIgracNaPotezu());
		}
		else { // sve figure su vec postavljene
			polje.setTipPolja(selektovanoPolje.getTipPolja());
			selektovanoPolje.setTipPolja(TipPolja.ZUTO);
		}
		
		stanje.setSelektovanoPolje(polje);
	}

	private void pojediFiguru(Stanje stanje, boolean azurirajPomocniPanel) {
		if(this.brojSekundiZaSpavanje > 0) {
			try { Thread.sleep(this.brojSekundiZaSpavanje * 1000); } catch (InterruptedException e) {} // sacekaj onoliko sekundi koliko je zadato
		}
		
		/*ArrayList<Polje> protivnikovaPolja = getProtivnikovaPolja(stanje);
		
		int indeksPoljaKojeTrebaPojesti;
		
		while(pojediPlavog || pojediCrvenog) { // pokusavaj random da pojedes protivnikovu figuru dok ne nabodes ispravnu
			indeksPoljaKojeTrebaPojesti = random.nextInt(protivnikovaPolja.size());
			JedenjeFigure(stanje, protivnikovaPolja.get(indeksPoljaKojeTrebaPojesti), azurirajPomocniPanel);
		}*/
		
		PoljeAkcija novoSelektovanoPoljeIAkcija = reinforcementLearning.getNovoSelektovanoPoljeIAkcija(stanje);
		
		if(prikaziDialogTabelaAkcijaIQVrednosti) glavniProzor.prikaziTabeluAkcijaiQVrednosti(reinforcementLearning.getqVrednostiZaMoguceAkcije(), novoSelektovanoPoljeIAkcija);
		
		if(novoSelektovanoPoljeIAkcija != null) {
			Potez potez = pretvoriSelektovanoPoljeiAkcijuUPotez(stanje, novoSelektovanoPoljeIAkcija);
			
			if(potez != null) {	
				Stanje staroStanje = new Stanje(stanje);
				staroStanje.setSelektovanoPolje(potez.getPolje());
				//staroStanje.setSelektovanoPolje(potez.getSelektovanoPolje());
				
				double stariScore = staroStanje.getScore();
						
				izvrsiJedenjeFigure(stanje, potez.getPolje(), azurirajPomocniPanel);
				
				Stanje potencijalnoNovoStanje = predvidiProtivnikovPotezINapraviNovoStanje(stanje);
				
				double nagrada = stanje.izracunajScore(staroStanje.getIgracNaPotezu()) - stariScore;
				reinforcementLearning.postaviNovuQVrednost(staroStanje, potez.getAkcija(), potencijalnoNovoStanje, nagrada);
			}
			else {
				JOptionPane.showMessageDialog(null, "Potez == null prilikom jedenja, Prilikom jedenja", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "NovoSelektovanoPoljeIAkcija == null, Prilikom jedenja", "Error (Prilikom jedenja)", JOptionPane.ERROR_MESSAGE);
		}
		
		//if(prikaziDialogTabelaAkcijaIQVrednosti) glavniProzor.prikaziTabeluAkcijaiQVrednosti(reinforcementLearning.getqVrednostiZaMoguceAkcije());
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

	public void odreagujNaMouseReleased(MouseEvent e) {
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			if(trenutnoStanje.getPlaviIgrac().getAlgoritam() != Algoritam.COVEK) return;
		}
		else {
			if(trenutnoStanje.getCrveniIgrac().getAlgoritam() != Algoritam.COVEK) return;
		}
		
		Polje polje = getPoljeOnClicked(e.getPoint()); 
		if(polje != null) {
			//controller.setProveriKrajIgre(false);
			/*if ((controller.getIgracNaPotezu() == IgracType.PLAVI && selektovanoPolje.getBoja() == Color.RED) || (controller.getIgracNaPotezu() == IgracType.CRVENI && selektovanoPolje.getBoja() == Color.BLUE)) {
				return;
			}*/
			
			if(trenutnoStanje.isPojedi()) {
				izvrsiJedenjeFigure(trenutnoStanje, polje, true);
			}
			else {
				if(trenutnoStanje.daLiSuSveFigurePostavljene()) {
					Polje selektovanoPolje = trenutnoStanje.getSelektovanoPolje();
					if (selektovanoPolje != null) {
						if ((trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO && selektovanoPolje.getTipPolja() == TipPolja.PLAVO) || (trenutnoStanje.getIgracNaPotezu() == TipPolja.CRVENO && selektovanoPolje.getTipPolja() == TipPolja.CRVENO)) {
							if(daLiJeIspravanPotez(selektovanoPolje.getPozicija(), polje.getPozicija())) {
								if(selektovanoPolje.getTipPolja() != TipPolja.ZUTO) {
									if(polje.getTipPolja() == TipPolja.ZUTO) {
										napraviPotez(trenutnoStanje, polje, selektovanoPolje, false);
										potezNapravljen = true;
									}
									
								}
							}
						}
						
					} 
								
					trenutnoStanje.setSelektovanoPolje(polje);	
				}
				else {
					if(polje.getTipPolja() == TipPolja.ZUTO) {
						napraviPotez(trenutnoStanje, polje, null, false);
						potezNapravljen = true;
					}
				}
			}
			
			glavniProzor.osveziTablu();;
			
			if(proveriKrajIgre) {
				proveriKrajIgre = false;
				
				Rezultat rezultat = trenutnoStanje.krajIgre();
				if(rezultat != null) {
					rezultat.setBrojPoteza(Controller.brojPoteza);
					RadSaPodacima.upisiKrajnjiRezultatUFajl(rezultat);
					
					/*if(trenutnoStanje.getPlaviIgrac().getAlgoritam() == Algoritam.RL || trenutnoStanje.getCrveniIgrac().getAlgoritam() == Algoritam.RL) {
						RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(reinforcementLearning.getqVrednosti());
						RadSaPodacima.sacuvajTezineUFajl(reinforcementLearning.getTezine());
						//RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(controller.getReinforcementLearning().getBrojMenjanjaQVrednosti());
					}*/
					
					String poruka = rezultat.getPobednik() + " (" + rezultat.getAlgoritamPobednika()  + ") je pobednik!";
				    glavniProzor.dialogPocetni(poruka);
					
					return;
					//controller.setPotezNapravljen(false); // ovo postavljamo 
				}
				
			}
			
			if(potezNapravljen && !trenutnoStanje.isPojedi()) {
				potezNapravljen = false;
				
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						//controller.noviPotez();
						Algoritam algoritam;
						if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
							algoritam = trenutnoStanje.getPlaviIgrac().getAlgoritam();
							Controller.brojPoteza++;
						}
						else {
							algoritam = trenutnoStanje.getCrveniIgrac().getAlgoritam();
						}
						
						if (algoritam != Algoritam.COVEK) {
							try { Thread.sleep(brojSekundiZaSpavanje * 1000); } catch (InterruptedException e) {} // sacekaj onoliko sekundi koliko je zadato
							
							// sad je bot na redu da odigra
							boteOdigrajPotez(algoritam);
						}
						
					}
				});
				t.start();
				
			}
			
		}
		else {
			trenutnoStanje.setSelektovanoPolje(null);
			
			glavniProzor.osveziTablu();
		}
		
	}
	
	public Polje getPoljeOnClicked(Point p) {
		Polje[][] polja = trenutnoStanje.getPolja();
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].contains(p)) {
					return polja[i][j];
				}
			}
		}
		
		return null;
	}

	public boolean isKoristioReinforcmentLearning() {
		return koristioReinforcmentLearning;
	}

	public void setKoristioReinforcmentLearning(boolean koristioReinforcmentLearning) {
		this.koristioReinforcmentLearning = koristioReinforcmentLearning;
	}

	public void podesiEpsilon(double epsilon, boolean azurirajVrednostUPomocnomPanelu) {
		reinforcementLearning.setEpsilon(epsilon);
		if(azurirajVrednostUPomocnomPanelu) glavniProzor.setEpsilonUPomocnomPanelu(epsilon);
		
	}

	public boolean isPrikaziDialogTabelaAkcijaIQVrednosti() {
		return prikaziDialogTabelaAkcijaIQVrednosti;
	}

	public void setPrikaziDialogTabelaAkcijaIQVrednosti(boolean prikaziDialogTabelaAkcijaIQVrednosti, boolean azurirajVrednostUPomocnomPanelu) {
		this.prikaziDialogTabelaAkcijaIQVrednosti = prikaziDialogTabelaAkcijaIQVrednosti;
		if(azurirajVrednostUPomocnomPanelu) glavniProzor.podesiCheckBoxZaPrikazTabele(prikaziDialogTabelaAkcijaIQVrednosti);
	}
	
}

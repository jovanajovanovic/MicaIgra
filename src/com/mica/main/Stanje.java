package com.mica.main;

import java.util.Arrays;

public class Stanje {
	private Polje[][] polja;
	
	private TipPolja igracNaPotezu;
	private Igrac plaviIgrac;
	private Igrac crveniIgrac;
	
	private boolean pojedi;
	
	private double score;
	
	private final double konstantaZaMojeTare = 18;
	private final double konstantaZaProtivnikoveTare = 18;
	private final double konstantaZaMojePoluTare = 10;
	private final double konstantaZaProtivnikovePoluTare = 7;
	
	private final double konstantaZaKrajIgre = 1000;
	
	public Stanje() {
		
	}
	
	public Stanje(TipPolja igracNaPotezu) {
		polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(i, j, TipPolja.ZUTO);;
			}
			
		}
		
		this.plaviIgrac = new Igrac("Plavi", TipPolja.PLAVO, Controller.BROJ_FIGURA, Controller.BROJ_FIGURA);
		this.crveniIgrac = new Igrac("Crveni", TipPolja.CRVENO, Controller.BROJ_FIGURA, Controller.BROJ_FIGURA);
		
		this.igracNaPotezu = igracNaPotezu;
		this.pojedi = false;
		
		this.score = 0;
	}
	
	public Stanje(Polje[][] polja, TipPolja igracNaPotezu, Igrac plaviIgrac, Igrac crveniIgrac) {
		this.polja = polja;
		this.igracNaPotezu = igracNaPotezu;
		this.plaviIgrac = plaviIgrac;
		this.crveniIgrac = crveniIgrac;
	}
	
	public Stanje(Stanje stanje) {
		this.igracNaPotezu = stanje.igracNaPotezu;
			
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(stanje.polja[i][j]);
			}
			
		}
		
		this.polja = polja;
		
		this.plaviIgrac = new Igrac(stanje.plaviIgrac, polja);
		this.crveniIgrac = new Igrac(stanje.crveniIgrac, polja);
		
		this.pojedi = stanje.pojedi;
		
		this.score = stanje.score;
	}

	public static Stanje kreirajStanje(String[] tokeniStanje) {
		String[] tokeniIgrac, tokeni;
		String tokenPolje;
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		Igrac plaviIgrac, crveniIgrac;
		TipPolja igracNaPotezu;
		
		try {
			if(tokeniStanje.length != 4) return null;
			
			tokeni = tokeniStanje[0].trim().split(";");
			if(tokeni.length != 24) return null;
			
			for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
				for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
					tokenPolje = tokeni[j + Controller.BROJ_POLJA_U_KRUGU*i].trim();
					
					polja[i][j] = new Polje(i, j, TipPolja.values()[Integer.parseInt(tokenPolje)]);
				}
			}
			
			tokeniIgrac = tokeniStanje[1].trim().split(";");
			if(tokeniIgrac.length != 5) return null;
			plaviIgrac = Igrac.kreirajIgraca(tokeniIgrac, polja);
			if(plaviIgrac == null) return null;
			
			tokeniIgrac = tokeniStanje[2].trim().split(";");
			if(tokeniIgrac.length != 5) return null;
			crveniIgrac = Igrac.kreirajIgraca(tokeniIgrac, polja);
			if(crveniIgrac == null) return null;
			
			igracNaPotezu = TipPolja.values()[Integer.parseInt(tokeniStanje[3].trim())];
			
			return new Stanje(polja, igracNaPotezu, plaviIgrac, crveniIgrac);
		}
		catch(Exception e) {
			return null;
		}
		
	}
	
	
	public boolean daLiSuSveFigurePostavljene() {
		return plaviIgrac.getBrojNepostavljenihFigura() == 0 && crveniIgrac.getBrojNepostavljenihFigura() == 0;
	}
	
	public void podesiPoljaUTaramaIVanNjih() {
		TipPolja tipPolja;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				tipPolja = polja[i][j].getTipPolja();
				
				if(polja[i][j+1].getTipPolja() == tipPolja && polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
					polja[i][j].setDaLiJeUTari(true);
					polja[i][j+1].setDaLiJeUTari(true);
					polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].setDaLiJeUTari(true);
				}
				else {
					if(j == 0) polja[i][j].setDaLiJeUTari(false);
					polja[i][j+1].setDaLiJeUTari(false);
					if(j != 6) polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].setDaLiJeUTari(false);
				}
			}
		}
		
		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
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

	public Rezultat krajIgre() {
		if (plaviIgrac.getBrojPreostalihFigura() == 2) {
			return new Rezultat("Crveni", crveniIgrac.getAlgoritam(), "Plavi", plaviIgrac.getAlgoritam());
		}
		else if(crveniIgrac.getBrojPreostalihFigura() == 2) {
			return new Rezultat("Plavi", plaviIgrac.getAlgoritam(), "Crveni", crveniIgrac.getAlgoritam());
		}
		
		
		Igrac igrac;
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) {
			igrac = plaviIgrac;
		}
		else {
			igrac = crveniIgrac;
		}
		
		// Ako su mu preostale 3 figure, sigurno se moze mrdati, jer moze da skace, pa zato necemo nastaviti proveru
		if(igrac.getBrojPreostalihFigura() == 3) {
			return null;
		}
		
		// moze li se protivnik mrdati?
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() != igracNaPotezu) continue;
				
				if(j % 2 == 1) {
					if(polja[i][(j+1) % polja[i].length].getTipPolja() == TipPolja.ZUTO || polja[i][j-1].getTipPolja() == TipPolja.ZUTO) {
						return null;
					}
					
					if(i == 0 || i == 2) {
						if(polja[1][j].getTipPolja() == TipPolja.ZUTO) {
							return null;
						}
					}
					
					if(i == 1) {
						if(polja[0][j].getTipPolja() == TipPolja.ZUTO || polja[2][j].getTipPolja() == TipPolja.ZUTO) {
							return null;
						}
					}
				}
				else {
					int a = j-1;
					if(a < 0) {
						a += polja[i].length;
					}
					if(polja[i][j+1].getTipPolja() == TipPolja.ZUTO || polja[i][a].getTipPolja() == TipPolja.ZUTO ) {
						return null;
					}
				}
			}
		}
		
		Rezultat rezultat;
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) {
			rezultat = new Rezultat("Crveni", crveniIgrac.getAlgoritam(), "Plavi", plaviIgrac.getAlgoritam());
		}
		else {
			rezultat = new Rezultat("Plavi", plaviIgrac.getAlgoritam(), "Crveni", crveniIgrac.getAlgoritam());
		}

		
		return rezultat;
	}

	public Polje[][] getPolja() {
		return polja;
	}

	public void setPolja(Polje[][] polja) {
		this.polja = polja;
	}
	
	public TipPolja getIgracNaPotezu() {
		return igracNaPotezu;
	}
	
	public void setIgracNaPotezu(TipPolja igracNaPotezu) {
		this.igracNaPotezu = igracNaPotezu;
	}

	public Igrac getPlaviIgrac() {
		return plaviIgrac;
	}

	public void setPlaviIgrac(Igrac plaviIgrac) {
		this.plaviIgrac = plaviIgrac;
	}

	public Igrac getCrveniIgrac() {
		return crveniIgrac;
	}

	public void setCrveniIgrac(Igrac crveniIgrac) {
		this.crveniIgrac = crveniIgrac;
	}
	
	public boolean isPojedi() {
		return pojedi;
	}

	public void setPojedi(boolean pojedi) {
		this.pojedi = pojedi;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double izracunajScore() {
		/*if(igracNaPotezu == TipPolja.PLAVO) {
			return plaviIgrac.getBrojPreostalihFigura() + (Controller.BROJ_FIGURA - crveniIgrac.getBrojPreostalihFigura());
		}
		
		return crveniIgrac.getBrojPreostalihFigura() + (Controller.BROJ_FIGURA - plaviIgrac.getBrojPreostalihFigura());
		*/
		score = 0.0;
		Rezultat rezultat = krajIgre();
		if(rezultat == null) {
			int plaviBrojTara = prebrojTare(TipPolja.PLAVO);
			int crveniBrojTara = prebrojTare(TipPolja.CRVENO); 
			
			int plaviBrojPoluTara = prebrojPoluTare(TipPolja.PLAVO);
			int crveniBrojPoluTara = prebrojPoluTare(TipPolja.CRVENO); 
			
			if(igracNaPotezu == TipPolja.PLAVO) {
				score += plaviIgrac.getBrojPreostalihFigura() - crveniIgrac.getBrojPreostalihFigura();
				score += konstantaZaMojeTare*plaviBrojTara - konstantaZaProtivnikoveTare*crveniBrojTara;
				score += konstantaZaMojePoluTare*plaviBrojPoluTara - konstantaZaProtivnikovePoluTare*crveniBrojPoluTara;
			}
			else {
				score += crveniIgrac.getBrojPreostalihFigura() - plaviIgrac.getBrojPreostalihFigura();
				score += konstantaZaMojeTare*crveniBrojTara - konstantaZaProtivnikoveTare*plaviBrojTara;
				score += konstantaZaMojePoluTare*crveniBrojPoluTara - konstantaZaProtivnikovePoluTare*plaviBrojPoluTara;
			}
		}
		else {
			String pobednik = rezultat.getPobednik();
			pobednik = pobednik.substring(0, pobednik.length() -1) + "o";
		
			if(igracNaPotezu.name().equalsIgnoreCase(pobednik)) {
				score = konstantaZaKrajIgre;
			}
			else {
				score = -konstantaZaKrajIgre;
			}
		}
	
		return score;
	}
	
	private int prebrojTare(TipPolja tipPolja) {
		int brojTara = 0;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojTara++;
				}
			}
		}
		
		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == tipPolja && polja[2][k].getTipPolja() == tipPolja) {
				brojTara++;
			}
		}
		
		return brojTara;
	}
	
	private int prebrojPoluTare(TipPolja tipPolja) {
		// polu tare == situacije u kojima su dve figure vec u redu,
		// a treca moze biti stavljena u red vec u sledecem potezu
		
		/*  o + +    |    + + o     |   + o + 
		    +        |        +     |     + 
	    */
		
		boolean postavljene = daLiSuSveFigurePostavljene();
		
		int brojPoluTara = 0;
		
		int pomocniIndeks1;
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				pomocniIndeks1 = j-1;
				
				if (pomocniIndeks1 == -1) pomocniIndeks1 = 7;
				
				if(polja[i][j].getTipPolja() == TipPolja.ZUTO && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					if(!postavljene) {
						brojPoluTara++;
					}
					else if(polja[i][pomocniIndeks1].getTipPolja() == tipPolja) {
						brojPoluTara++;
					}
				}
				else if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == TipPolja.ZUTO 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					if(!postavljene) {
						brojPoluTara++;
					}
					else if(i == 0 || i == 2) {
						if(polja[1][(j+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojPoluTara++;
						}
					}
					else if(i == 1) {
						if(polja[0][(j+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja 
								|| polja[2][(j+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
							brojPoluTara++;
						}
					}
				
				}
				else if(polja[i][j].getTipPolja() == tipPolja && polja[i][j+1].getTipPolja() == tipPolja 
						&& polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == TipPolja.ZUTO) {
					if(!postavljene) {
						brojPoluTara++;
					}
					else if(polja[i][(j+3)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
						brojPoluTara++;
					}
				}
			}
		}
		

		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			if(polja[0][k].getTipPolja() == TipPolja.ZUTO && polja[1][k].getTipPolja() == tipPolja 
					&& polja[2][k].getTipPolja() == tipPolja) {
				if(!postavljene) {
					brojPoluTara++;
				}
				else if(polja[0][k-1].getTipPolja() == tipPolja || polja[0][(k+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojPoluTara++;
				}
				
			}
				
			else if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == TipPolja.ZUTO 
					&& polja[2][k].getTipPolja() == tipPolja) {
				if(!postavljene) {
					brojPoluTara++;
				}
				else if(polja[1][k-1].getTipPolja() == tipPolja || polja[1][(k+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojPoluTara++;
				}
			}
			
			else if(polja[0][k].getTipPolja() == tipPolja && polja[1][k].getTipPolja() == tipPolja 
					&& polja[2][k].getTipPolja() == TipPolja.ZUTO) {
				if(!postavljene) {
					brojPoluTara++;
				}
				else if(polja[2][k-1].getTipPolja() == tipPolja || polja[2][(k+1)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja) {
					brojPoluTara++;
				}
			}
		}
		
		return brojPoluTara;
	}
	
	public boolean daLiSuSvaProtivnickaPoljaUTari(TipPolja tipPoljaZaJedenje) {
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() != tipPoljaZaJedenje) continue;
				
				if(!polja[i][j].isDaLiJeUTari()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void horizontalnaOsnaSimetrija() {
		int[] indeksi1 = {2, 3, 4};
		int[] indeksi2 = {0, 7, 6};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void vertikalnaOsnaSimetrija() {
		int[] indeksi1 = {0, 1, 2};
		int[] indeksi2 = {6, 5, 4};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void gornjiLeviDonjiDesniKosaOsnaSimetrija() {
		int[] indeksi1 = {1, 2, 3};
		int[] indeksi2 = {7, 6, 5};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void gornjiDesniDonjiLeviKosaOsnaSimetrija() {
		int[] indeksi1 = {3, 4, 5};
		int[] indeksi2 = {1, 0, 7};
		
		osnaSimetrija(indeksi1, indeksi2);
	}
	
	public void osnaSimetrija(int[] indeksi1, int[] indeksi2) {
		TipPolja pomTipPolja;
		boolean pomDaLiJeUTari;
		int indeks1, indeks2;
		
		boolean josNijePromenjenoPlavoSelektovanoPolje = true;
		boolean josNijePromenjenoCrvenoSelektovanoPolje = true;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < indeksi1.length; j++) {
				indeks1 = indeksi1[j];
				indeks2 = indeksi2[j];
				pomTipPolja = polja[i][indeks1].getTipPolja();
				pomDaLiJeUTari = polja[i][indeks1].isDaLiJeUTari();
				
				polja[i][indeks1].setTipPolja(polja[i][indeks2].getTipPolja());
				polja[i][indeks1].setDaLiJeUTari(polja[i][indeks2].isDaLiJeUTari());
				
				polja[i][indeks2].setTipPolja(pomTipPolja);
				polja[i][indeks2].setDaLiJeUTari(pomDaLiJeUTari);
				
				if(josNijePromenjenoPlavoSelektovanoPolje) {
					josNijePromenjenoPlavoSelektovanoPolje = !promeniSelektovanoPolje(plaviIgrac, polja, i, indeks1, indeks2);
				}
				
				if(josNijePromenjenoCrvenoSelektovanoPolje) {
					josNijePromenjenoCrvenoSelektovanoPolje = !promeniSelektovanoPolje(plaviIgrac, polja, i, indeks1, indeks2);
				}
			}
		}
	}
	
	private boolean promeniSelektovanoPolje(Igrac igrac, Polje[][] polja, int x, int indeks1, int indeks2) {
		if(igrac.getSelektovanoPolje() != null) {
			if(igrac.getSelektovanoPolje().getPozicija().getX() == x) {
				if(igrac.getSelektovanoPolje().getPozicija().getY() == indeks1) {
					igrac.setSelektovanoPolje(polja[x][indeks2]);
					return true;
				}
				else if(igrac.getSelektovanoPolje().getPozicija().getY() == indeks2) {
					igrac.setSelektovanoPolje(polja[x][indeks1]);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void promeniIgracNaPotezuIgraceIBojeFigurama() {
		TipPolja stariIgracNaPotezu = igracNaPotezu;
		
		if(igracNaPotezu == TipPolja.PLAVO) igracNaPotezu = TipPolja.CRVENO;
		else igracNaPotezu = TipPolja.PLAVO;
		
		Igrac pomIgrac = plaviIgrac;
		plaviIgrac = crveniIgrac;
		crveniIgrac = pomIgrac;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if (polja[i][j].getTipPolja() == TipPolja.ZUTO) continue;
				
				if(polja[i][j].getTipPolja() == stariIgracNaPotezu) {
					polja[i][j].setTipPolja(igracNaPotezu);
				}
				else {
					polja[i][j].setTipPolja(stariIgracNaPotezu);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sbPolja = new StringBuilder("");
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				sbPolja.append(";");
				sbPolja.append(polja[i][j]);
			}
		}
		
		StringBuilder sb = new StringBuilder("");
		sb.append(sbPolja.toString().substring(1));
		sb.append(":"); sb.append(plaviIgrac); sb.append(":"); sb.append(crveniIgrac);
		sb.append(":"); sb.append(igracNaPotezu.ordinal());
		
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crveniIgrac == null) ? 0 : crveniIgrac.hashCode());
		result = prime * result + ((igracNaPotezu == null) ? 0 : igracNaPotezu.hashCode());
		result = prime * result + ((plaviIgrac == null) ? 0 : plaviIgrac.hashCode());
		result = prime * result + Arrays.deepHashCode(polja);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Stanje) {
			Stanje stanje = (Stanje) obj;
			
			for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
				for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
					if(polja[i][j].getTipPolja() != stanje.polja[i][j].getTipPolja()) return false;
				}
			}
			
			if(igracNaPotezu != stanje.igracNaPotezu) return false;
			
			if(!crveniIgrac.equals(stanje.crveniIgrac)) return false;
			
			if(!plaviIgrac.equals(stanje.plaviIgrac)) return false;
			
			if(score != stanje.score) return false; 
			
			return true;
		}
		
		return false;
	}
	
}

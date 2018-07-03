package com.mica.main;

import java.util.Arrays;

public class Stanje {
	private Polje[][] polja;
	
	private TipPolja igracNaPotezu;
	private Igrac plaviIgrac;
	private Igrac crveniIgrac;
	
	private double score;
	
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
		this.plaviIgrac = new Igrac(stanje.plaviIgrac);
		this.crveniIgrac = new Igrac(stanje.crveniIgrac);
			
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(stanje.polja[i][j]);
			}
			
		}
		
		this.polja = polja;
		this.score = stanje.score;
	}

	public static Stanje kreirajStanje(String[] tokeniStanje) {
		String[] tokeniIgrac, tokeniPolje, tokeni;
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		Igrac plaviIgrac, crveniIgrac;
		TipPolja igracNaPotezu;
		
		try {
			if(tokeniStanje.length != 4) return null;
			
			tokeni = tokeniStanje[0].trim().split(";");
			if(tokeni.length != 24) return null;
			
			for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
				for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
					tokeniPolje = tokeni[j + Controller.BROJ_POLJA_U_KRUGU*i].split(",");
					if(tokeniPolje.length != 2) return null;
					
					polja[i][j] = new Polje(i, j, TipPolja.valueOf(tokeniPolje[0]), Boolean.parseBoolean(tokeniPolje[1]));
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
			
			igracNaPotezu = TipPolja.valueOf(tokeniStanje[3].trim());
			
			return new Stanje(polja, igracNaPotezu, plaviIgrac, crveniIgrac);
		}
		catch(Exception e) {
			return null;
		}
		
	}
	
	
	public boolean daLiSuSveFigurePostavljene() {
		return plaviIgrac.getBrojNepostavljenihFigura() == 0 && crveniIgrac.getBrojNepostavljenihFigura() == 0;
	}

	public String krajIgre() {
		if (plaviIgrac.getBrojPreostalihFigura() == 2) {
			return "CRVENI";
		}
		else if(crveniIgrac.getBrojPreostalihFigura() == 2) {
			return "PLAVI";
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
		for (int i = 0; i < polja.length; i++) {
			for (int j = 0; j < polja[i].length; j++) {
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
		
		String pobednik;
		// vec je promenjen potez, tj. zapocet novi
		if(igracNaPotezu == TipPolja.PLAVO) pobednik = "CRVENI";
		else pobednik = "PLAVI";

		
		return pobednik;
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
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double izracunajScore(TipPolja igracNaPotezu) {
		if(igracNaPotezu == TipPolja.PLAVO) {
			return plaviIgrac.getBrojPreostalihFigura() + (Controller.BROJ_FIGURA - crveniIgrac.getBrojPreostalihFigura());
		}
		
		return crveniIgrac.getBrojPreostalihFigura() + (Controller.BROJ_FIGURA - plaviIgrac.getBrojPreostalihFigura());
	}
	
	@Override
	public String toString() {
		StringBuilder sbPolja = new StringBuilder("");
		
		for (int i = 0; i < polja.length; i++) {
			for (int j = 0; j < polja[0].length; j++) {
				sbPolja.append(";");
				sbPolja.append(polja[i][j]);
			}
		}
		
		StringBuilder sb = new StringBuilder("");
		sb.append(sbPolja.toString().substring(1));
		sb.append(":"); sb.append(plaviIgrac); sb.append(":"); sb.append(crveniIgrac);
		sb.append(":"); sb.append(igracNaPotezu);
		
		
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
			
			for (int i = 0; i < polja.length; i++) {
				for (int j = 0; j < polja[0].length; j++) {
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

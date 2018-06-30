package com.mica.main;

import com.mica.gui.Polje;

public class Stanje {
	private Polje[][] polja;
	
	private TipPolja igracNaPotezu;
	private Igrac plaviIgrac;
	private Igrac crveniIgrac;
	
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
	}
	
	public Stanje(Polje[][] polja, TipPolja igracNaPotezu, Igrac plaviIgrac, Igrac crveniIgrac) {
		this.polja = polja;
		this.igracNaPotezu = igracNaPotezu;
		this.plaviIgrac = plaviIgrac;
		this.crveniIgrac = crveniIgrac;
	}
	
	public Stanje(Stanje stanje) {
		this.igracNaPotezu = stanje.igracNaPotezu;
		this.plaviIgrac = new Igrac(plaviIgrac);
		this.crveniIgrac = new Igrac(crveniIgrac);
			
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) { 
				polja[i][j] = new Polje(stanje.polja[i][j]);
			}
			
		}
		
		this.polja = polja;
	}

	public static Stanje kreirajStanje(String[] tokeniStanje) {
		String[] tokeniIgrac, tokeniPolje, tokeni;
		Polje[][] polja = new Polje[Controller.BROJ_KRUGOVA][Controller.BROJ_POLJA_U_KRUGU];
		Igrac plaviIgrac, crveniIgrac;
		TipPolja igracNaPotezu;
		
		try {
			if(tokeniStanje.length != 4) return null;
			
			igracNaPotezu = TipPolja.valueOf(tokeniStanje[0].trim());
			
			tokeniIgrac = tokeniStanje[1].trim().split(",");
			if(tokeniIgrac.length != 4) return null;
			plaviIgrac = Igrac.kreirajStanjet(tokeniIgrac);
			if(plaviIgrac == null) return null;
			
			tokeniIgrac = tokeniStanje[2].trim().split(",");
			if(tokeniIgrac.length != 4) return null;
			crveniIgrac = Igrac.kreirajStanjet(tokeniIgrac);
			if(crveniIgrac == null) return null;
			
			tokeni = tokeniStanje[3].trim().split(",");
			if(tokeni.length != 24) return null;
			
			for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
				for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
					tokeniPolje = tokeni[j + Controller.BROJ_POLJA_U_KRUGU*i].split(":");
					if(tokeniPolje.length != 2) return null;
					
					polja[i][j] = new Polje(i, j, TipPolja.valueOf(tokeniPolje[0]), Boolean.parseBoolean(tokeniPolje[1]));
				}
			}
			
			return new Stanje(polja, igracNaPotezu, plaviIgrac, crveniIgrac);
		}
		catch(Exception e) {
			return null;
		}
		
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
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Stanje) {
			Stanje stanje = (Stanje) obj;
			
			for (int i = 0; i < polja.length; i++) {
				for (int j = 0; j < polja[0].length; j++) {
					if(polja[i][j].getTipPolja() != stanje.polja[i][j].getTipPolja()) return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		
		for (int i = 0; i < polja.length; i++) {
			for (int j = 0; j < polja[0].length; j++) {
				sb.append(";");
				sb.append(polja[i][j]);
			}
		}
		
		return sb.toString().substring(2);
	}
	
}

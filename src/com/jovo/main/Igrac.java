package com.jovo.main;

public class Igrac {
	private String name;
	private TipPolja tipIgraca;
	private int brojNepostavljenihFigura;
	private int brojPreostalihFigura;
	
	public Igrac(String name, TipPolja tipIgraca, int brojNepostavljenihFigura, int brojPreostalihFigura) {	
		this.name = name;
		this.tipIgraca = tipIgraca;
		this.brojNepostavljenihFigura = brojNepostavljenihFigura;
		this.brojPreostalihFigura = brojPreostalihFigura;
	}
	
	public Igrac(Igrac igrac) {
		this.name = igrac.name;
		this.tipIgraca = igrac.tipIgraca;
		this.brojNepostavljenihFigura = igrac.brojNepostavljenihFigura;
		this.brojPreostalihFigura = igrac.brojPreostalihFigura;
	}
	
	public static Igrac kreirajStanjet(String[] tokeniIgrac) {
		try {
			Igrac igrac = new Igrac(tokeniIgrac[0].trim(), TipPolja.valueOf(tokeniIgrac[1].trim()), Integer.parseInt(tokeniIgrac[2].trim()), Integer.parseInt(tokeniIgrac[3].trim()));
			
			return igrac;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public void umanjiBrojNepostavljenihFigura() {
		brojNepostavljenihFigura--;
	}
	
	public void umanjiBrojPreostalihFigura() {
		brojPreostalihFigura--;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TipPolja getTipIgraca() {
		return tipIgraca;
	}

	public void setTipIgraca(TipPolja tipIgraca) {
		this.tipIgraca = tipIgraca;
	}

	public int getBrojNepostavljenihFigura() {
		return brojNepostavljenihFigura;
	}

	public void setBrojNepostavljenihFigura(int brojNepostavljenihFigura) {
		this.brojNepostavljenihFigura = brojNepostavljenihFigura;
	}

	public int getBrojPreostalihFigura() {
		return brojPreostalihFigura;
	}

	public void setBrojPreostalihFigura(int brojPreostalihFigura) {
		this.brojPreostalihFigura = brojPreostalihFigura;
	}
	
}

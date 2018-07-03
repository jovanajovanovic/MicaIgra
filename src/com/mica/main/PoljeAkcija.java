package com.mica.main;

public class PoljeAkcija {
	private Polje polje;
	private Akcija akcija;
	
	public PoljeAkcija(Polje polje, Akcija akcija) {
		this.polje = polje;
		this.akcija = akcija;
	}

	public Polje getPolje() {
		return polje;
	}

	public void setPolje(Polje polje) {
		this.polje = polje;
	}

	public Akcija getAkcija() {
		return akcija;
	}

	public void setAkcija(Akcija akcija) {
		this.akcija = akcija;
	}
}
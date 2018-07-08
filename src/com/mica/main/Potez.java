package com.mica.main;

public class Potez {
	private Polje polje;
	private Polje selektovanoPolje;
	private Akcija akcija;
	private Polje pojediPolje;
	
	public Potez(Polje polje, Polje selektovanoPolje, Akcija akcija) {
		this.polje = polje;
		this.selektovanoPolje = selektovanoPolje;
		this.akcija = akcija;
	}

	public Polje getPolje() {
		return polje;
	}

	public void setPolje(Polje polje) {
		this.polje = polje;
	}

	public Polje getSelektovanoPolje() {
		return selektovanoPolje;
	}

	public void setSelektovanoPolje(Polje selektovanoPolje) {
		this.selektovanoPolje = selektovanoPolje;
	}

	public Akcija getAkcija() {
		return akcija;
	}

	public void setAkcija(Akcija akcija) {
		this.akcija = akcija;
	}

	public Polje getPojediPolje() {
		return pojediPolje;
	}

	public void setPojediPolje(Polje pojediPolje) {
		this.pojediPolje = pojediPolje;
	}

	
}

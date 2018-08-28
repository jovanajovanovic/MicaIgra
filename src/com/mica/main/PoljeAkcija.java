package com.mica.main;

public class PoljeAkcija {
	private Polje polje;
	private Akcija akcija;
	private int score; 
	private Polje prethodnoPolje;
	private boolean jedenje;
	private Polje jediPolje;
	
	
	
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	public Polje getPrethodnoPolje() {
		return prethodnoPolje;
	}

	public void setPrethodnoPolje(Polje prethodnoPolje) {
		this.prethodnoPolje = prethodnoPolje;
	}

	public boolean isJedenje() {
		return jedenje;
	}

	public void setJedenje(boolean jedenje) {
		this.jedenje = jedenje;
	}

	public Polje getJediPolje() {
		return jediPolje;
	}

	public void setJediPolje(Polje jediPolje) {
		this.jediPolje = jediPolje;
	}
	
	
}
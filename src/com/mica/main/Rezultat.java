package com.mica.main;

public class Rezultat {
	private String pobednik;
	private Algoritam algoritamPobednika;
	private String gubitnik;
	private Algoritam algoritamGubitnika;
	private int brojPoteza;
	
	public Rezultat(String pobednik, Algoritam algoritamPobednika, String gubitnik, Algoritam algoritamGubitnika) {
		this.pobednik = pobednik;
		this.algoritamPobednika = algoritamPobednika;
		this.gubitnik = gubitnik;
		this.algoritamGubitnika = algoritamGubitnika;
		this.brojPoteza = 0;
	}

	public String getPobednik() {
		return pobednik;
	}

	public void setPobednik(String pobednik) {
		this.pobednik = pobednik;
	}

	public Algoritam getAlgoritamPobednika() {
		return algoritamPobednika;
	}

	public void setAlgoritamPobednika(Algoritam algoritamPobednika) {
		this.algoritamPobednika = algoritamPobednika;
	}

	public String getGubitnik() {
		return gubitnik;
	}

	public void setGubitnik(String gubitnik) {
		this.gubitnik = gubitnik;
	}

	public Algoritam getAlgoritamGubitnika() {
		return algoritamGubitnika;
	}

	public void setAlgoritamGubitnika(Algoritam algoritamGubitnika) {
		this.algoritamGubitnika = algoritamGubitnika;
	}

	public int getBrojPoteza() {
		return brojPoteza;
	}

	public void setBrojPoteza(int brojPoteza) {
		this.brojPoteza = brojPoteza;
	}
	
}

package com.mica.algorithms;

public class QVrednost {
	private double vrednost;
	private int brojMenjanja;
	
	
	public QVrednost(double vrednost, int brojMenjanja) {
		this.vrednost = vrednost;
		this.brojMenjanja = brojMenjanja;
	}
	public double getVrednost() {
		return vrednost;
	}
	public void setVrednost(double vrednost) {
		this.vrednost = vrednost;
	}
	public int getBrojMenjanja() {
		return brojMenjanja;
	}
	public void setBrojMenjanja(int brojMenjanja) {
		this.brojMenjanja = brojMenjanja;
	}
	
	
	@Override
	public String toString() {
		return vrednost + ";" + brojMenjanja;
	}
	
	public static QVrednost kreirajQVrednost(String[] tokeni) {
		try {
			return new QVrednost(Double.parseDouble(tokeni[0].trim()), Integer.parseInt(tokeni[1].trim()));
		} catch (Exception e) {
			return null;
		}
	}
}

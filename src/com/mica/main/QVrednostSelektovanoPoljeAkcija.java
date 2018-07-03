package com.mica.main;

public class QVrednostSelektovanoPoljeAkcija {
	private double qVrednost;
	private PoljeAkcija selektovanoPoljeAkcija;
	
	public QVrednostSelektovanoPoljeAkcija(double qVrednost, PoljeAkcija selektovanoPoljeAkcija) {
		this.qVrednost = qVrednost;
		this.selektovanoPoljeAkcija = selektovanoPoljeAkcija;
	}
	
	public double getqVrednost() {
		return qVrednost;
	}
	public void setqVrednost(double qVrednost) {
		this.qVrednost = qVrednost;
	}
	public PoljeAkcija getSelektovanoPoljeAkcija() {
		return selektovanoPoljeAkcija;
	}
	public void setSelektovanoPoljeAkcija(PoljeAkcija selektovanoPoljeAkcija) {
		this.selektovanoPoljeAkcija = selektovanoPoljeAkcija;
	}
	
}

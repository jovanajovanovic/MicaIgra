package com.mica.main;

public class QVrednostAkcija {
	private double qVrednost;
	private Akcija akcija;
	
	public QVrednostAkcija(double qVrednost, Akcija akcija) {
		this.qVrednost = qVrednost;
		this.akcija = akcija;
	}
	
	public double getqVrednost() {
		return qVrednost;
	}
	public void setqVrednost(double qVrednost) {
		this.qVrednost = qVrednost;
	}
	public Akcija getAkcija() {
		return akcija;
	}
	public void setAkcija(Akcija akcija) {
		this.akcija = akcija;
	}
	
}

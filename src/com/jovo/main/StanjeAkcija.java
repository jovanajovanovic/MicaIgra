package com.jovo.main;

public class StanjeAkcija {
	private Stanje stanje;
	private Akcija akcija;
	
	public StanjeAkcija(Stanje stanje, Akcija akcija) {
		this.stanje = stanje;
		this.akcija = akcija;
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setStanje(Stanje stanje) {
		this.stanje = stanje;
	}

	public Akcija getAkcija() {
		return akcija;
	}

	public void setAkcija(Akcija akcija) {
		this.akcija = akcija;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StanjeAkcija) {
			StanjeAkcija sa = (StanjeAkcija) obj;
			
			return stanje.equals(sa.stanje) && akcija == sa.akcija;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return stanje + "|" + akcija.name();
	}
}

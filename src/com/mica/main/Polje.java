package com.mica.main;

import java.awt.Point;

import com.mica.gui.Tabla;

public class Polje {
	private Pozicija pozicija;
	private TipPolja tipPolja;
	private boolean daLiJeUTari;
	
	public Polje(int sloj, int redniBrojUSloju, TipPolja tipPolja) {
		this.tipPolja = tipPolja;
		this.daLiJeUTari = false;
		this.pozicija = new Pozicija(sloj, redniBrojUSloju);
	}
	
	public Polje(int sloj, int redniBrojUSloju, TipPolja tipPolja, boolean daLiJeUTari) {
		this(sloj, redniBrojUSloju, tipPolja);
		this.daLiJeUTari = daLiJeUTari;
	}

	public Polje(Polje polje) {
		this.tipPolja = polje.tipPolja;
		this.daLiJeUTari = polje.daLiJeUTari;
		this.pozicija = new Pozicija(polje.pozicija);
	}

	public TipPolja getTipPolja() {
		return tipPolja;
	}

	public void setTipPolja(TipPolja tipPolja) {
		this.tipPolja = tipPolja;
	}

	public boolean isDaLiJeUTari() {
		return daLiJeUTari;
	}

	public void setDaLiJeUTari(boolean daLiJeUTari) {
		this.daLiJeUTari = daLiJeUTari;
	}
	
	public boolean contains(Point p) {
		Pozicija koordinate = Tabla.mapiranjeIndeksovaNaKoordinate.get(pozicija);
		Point center = new Point(koordinate.getX() + Tabla.POLUPRECNIK_POLJA, koordinate.getY() + Tabla.POLUPRECNIK_POLJA);
		return center.distance(p) <= Tabla.POLUPRECNIK_POLJA;
	}

	public Pozicija getPozicija() {
		return pozicija;
	}

	public void setPozicija(Pozicija pozicija) {
		this.pozicija = pozicija;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Polje) {
			Polje polje = (Polje) obj;
			return pozicija.equals(polje.pozicija) && daLiJeUTari == polje.daLiJeUTari && tipPolja == polje.tipPolja;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (daLiJeUTari ? 1231 : 1237);
		result = prime * result + ((pozicija == null) ? 0 : pozicija.hashCode());
		result = prime * result + ((tipPolja == null) ? 0 : tipPolja.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return tipPolja.name() + "," + daLiJeUTari; 
	}

}

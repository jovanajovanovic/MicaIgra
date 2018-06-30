package com.mica.gui;

import java.awt.Point;

import com.mica.main.Pozicija;
import com.mica.main.TipPolja;

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
			return pozicija.equals(((Polje)obj).pozicija);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return tipPolja.name() + "," + daLiJeUTari; 
	}
}

package com.mica.main;

public class Pozicija {
	private int x;
	private int y;
	
	public Pozicija(int x, int y) {
		this.x = x;
		this.y = y;
	}

	
	public Pozicija(Pozicija pozicija) {
		this.x = pozicija.x;
		this.y = pozicija.y;
	}


	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}



	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pozicija) {
			Pozicija p = (Pozicija) obj;
			
			return p.x == x && p.y == y;
		}
		
		return false;
	}
}

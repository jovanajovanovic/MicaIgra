package com.mica.main;

public class Igrac {
	private String name;
	private TipPolja tipIgraca;
	private int brojNepostavljenihFigura;
	private int brojPreostalihFigura;
	
	private Polje selektovanoPolje = null;
	private Algoritam algoritam = null;
	//private ArrayList<Integer> indeksiPolja;
	//private int indeksSelektovanogPolja;
	
	public static final Algoritam[] algoritmi = { Algoritam.COVEK, Algoritam.RL, Algoritam.MINI_MAX };
	public static final Algoritam[] algoritmiZaTrening = { Algoritam.RL, Algoritam.MINI_MAX };
	
	public Igrac(String name, TipPolja tipIgraca, int brojNepostavljenihFigura, int brojPreostalihFigura) {	
		this.name = name;
		this.tipIgraca = tipIgraca;
		this.brojNepostavljenihFigura = brojNepostavljenihFigura;
		this.brojPreostalihFigura = brojPreostalihFigura;
		//this.indeksiPolja = new ArrayList<Integer>();
		//this.indeksSelektovanogPolja = -1;
	}
	
	public Igrac(String name, TipPolja tipIgraca, int brojNepostavljenihFigura, int brojPreostalihFigura, Polje selektovanoPolje) {	
		this.name = name;
		this.tipIgraca = tipIgraca;
		this.brojNepostavljenihFigura = brojNepostavljenihFigura;
		this.brojPreostalihFigura = brojPreostalihFigura;
		//this.indeksiPolja = new ArrayList<Integer>();
		//this.indeksSelektovanogPolja = -1;
		this.selektovanoPolje = selektovanoPolje;
	}
	
	/*public Igrac(String name, TipPolja tipIgraca, int brojNepostavljenihFigura, int brojPreostalihFigura, ArrayList<Integer> indeksiPolja, int indeksSelektovanogPolja) {	
		this.name = name;
		this.tipIgraca = tipIgraca;
		this.brojNepostavljenihFigura = brojNepostavljenihFigura;
		this.brojPreostalihFigura = brojPreostalihFigura;
		this.indeksiPolja = indeksiPolja;
		this.indeksSelektovanogPolja = indeksSelektovanogPolja;
	}*/
	
	public Igrac(Igrac igrac, Polje[][] polja) {
		this.name = igrac.name;
		this.tipIgraca = igrac.tipIgraca;
		this.brojNepostavljenihFigura = igrac.brojNepostavljenihFigura;
		this.brojPreostalihFigura = igrac.brojPreostalihFigura;
		//this.indeksiPolja = new ArrayList<Integer>(igrac.indeksiPolja);
		//this.indeksSelektovanogPolja = igrac.indeksSelektovanogPolja;
		
		if(igrac.selektovanoPolje == null) this.selektovanoPolje = null;
		else this.selektovanoPolje = polja[igrac.selektovanoPolje.getPozicija().getX()][igrac.selektovanoPolje.getPozicija().getY()];
		
		this.algoritam = igrac.algoritam;
	}
	
	public static Igrac kreirajIgraca(String[] tokeniIgrac, Polje[][] polja) {
		try {
			/*String[] tokeniPolja = tokeniIgrac[4].trim().split("\\.");
			ArrayList<Integer> indeksiPolja = new ArrayList<Integer>();
			for (String token : tokeniPolja) {
				indeksiPolja.add(Integer.parseInt(token.trim()));
			}
			Igrac igrac = new Igrac(tokeniIgrac[0].trim(), TipPolja.valueOf(tokeniIgrac[1].trim()), Integer.parseInt(tokeniIgrac[2].trim()), Integer.parseInt(tokeniIgrac[3].trim()), indeksiPolja, Integer.parseInt(tokeniIgrac[5].trim()));
			*/
			
			String[] tokeniSelektovanoPolje = tokeniIgrac[4].trim().split(",");
			int x = Integer.parseInt(tokeniSelektovanoPolje[0].trim());
			int y = Integer.parseInt(tokeniSelektovanoPolje[1].trim());
			Polje selektovanoPolje;
			if(x == -1 && y == -1) selektovanoPolje = null;
			else selektovanoPolje = polja[x][y];
			
			Igrac igrac = new Igrac(tokeniIgrac[0].trim(), TipPolja.values()[Integer.parseInt(tokeniIgrac[1].trim())], Integer.parseInt(tokeniIgrac[2].trim()), Integer.parseInt(tokeniIgrac[3].trim()), selektovanoPolje);
			return igrac;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public void umanjiBrojNepostavljenihFigura() {
		brojNepostavljenihFigura--;
	}
	
	public void umanjiBrojPreostalihFigura() {
		brojPreostalihFigura--;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TipPolja getTipIgraca() {
		return tipIgraca;
	}

	public void setTipIgraca(TipPolja tipIgraca) {
		this.tipIgraca = tipIgraca;
	}

	public int getBrojNepostavljenihFigura() {
		return brojNepostavljenihFigura;
	}

	public void setBrojNepostavljenihFigura(int brojNepostavljenihFigura) {
		this.brojNepostavljenihFigura = brojNepostavljenihFigura;
	}

	public int getBrojPreostalihFigura() {
		return brojPreostalihFigura;
	}

	public void setBrojPreostalihFigura(int brojPreostalihFigura) {
		this.brojPreostalihFigura = brojPreostalihFigura;
	}
	
	public Polje getSelektovanoPolje() {
		return selektovanoPolje;
	}

	public void setSelektovanoPolje(Polje selektovanoPolje) {
		this.selektovanoPolje = selektovanoPolje;
	}
	
	public Algoritam getAlgoritam() {
		return algoritam;
	}

	public void setAlgoritam(Algoritam algoritam) {
		this.algoritam = algoritam;
	}

	@Override
	public String toString() {
		/*StringBuilder sbPolja = new StringBuilder("");
		for (Integer indeks : indeksiPolja) {
			sbPolja.append(".");
			sbPolja.append(indeks);
		}*/
		
		StringBuilder sb = new StringBuilder("");
		sb.append(name); sb.append(";"); sb.append(tipIgraca.ordinal()); sb.append(";"); sb.append(brojNepostavljenihFigura); 
		sb.append(";"); sb.append(brojPreostalihFigura); 
		sb.append(";"); 
		if(selektovanoPolje != null) {
			sb.append(selektovanoPolje.getPozicija().getX()); 
			sb.append(",");
			sb.append(selektovanoPolje.getPozicija().getY());
		}
		else {
			sb.append(-1); 
			sb.append(",");
			sb.append(-1);
		}
		//sb.append(","); sb.append(sbPolja.toString().substring(1)); sb.append(","); sb.append(indeksSelektovanogPolja);
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brojNepostavljenihFigura;
		result = prime * result + brojPreostalihFigura;
		result = prime * result + ((tipIgraca == null) ? 0 : tipIgraca.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if(obj instanceof Igrac) {
			Igrac igrac = (Igrac) obj;
			
			return tipIgraca == igrac.tipIgraca && brojNepostavljenihFigura == igrac.brojNepostavljenihFigura &&
					brojPreostalihFigura == igrac.brojPreostalihFigura;
		}
		
		return false;
		
	}
	
}

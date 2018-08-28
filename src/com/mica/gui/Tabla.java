package com.mica.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import com.mica.main.Controller;
import com.mica.main.Igrac;
import com.mica.main.Polje;
import com.mica.main.Pozicija;
import com.mica.main.Stanje;

@SuppressWarnings("serial")
public class Tabla extends JPanel implements MouseListener {
	
	public static final int POLUPRECNIK_POLJA = 30;
	
	private final Color BOJA_PRAZNOG_POLJA = Color.ORANGE;
	
	public static final Color BOJA_POZADINE = new Color(0, 102, 0);
	
	private final int[] RAZMACI_IZMEDJU_POLJA = {300, 210, 120};
	private final Point[] GORNJI_LEVI_COSKOVI_KRUGOVA = { new Point(20, 20), new Point(155, 110), new Point(290, 200) };
	private final int[] X_KOORDINATE = {0, 0, 0 , 1, 2, 2, 2, 1};
	private final int[] Y_KOORDINATE = {0, 1, 2 , 2, 2, 1, 0, 0};
	
	public static HashMap<Pozicija, Pozicija> mapiranjeIndeksovaNaKoordinate;
	
	public static final Color[] boje = { Color.ORANGE, Color.BLUE, Color.RED };
	private final Color BOJA_ZA_SELEKTOVANJE = Color.PINK;
	
	private Controller controller;
	
	public Tabla(Controller controller) {
		this.controller = controller;
		
		mapiranjeIndeksovaNaKoordinate = new HashMap<Pozicija, Pozicija>();
		
		Point gornjiLeviCosak;
        int vertikalniRazmakIzmedjuPolja, horizontalniRazmakIzmedjuPolja;
        
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			gornjiLeviCosak = GORNJI_LEVI_COSKOVI_KRUGOVA[i];
			vertikalniRazmakIzmedjuPolja = RAZMACI_IZMEDJU_POLJA[i];
			horizontalniRazmakIzmedjuPolja = vertikalniRazmakIzmedjuPolja + vertikalniRazmakIzmedjuPolja/2;
			
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				mapiranjeIndeksovaNaKoordinate.put(new Pozicija(i,j), new Pozicija(gornjiLeviCosak.x + X_KOORDINATE[j] * horizontalniRazmakIzmedjuPolja, gornjiLeviCosak.y + Y_KOORDINATE[j] * vertikalniRazmakIzmedjuPolja));
			}
			
		}
		
		setBackground(BOJA_POZADINE);
		
		addMouseListener(this);

	}

	@Override
    protected void paintComponent(Graphics g) {
		Stanje trenutnoStanje = controller.getTrenutnoStanje();
		Polje[][] polja = trenutnoStanje.getPolja();
		
		Igrac plaviIgrac = trenutnoStanje.getPlaviIgrac();
		Igrac crveniIgrac = trenutnoStanje.getCrveniIgrac();
		
		Polje selektovanoPolje = trenutnoStanje.getSelektovanoPolje();
		/*if((trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO && plaviIgrac.getAlgoritam() != Algoritam.COVEK) || 
				(trenutnoStanje.getIgracNaPotezu() == TipPolja.CRVENO && crveniIgrac.getAlgoritam() != Algoritam.COVEK)) {
			selektovanoPolje = null;
		}
		else selektovanoPolje = trenutnoStanje.getSelektovanoPolje();*/
		
        super.paintComponent(g);
        
        Polje polje, sledecePolje;
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        
        ArrayList<int[]> lines = new ArrayList<int[]>();
        
        int[] koordinateLinije;
        
        //iscrtavanje krugova i pripremanje koordinata za linije
        for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				polje = polja[i][j];
				sledecePolje = polja[i][(j+1) % Controller.BROJ_POLJA_U_KRUGU];
				
				koordinateLinije = getKoordinateLinije(polje, sledecePolje, j);
				lines.add(koordinateLinije);
			
				if(j%2==1 && i+1 < Controller.BROJ_KRUGOVA) {
					sledecePolje = polja[i+1][j];
					
					koordinateLinije = getKoordinateLinije(polje, sledecePolje, (j+1)%Controller.BROJ_POLJA_U_KRUGU);
					lines.add(koordinateLinije);
				}
			}
			
			g2.setColor(BOJA_PRAZNOG_POLJA);
			
			// prvo iscrtavamo linije
			for (int[] cs : lines) { // cs = coordinates
				g2.drawLine(cs[0], cs[1], cs[2], cs[3]);
			}
			
			Pozicija koordinatePolja;
			
			for (int m = 0; m < Controller.BROJ_KRUGOVA; m++) {
				for (int n = 0; n < Controller.BROJ_POLJA_U_KRUGU; n++) {
					polje = polja[m][n];
					koordinatePolja = mapiranjeIndeksovaNaKoordinate.get(polje.getPozicija());
					
					if(selektovanoPolje != null) {
						if (selektovanoPolje.equals(polje)) {
							g2.setColor(BOJA_ZA_SELEKTOVANJE);
							g2.fillOval(koordinatePolja.getX() - POLUPRECNIK_POLJA/2, koordinatePolja.getY() - POLUPRECNIK_POLJA/2, 3*POLUPRECNIK_POLJA, 3*POLUPRECNIK_POLJA);
						}
					}
					
					g2.setColor(boje[polje.getTipPolja().ordinal()]);
					g2.fillOval(koordinatePolja.getX(), koordinatePolja.getY(), 2*POLUPRECNIK_POLJA, 2*POLUPRECNIK_POLJA);
				}
			}
			
		}
        
        // g.drawImage(court, getX() + 10, getY() + 15, MainWindow.SIZE_SCREEN.width / 2 - 140, MainWindow.SIZE_SCREEN.height - 95, this);

        
       // System.out.println("---------------------------------------");

    }
	
	/*
	private void resetujTabluZaNovuIgru() {
		Polje[][] polja = trenutnoStanje.getPolja();
		
		selektovanoPolje = null;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				polja[i][j].setTipPolja(TipPolja.ZUTO);;
			}
		}
	}*/
	
	private int[] getKoordinateLinije(Polje polje, Polje sledecePolje, int j) {
		Pozicija koordinatePolja = Tabla.mapiranjeIndeksovaNaKoordinate.get(polje.getPozicija());
		Pozicija koordinateSledecegPolja = Tabla.mapiranjeIndeksovaNaKoordinate.get(sledecePolje.getPozicija());
		int[] koordinateLinije = new int[4];
		
		koordinateLinije[0] = koordinatePolja.getX() + Tabla.POLUPRECNIK_POLJA;
		koordinateLinije[1] = koordinatePolja.getY() + Tabla.POLUPRECNIK_POLJA;
		koordinateLinije[2] = koordinateSledecegPolja.getX() + Tabla.POLUPRECNIK_POLJA;
		koordinateLinije[3] = koordinateSledecegPolja.getY() + Tabla.POLUPRECNIK_POLJA;
		
		return koordinateLinije;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		controller.odreagujNaMouseReleased(e);
	}
	
	/*private boolean proveriPostojiLiTaraISetujIliResetujPoljaUTari(Polje polje, boolean setIliReset) {
		int sloj = polje.getPozicija().getSloj();
		int redniBrojUSloju = polje.getPozicija().getRedniBrojUSloju();
		Color color = polje.getBoja();  
		
		boolean ret = false;
		
		if(redniBrojUSloju % 2 == 1) {
			if(polja[0][redniBrojUSloju].getBoja() == color && polja[1][redniBrojUSloju].getBoja() == color && polja[2][redniBrojUSloju].getBoja() == color) {
				polja[0][redniBrojUSloju].setDaLiJeUTari(setIliReset);
				polja[1][redniBrojUSloju].setDaLiJeUTari(setIliReset);
				polja[2][redniBrojUSloju].setDaLiJeUTari(setIliReset);
				
				ret = true;
			}
			
			if(polja[sloj][redniBrojUSloju - 1].getBoja() == color && polja[sloj][redniBrojUSloju].getBoja() == color && polja[sloj][(redniBrojUSloju + 1) % BROJ_POLJA_U_KRUGU].getBoja() == color) {
				polja[sloj][redniBrojUSloju - 1].setDaLiJeUTari(setIliReset); 
				polja[sloj][redniBrojUSloju].setDaLiJeUTari(setIliReset);
				polja[sloj][(redniBrojUSloju + 1) % BROJ_POLJA_U_KRUGU].setDaLiJeUTari(setIliReset);
				
				ret = true;
			}
		}
		else {
			if(polja[sloj][redniBrojUSloju + 1].getBoja() == color && polja[sloj][(redniBrojUSloju + 2) % BROJ_POLJA_U_KRUGU].getBoja() == color) {
				polje.setDaLiJeUTari(setIliReset);
				polja[sloj][redniBrojUSloju + 1].setDaLiJeUTari(setIliReset); 
				polja[sloj][(redniBrojUSloju + 2) % BROJ_POLJA_U_KRUGU].setDaLiJeUTari(setIliReset);
				
				ret = true;
			}
			
			int r1 = redniBrojUSloju - 1;
			if(r1 < 0) r1 = BROJ_POLJA_U_KRUGU + r1;
			int r2 = redniBrojUSloju - 2;
			if(r2 < 0) r2 = BROJ_POLJA_U_KRUGU + r2;
			
			if(polja[sloj][r1].getBoja() == color && polja[sloj][r2].getBoja() == color) {
				polje.setDaLiJeUTari(setIliReset);
				polja[sloj][r1].setDaLiJeUTari(setIliReset);
				polja[sloj][r2].setDaLiJeUTari(setIliReset);
				
				ret = true;
			}
		}
		
		return ret;
	}*/

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void refresh() {
		revalidate();
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

}

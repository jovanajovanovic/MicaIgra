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

import com.mica.main.Algoritam;
import com.mica.main.Controller;
import com.mica.main.Igrac;
import com.mica.main.Polje;
import com.mica.main.Pozicija;
import com.mica.main.RadSaPodacima;
import com.mica.main.Rezultat;
import com.mica.main.Stanje;
import com.mica.main.TipPolja;

@SuppressWarnings("serial")
public class Tabla extends JPanel implements MouseListener {
	
	public static final int POLUPRECNIK_POLJA = 30;
	public static final Color BOJA_PRAZNOG_POLJA = Color.ORANGE;
	private final int[] RAZMACI_IZMEDJU_POLJA = {300, 210, 120};
	private final Point[] GORNJI_LEVI_COSKOVI_KRUGOVA = { new Point(10, 20), new Point(145, 110), new Point(280, 200) };
	private final int[] X_KOORDINATE = {0, 0, 0 , 1, 2, 2, 2, 1};
	private final int[] Y_KOORDINATE = {0, 1, 2 , 2, 2, 1, 0, 0};
	
	public static HashMap<Pozicija, Pozicija> mapiranjeIndeksovaNaKoordinate;
	
	public static final Color[] boje = { Color.ORANGE, Color.BLUE, Color.RED };
	
	private Controller controller;
	
	public Tabla(Controller controller) {
		this.controller = controller;
		
		mapiranjeIndeksovaNaKoordinate = new HashMap<Pozicija, Pozicija>();
		
		Point gornjiLeviCosak;
        int vertikalniRazmakIzmedjuPolja, horizontalniRazmakIzmedjuPolja;
		Polje polje;
        
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			gornjiLeviCosak = GORNJI_LEVI_COSKOVI_KRUGOVA[i];
			vertikalniRazmakIzmedjuPolja = RAZMACI_IZMEDJU_POLJA[i];
			horizontalniRazmakIzmedjuPolja = vertikalniRazmakIzmedjuPolja + vertikalniRazmakIzmedjuPolja/2;
			
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				polje = controller.getTrenutnoStanje().getPolja()[i][j];
				mapiranjeIndeksovaNaKoordinate.put(polje.getPozicija(), new Pozicija(gornjiLeviCosak.x + X_KOORDINATE[j] * horizontalniRazmakIzmedjuPolja, gornjiLeviCosak.y + Y_KOORDINATE[j] * vertikalniRazmakIzmedjuPolja));
			}
			
		}
		
		addMouseListener(this);

	}

	@Override
    protected void paintComponent(Graphics g) {
		Stanje trenutnoStanje = controller.getTrenutnoStanje();
		Polje[][] polja = trenutnoStanje.getPolja();
		
		Igrac plaviIgrac = trenutnoStanje.getPlaviIgrac();
		Igrac crveniIgrac = trenutnoStanje.getCrveniIgrac();
		
		Polje plavoSelektovanoPolje;
		if(plaviIgrac.getAlgoritam() != Algoritam.COVEK) plavoSelektovanoPolje = null;
		else plavoSelektovanoPolje = plaviIgrac.getSelektovanoPolje();
		
		Polje crvenoSelektovanoPolje;
		if(crveniIgrac.getAlgoritam() != Algoritam.COVEK) crvenoSelektovanoPolje = null;
		else crvenoSelektovanoPolje = crveniIgrac.getSelektovanoPolje();
		
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
					
					if(crvenoSelektovanoPolje != null) {
						if (crvenoSelektovanoPolje.equals(polje)) {
							g2.setColor(Color.GREEN);
							g2.fillOval(koordinatePolja.getX() - POLUPRECNIK_POLJA/2, koordinatePolja.getY() - POLUPRECNIK_POLJA/2, 3*POLUPRECNIK_POLJA, 3*POLUPRECNIK_POLJA);
						}
					}
					if(plavoSelektovanoPolje != null) {
						if (plavoSelektovanoPolje.equals(polje)) {
							g2.setColor(Color.PINK);
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
		
		/*if(j == 0 || j == 1) {
			koordinateLinije[0] = koordinatePolja.getX();
			koordinateLinije[1] = koordinatePolja.getY() + Tabla.POLUPRECNIK_POLJA;
			koordinateLinije[2] = koordinateSledecegPolja.getX(); 
			koordinateLinije[3] = koordinateSledecegPolja.getY() - Tabla.POLUPRECNIK_POLJA;
		}
		else if(j == 2 || j == 3) {
			koordinateLinije[0] = koordinatePolja.getX() + Tabla.POLUPRECNIK_POLJA;
			koordinateLinije[1] = koordinatePolja.getY();
			koordinateLinije[2] = koordinateSledecegPolja.getX() - Tabla.POLUPRECNIK_POLJA;
			koordinateLinije[3] = koordinateSledecegPolja.getY();
		}
		else if(j == 4 || j == 5) {
			koordinateLinije[0] = koordinatePolja.getX();
			koordinateLinije[1] = koordinatePolja.getY() - Tabla.POLUPRECNIK_POLJA;
			koordinateLinije[2] = koordinateSledecegPolja.getX(); 
			koordinateLinije[3] = koordinateSledecegPolja.getY() + Tabla.POLUPRECNIK_POLJA;
		}
		else {
			koordinateLinije[0] = koordinatePolja.getX() - Tabla.POLUPRECNIK_POLJA;
			koordinateLinije[1] = koordinatePolja.getY();
			koordinateLinije[2] = koordinateSledecegPolja.getX() + Tabla.POLUPRECNIK_POLJA;
			koordinateLinije[3] = koordinateSledecegPolja.getY();
		}*/
	}

	public Polje getPoljeOnClicked(Point p) {
		Polje[][] polja = controller.getTrenutnoStanje().getPolja();
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].contains(p)) {
					return polja[i][j];
				}
			}
		}
		
		return null;
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
		Stanje trenutnoStanje = controller.getTrenutnoStanje();
		Polje selektovanoPolje;
		
		if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
			if(trenutnoStanje.getPlaviIgrac().getAlgoritam() != Algoritam.COVEK) return;
			selektovanoPolje = trenutnoStanje.getPlaviIgrac().getSelektovanoPolje();
		}
		else {
			if(trenutnoStanje.getCrveniIgrac().getAlgoritam() != Algoritam.COVEK) return;
			selektovanoPolje = trenutnoStanje.getCrveniIgrac().getSelektovanoPolje();
		}
		
		Polje polje = getPoljeOnClicked(e.getPoint()); 
		if(polje != null) {
			//controller.setProveriKrajIgre(false);
			/*if ((controller.getIgracNaPotezu() == IgracType.PLAVI && selektovanoPolje.getBoja() == Color.RED) || (controller.getIgracNaPotezu() == IgracType.CRVENI && selektovanoPolje.getBoja() == Color.BLUE)) {
				return;
			}*/
			
			if(trenutnoStanje.isPojedi()) {
				controller.izvrsiJedenjeFigure(trenutnoStanje, polje, true);
			}
			else {
				if(trenutnoStanje.daLiSuSveFigurePostavljene()) {
					if (selektovanoPolje != null) {
						if ((controller.getTrenutnoStanje().getIgracNaPotezu() == TipPolja.PLAVO && selektovanoPolje.getTipPolja() == TipPolja.PLAVO) || (controller.getTrenutnoStanje().getIgracNaPotezu() == TipPolja.CRVENO && selektovanoPolje.getTipPolja() == TipPolja.CRVENO)) {
							if(controller.daLiJeIspravanPotez(selektovanoPolje.getPozicija(), polje.getPozicija())) {
								if(selektovanoPolje.getTipPolja() != TipPolja.ZUTO) {
									if(polje.getTipPolja() == TipPolja.ZUTO) {
										controller.napraviPotez(trenutnoStanje, polje, selektovanoPolje, true);
										controller.setPotezNapravljen(true);
										//controller.noviPotez();
										//controller.setProveriKrajIgre(true);
										//refresh();
									}
									
								}
							}
						}
						
					} 
					
					if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
						trenutnoStanje.getPlaviIgrac().setSelektovanoPolje(polje);
					}
					else {
						trenutnoStanje.getCrveniIgrac().setSelektovanoPolje(polje);
					}
					
				}
				else {
					if(polje.getTipPolja() == TipPolja.ZUTO) {
						controller.napraviPotez(trenutnoStanje, polje, null, true);
						controller.setPotezNapravljen(true);
						//controller.noviPotez();
					}
					
					/*if(polje.getTipPolja() == TipPolja.ZUTO) {
						polje.setTipPolja(trenutnoStanje.getIgracNaPotezu());
						
						boolean daLijeUTariStaro = polje.isDaLiJeUTari();
						controller.podesiPoljaUTaramaIVanNjih();
						
						if(!daLijeUTariStaro && polje.isDaLiJeUTari())  {
							controller.spremiJedenje(); // kasnije prilikom jedenja bice setovano - proveriKrajIgre = true;
						}
						else {
							controller.noviPotez();
							if (controller.getTrenutnoStanje().daLiSuSveFigurePostavljene()) controller.setProveriKrajIgre(true);
						}
						//proveriKrajIgre = true;
						//refresh();
						
					}*/
					
				}
			}
			
			refresh();
			
			if(controller.isProveriKrajIgre()) {
				controller.setProveriKrajIgre(false);
				
				Rezultat rezultat = trenutnoStanje.krajIgre();
				if(rezultat != null) {
					rezultat.setBrojPoteza(controller.getBrojPoteza());
					RadSaPodacima.upisiKrajnjiRezultatUFajl(rezultat);
					
					if(trenutnoStanje.getPlaviIgrac().getAlgoritam() == Algoritam.RL || trenutnoStanje.getCrveniIgrac().getAlgoritam() == Algoritam.RL) {
						RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(controller.getReinforcementLearning().getqVrednosti());
						RadSaPodacima.sacuvajStanjaAkcijeIBrojIzmenaUFajl(controller.getReinforcementLearning().getBrojMenjanjaQVrednosti());
					}
					
					String poruka = rezultat.getPobednik() + " (" + rezultat.getAlgoritamPobednika()  + ") je pobednik!";
				    controller.dialogPocetni(poruka);
					
					return;
					//controller.setPotezNapravljen(false); // ovo postavljamo 
				}
				
			}
			
			if(controller.isPotezNapravljen() && !trenutnoStanje.isPojedi()) {
				controller.setPotezNapravljen(false);
				
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						//controller.noviPotez();
						Algoritam algoritam;
						if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
							algoritam = trenutnoStanje.getPlaviIgrac().getAlgoritam();
							controller.uvecajBrojPoteza();
						}
						else {
							algoritam = trenutnoStanje.getCrveniIgrac().getAlgoritam();
						}
						
						if (algoritam != Algoritam.COVEK) {
							try { Thread.sleep(2000); } catch (InterruptedException e) {} // na pocetku sacekaj 2 sec
							
							// sad je bot na redu da odigra
							controller.boteOdigrajPotez(algoritam);
						}
						
					}
				});
				t.start();
				
			}
			
		}
		else {
			if(trenutnoStanje.getIgracNaPotezu() == TipPolja.PLAVO) {
				trenutnoStanje.getPlaviIgrac().setSelektovanoPolje(null);
			}
			else {
				trenutnoStanje.getCrveniIgrac().setSelektovanoPolje(null);
			}
			
		}
		
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

package com.jovo.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jovo.main.Controller;
import com.jovo.main.Pozicija;
import com.jovo.main.TipPolja;

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
	
	private Polje selektovanoPolje = null;
	
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
		Polje[][] polja = controller.getTrenutnoStanje().getPolja();
		
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
							g2.setColor(Color.GREEN);
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
		
		Polje polje = getPoljeOnClicked(e.getPoint()); 
		if(polje != null) {
			boolean proveriKrajIgre = false;
			/*if ((controller.getIgracNaPotezu() == IgracType.PLAVI && selektovanoPolje.getBoja() == Color.RED) || (controller.getIgracNaPotezu() == IgracType.CRVENI && selektovanoPolje.getBoja() == Color.BLUE)) {
				return;
			}*/
			
			if(controller.isPojediPlavog() || controller.isPojediCrvenog()) {
				boolean uslov = true;
				boolean dlsspput;
				
				if(controller.isPojediPlavog()) {
					if(polje.getTipPolja() == TipPolja.PLAVO) {
						uslov = true;
						dlsspput = daLiSuSvaProtivnickaPoljaUTari(TipPolja.PLAVO);
						if(!dlsspput) {
							if (polje.isDaLiJeUTari()) {
								uslov = false;
							}
						}
						
						if (uslov) {
							polje.setTipPolja(TipPolja.ZUTO);
							controller.zavrsiJedenje();
							if(dlsspput) podesiPoljaUTaramaIVanNjih();
							proveriKrajIgre = true;
						}
						
					}
					//return;
				}
				
				if (controller.isPojediCrvenog()) {
					if(polje.getTipPolja() == TipPolja.CRVENO) {
						uslov = true;
						dlsspput = daLiSuSvaProtivnickaPoljaUTari(TipPolja.CRVENO);
						if(!dlsspput) {
							if (polje.isDaLiJeUTari()) {
								uslov = false;
							}
						}
						
						if (uslov) {
							polje.setTipPolja(TipPolja.ZUTO);
							controller.zavrsiJedenje();
							if(dlsspput) podesiPoljaUTaramaIVanNjih();
							proveriKrajIgre = true;
						}
						
						//refresh();
						
					}
					//return;
				}
			}
			else {
				if(controller.daLiSuSveFigurePostavljene()) {
					if (selektovanoPolje != null) {
						if ((controller.getTrenutnoStanje().getIgracNaPotezu() == TipPolja.PLAVO && selektovanoPolje.getTipPolja() == TipPolja.PLAVO) || (controller.getTrenutnoStanje().getIgracNaPotezu() == TipPolja.CRVENO && selektovanoPolje.getTipPolja() == TipPolja.CRVENO)) {
							if(controller.daLiJeIspravanPotez(selektovanoPolje.getPozicija(), polje.getPozicija())) {
								if(selektovanoPolje.getTipPolja() != TipPolja.ZUTO) {
									if(polje.getTipPolja() == TipPolja.ZUTO) {
										polje.setTipPolja(selektovanoPolje.getTipPolja());
										selektovanoPolje.setTipPolja(TipPolja.ZUTO);
										boolean daLijeUTariStaro = polje.isDaLiJeUTari();
										podesiPoljaUTaramaIVanNjih();
										if(!daLijeUTariStaro && polje.isDaLiJeUTari())  {
											controller.spremiJedenje();
										}
										else {
											controller.noviPotez();
										}
										
										proveriKrajIgre = true;
										//refresh();
									}
									
								}
							}
						}
						
					} 
					
					selektovanoPolje = polje;
					
				}
				else {
					if(polje.getTipPolja() == TipPolja.ZUTO) {
						if (controller.getTrenutnoStanje().getIgracNaPotezu() == TipPolja.PLAVO) {
							polje.setTipPolja(TipPolja.PLAVO);
						}
						else if(controller.getTrenutnoStanje().getIgracNaPotezu() == TipPolja.CRVENO) {
							polje.setTipPolja(TipPolja.CRVENO);
						}
						
						boolean daLijeUTariStaro = polje.isDaLiJeUTari();
						podesiPoljaUTaramaIVanNjih();
						
						if(!daLijeUTariStaro && polje.isDaLiJeUTari())  {
							controller.spremiJedenje(); // kasnije prilikom jedenja bice setovano - proveriKrajIgre = true;
						}
						else {
							controller.noviPotez();
							if (controller.daLiSuSveFigurePostavljene()) proveriKrajIgre = true;
						}
						//proveriKrajIgre = true;
						//refresh();
						
					}
					
				}
			}
			
			refresh();
			
			if(proveriKrajIgre) {
				if(controller.krajIgre()) {
					int res = JOptionPane.showConfirmDialog(null, controller.getPobednik() + " je pobednik! Želite li da igrate novu igru?", "Kraj igre", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if(res == JOptionPane.YES_OPTION) {
						//resetujTabluZaNovuIgru();
						controller.resetujSveZaNovuIgru();
						refresh();
					}
				}
			}
			
		}
		else {
			selektovanoPolje = null;
		}
		
	}
	
	private void podesiPoljaUTaramaIVanNjih() {
		Polje[][] polja = controller.getTrenutnoStanje().getPolja();
		
		TipPolja tipPolja;
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j+=2) {
				tipPolja = polja[i][j].getTipPolja();
				
				if(polja[i][j+1].getTipPolja() == tipPolja && polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
					polja[i][j].setDaLiJeUTari(true);
					polja[i][j+1].setDaLiJeUTari(true);
					polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].setDaLiJeUTari(true);
				}
				else {
					if(j == 0) polja[i][j].setDaLiJeUTari(false);
					polja[i][j+1].setDaLiJeUTari(false);
					if(j != 6) polja[i][(j+2)%Controller.BROJ_POLJA_U_KRUGU].setDaLiJeUTari(false);
				}
			}
		}
		
		for (int k = 1; k < Controller.BROJ_POLJA_U_KRUGU; k+=2) {
			tipPolja = polja[0][k].getTipPolja();
			
			if(polja[1][k].getTipPolja() == tipPolja && polja[2][k].getTipPolja() == tipPolja && tipPolja != TipPolja.ZUTO) {
				polja[0][k].setDaLiJeUTari(true);
				polja[1][k].setDaLiJeUTari(true);
				polja[2][k].setDaLiJeUTari(true);
			}
			/*else {
				polja[0][k].setDaLiJeUTari(false);
				polja[1][k].setDaLiJeUTari(false);
				polja[2][k].setDaLiJeUTari(false);
			}*/
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
	
	public boolean daLiSuSvaProtivnickaPoljaUTari(TipPolja tipPolja) {
		Polje[][] polja = controller.getTrenutnoStanje().getPolja();
		
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if(polja[i][j].getTipPolja() != tipPolja) continue;
				
				if(!polja[i][j].isDaLiJeUTari()) {
					return false;
				}
			}
		}
		
		return true;
	}

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

	public Polje getSelektovanoPolje() {
		return selektovanoPolje;
	}

	public void setSelektovanoPolje(Polje selektovanoPolje) {
		this.selektovanoPolje = selektovanoPolje;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

}

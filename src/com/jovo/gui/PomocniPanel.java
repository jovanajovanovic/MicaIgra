package com.jovo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jovo.main.Controller;
import com.jovo.main.Igrac;
import com.jovo.main.Stanje;
import com.jovo.main.TipPolja;

@SuppressWarnings("serial")
public class PomocniPanel extends JPanel {
	private JLabel naPotezu;
	
	private JLabel brojPlavihNepostavljenihFigura;
	private JLabel brojCrvenihNepostavljenihFigura;
	
	private JLabel brojPlavihPreostalihFigura;
	private JLabel brojCrvenihPreostalihFigura;
	
	private JLabel pojediLabel;
	
	private Controller controller;
	
	
	public PomocniPanel() {
		setLayout(new BorderLayout());
		
		this.naPotezu = new JLabel();
		this.brojCrvenihNepostavljenihFigura = new JLabel();
		this.brojCrvenihNepostavljenihFigura.setForeground(Color.RED);
		this.brojPlavihNepostavljenihFigura = new JLabel();
		this.brojPlavihNepostavljenihFigura.setForeground(Color.BLUE);
		
		this.brojCrvenihPreostalihFigura = new JLabel();
		this.brojCrvenihPreostalihFigura.setForeground(Color.RED);
		this.brojPlavihPreostalihFigura = new JLabel();
		this.brojPlavihPreostalihFigura.setForeground(Color.BLUE);
		
		JPanel northPanel = new JPanel(new GridLayout(12, 1));
		
		northPanel.add(new JLabel("    "));
		northPanel.add(new JLabel("    "));
		
		northPanel.add(naPotezu);
		northPanel.add(new JLabel("    "));
		
		
		northPanel.add(brojPlavihNepostavljenihFigura);
		northPanel.add(brojPlavihPreostalihFigura);
		
		northPanel.add(new JLabel("    "));
		
		northPanel.add(brojCrvenihNepostavljenihFigura);
		northPanel.add(brojCrvenihPreostalihFigura);
		
		northPanel.add(new JLabel("    "));
		northPanel.add(new JLabel("    "));
		
		pojediLabel = new JLabel();
		pojediLabel.setVisible(false);
		northPanel.add(pojediLabel);
		
		add(northPanel, BorderLayout.NORTH);
	}

	
	
	public void setNaPotezu(TipPolja igracType) {
		String str = igracType.name();
		naPotezu.setText("NA POTEZU:   " + str.substring(0, str.length() - 1) + "I");
		naPotezu.setForeground(Tabla.boje[igracType.ordinal()]);
	}

	public void setBrojPlavihNepostavljenihFigura(int broj) {
		brojPlavihNepostavljenihFigura.setText("BROJ NEPOSTAVLJENIH FIGURA: " + broj + "     ");
	}

	public void setBrojCrvenihNepostavljenihFigura(int broj) {
		brojCrvenihNepostavljenihFigura.setText("BROJ NEPOSTAVLJENIH FIGURA: " + broj + "     ");
	}

	public void setBrojPlavihPreostalihFigura(int broj) {
		brojPlavihPreostalihFigura.setText("BROJ PREOSTALIH FIGURA: " + broj + "     ");
	}

	public void setBrojCrvenihPreostalihFigura(int broj) {
		brojCrvenihPreostalihFigura.setText("BROJ PREOSTALIH FIGURA: " + broj + "    ");
	}

	public JLabel getPojediLabel() {
		return pojediLabel;
	}
	
	public void setPojediLabel(String figuraZaJedenje) {
		pojediLabel.setText("Pojedite jednu " + figuraZaJedenje + " figuru!");
	}
	
	public Controller getController() {
		return controller;
	}


	public void setController(Controller controller) {
		this.controller = controller;
	}



	public void resetujSveZaNovuIgru(Stanje stanje) {
		Igrac crveniIgrac = stanje.getCrveniIgrac();
		Igrac plaviIgrac = stanje.getPlaviIgrac();
		
		 setNaPotezu(stanje.getIgracNaPotezu());
		 setBrojPlavihNepostavljenihFigura(plaviIgrac.getBrojNepostavljenihFigura());
		 setBrojCrvenihNepostavljenihFigura(crveniIgrac.getBrojNepostavljenihFigura());
		 setBrojPlavihPreostalihFigura(plaviIgrac.getBrojPreostalihFigura());
		 setBrojCrvenihPreostalihFigura(crveniIgrac.getBrojPreostalihFigura());
		
	}
	
}

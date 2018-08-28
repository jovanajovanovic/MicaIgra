package com.mica.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mica.main.Controller;
import com.mica.main.Igrac;
import com.mica.main.Stanje;
import com.mica.main.TipPolja;

@SuppressWarnings("serial")
public class PomocniPanel extends JPanel {
	private final int DEBLJINA_BORDERA_PANELA = 3;

	private JLabel naPotezu;
	
	private JLabel brojPlavihNepostavljenihFigura;
	private JLabel brojCrvenihNepostavljenihFigura;
	
	private JLabel brojPlavihPreostalihFigura;
	private JLabel brojCrvenihPreostalihFigura;
	
	private JLabel pojediLabel;
	private JPanel pojediLabelPanel;
	
	private JLabel brojPreostalihPartijaLabel;
	
	private JPanel panelZaSekunde;
	private JPanel panelZaEpsilon;
	private JPanel naPotezuPanel;
	private JPanel brojPlavihNepostavljenihFiguraPanel;
	private JPanel brojPlavihPreostalihFiguraPanel;
	private JPanel brojCrvenihNepostavljenihFiguraPanel;
	private JPanel brojCrvenihPreostalihFiguraPanel;
	private JPanel brojPreostalihPartijaLabelPanel;
	
	private JCheckBox checkBoxZaPrikazTabele;
	
	private JPanel checkBoxZaPrikazTabelePanel;
	
	private JSpinner spinnerSekunde;
	private JSpinner spinnerEpsilon;
	
	private Controller controller;
	
	
	public PomocniPanel(Controller controller) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		constraints.insets = new Insets(3,3,3,3);
		
		this.controller = controller;
		
		this.naPotezu = new JLabel();
		this.brojCrvenihNepostavljenihFigura = new JLabel();
		this.brojCrvenihNepostavljenihFigura.setForeground(Color.RED);
		this.brojPlavihNepostavljenihFigura = new JLabel();
		this.brojPlavihNepostavljenihFigura.setForeground(Color.BLUE);
		
		this.brojCrvenihPreostalihFigura = new JLabel();
		this.brojCrvenihPreostalihFigura.setForeground(Color.RED);
		this.brojPlavihPreostalihFigura = new JLabel();
		this.brojPlavihPreostalihFigura.setForeground(Color.BLUE);
		
		//JPanel northPanel = new JPanel(new GridLayout(19, 1));
		
		//northPanel.add(new JLabel("    "));
		//northPanel.add(new JLabel("    "));
		
		spinnerSekunde = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
		
		panelZaSekunde = napraviPanelZaSekunde(spinnerSekunde, false);
		panelZaSekunde.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		panelZaSekunde.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(panelZaSekunde, constraints);
		add(panelZaSekunde);
		
		//northPanel.add(new JLabel("    "));
		
	    spinnerEpsilon = new JSpinner(new SpinnerNumberModel(0.10, 0.00, 1.00, 0.01));
	    spinnerEpsilon.getEditor().setPreferredSize(new Dimension(25, 15));
		
		panelZaEpsilon = napraviPanelZaEpsilon(spinnerEpsilon, false, true);
		panelZaEpsilon.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		panelZaEpsilon.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 1;
		gridBagLayout.setConstraints(panelZaEpsilon, constraints);
		add(panelZaEpsilon);
		
		//northPanel.add(new JLabel("    "));
		
		naPotezuPanel = new JPanel();
		naPotezuPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		naPotezuPanel.add(naPotezu);
		naPotezuPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 2;
		gridBagLayout.setConstraints(naPotezuPanel, constraints);
		add(naPotezuPanel);
		//northPanel.add(naPotezuPanel);
		//northPanel.add(new JLabel("    "));
		
		brojPlavihNepostavljenihFiguraPanel = new JPanel();
		brojPlavihNepostavljenihFiguraPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		brojPlavihNepostavljenihFiguraPanel.add(brojPlavihNepostavljenihFigura);
		brojPlavihNepostavljenihFiguraPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 3;
		gridBagLayout.setConstraints(brojPlavihNepostavljenihFiguraPanel, constraints);
		add(brojPlavihNepostavljenihFiguraPanel);
		//northPanel.add(brojPlavihNepostavljenihFigura);
		
		brojPlavihPreostalihFiguraPanel = new JPanel();
		brojPlavihPreostalihFiguraPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		brojPlavihPreostalihFiguraPanel.add(brojPlavihPreostalihFigura);
		brojPlavihPreostalihFiguraPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 4;
		gridBagLayout.setConstraints(brojPlavihPreostalihFiguraPanel, constraints);
		add(brojPlavihPreostalihFiguraPanel);
		//northPanel.add(brojPlavihPreostalihFigura);
		
		//northPanel.add(new JLabel("    "));
		
		brojCrvenihNepostavljenihFiguraPanel = new JPanel();
		brojCrvenihNepostavljenihFiguraPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		brojCrvenihNepostavljenihFiguraPanel.add(brojCrvenihNepostavljenihFigura);
		brojCrvenihNepostavljenihFiguraPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 5;
		gridBagLayout.setConstraints(brojCrvenihNepostavljenihFiguraPanel, constraints);
		add(brojCrvenihNepostavljenihFiguraPanel);
		//northPanel.add(brojCrvenihNepostavljenihFigura);
		
		brojCrvenihPreostalihFiguraPanel = new JPanel();
		brojCrvenihPreostalihFiguraPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		brojCrvenihPreostalihFiguraPanel.add(brojCrvenihPreostalihFigura);
		brojCrvenihPreostalihFiguraPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 6;
		gridBagLayout.setConstraints(brojCrvenihPreostalihFiguraPanel, constraints);
		add(brojCrvenihPreostalihFiguraPanel);
		//northPanel.add(brojCrvenihPreostalihFigura);
		
		//northPanel.add(new JLabel("    "));
		//northPanel.add(new JLabel("    "));
		
		pojediLabel = new JLabel();
		pojediLabelPanel = napraviPanelZaJedenje(pojediLabel);
		pojediLabelPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		pojediLabelPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 7;
		gridBagLayout.setConstraints(pojediLabelPanel, constraints);
		add(pojediLabelPanel);
		//northPanel.add(pojediLabel);
		
		//northPanel.add(new JLabel("    "));
		//northPanel.add(new JLabel("    "));
		
		brojPreostalihPartijaLabelPanel = new JPanel();
		brojPreostalihPartijaLabelPanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		brojPreostalihPartijaLabel = new JLabel();
		brojPreostalihPartijaLabelPanel.add(brojPreostalihPartijaLabel);
		brojPreostalihPartijaLabelPanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 8;
		gridBagLayout.setConstraints(brojPreostalihPartijaLabelPanel, constraints);
		add(brojPreostalihPartijaLabelPanel);
		
		checkBoxZaPrikazTabelePanel = new JPanel();
		checkBoxZaPrikazTabelePanel.setBorder(BorderFactory.createLineBorder(Tabla.BOJA_POZADINE, DEBLJINA_BORDERA_PANELA));
		checkBoxZaPrikazTabele = new JCheckBox("Prikazuj dialog sa tabelom za akcije i Q-vrednosti", controller.isPrikaziDialogTabelaAkcijaIQVrednosti());
		checkBoxZaPrikazTabele.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				controller.setPrikaziDialogTabelaAkcijaIQVrednosti(checkBoxZaPrikazTabele.isSelected(), false);
				
			}
		});
		checkBoxZaPrikazTabelePanel.add(checkBoxZaPrikazTabele);
		checkBoxZaPrikazTabelePanel.setVisible(false);
		constraints.gridx = 0;
		constraints.gridy = 9;
		gridBagLayout.setConstraints(checkBoxZaPrikazTabelePanel, constraints);
		add(checkBoxZaPrikazTabelePanel);
	}

	
	
	public JPanel napraviPanelZaSekunde(JSpinner spinnerSekunde, boolean azurirajVrednostUPomocnomPanelu) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		JPanel panelZaSekunde = new JPanel(gridBagLayout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		//constraints.fill = GridBagConstraints.HORIZONTAL; 
		
		String strOdspavaj = "Odspavaj ";
		if (!azurirajVrednostUPomocnomPanelu) strOdspavaj = "  " + strOdspavaj;
		JLabel labelOdspavaj = new JLabel(strOdspavaj);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(labelOdspavaj, constraints);
		panelZaSekunde.add(labelOdspavaj);
		

		spinnerSekunde.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				controller.setBrojSekundiZaSpavanje((Integer) spinnerSekunde.getValue(), azurirajVrednostUPomocnomPanelu);
				
			}
		});
		//constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(spinnerSekunde, constraints);
		panelZaSekunde.add(spinnerSekunde);
		
		JLabel labelOstatakTeksta = new JLabel(" sec između poteza i pre jedenja  ");
		//constraints.weightx = 0.5;
		constraints.gridx = 2;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(labelOstatakTeksta, constraints);
		panelZaSekunde.add(labelOstatakTeksta);
		
		return panelZaSekunde;
	}
	
	public JPanel napraviPanelZaEpsilon(JSpinner spinnerEpsilon, boolean azurirajVrednostUPomocnomPanelu, boolean prikaziUDvaReda) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		JPanel panelZaEpsilon = new JPanel(gridBagLayout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		//constraints.fill = GridBagConstraints.HORIZONTAL; 
		
		String strEpsilon = "Epsilon (random akcija ili po politici?): ";
		if(!azurirajVrednostUPomocnomPanelu) strEpsilon = "  " + strEpsilon;
		JLabel labelEpsilon = new JLabel(strEpsilon);
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(labelEpsilon, constraints);
		panelZaEpsilon.add(labelEpsilon);
		

		spinnerEpsilon.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				controller.podesiEpsilon((Double) spinnerEpsilon.getValue(), azurirajVrednostUPomocnomPanelu);
				
			}
		});
		//constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(spinnerEpsilon, constraints);
		panelZaEpsilon.add(spinnerEpsilon);
		
		JLabel labelOstatakTeksta = new JLabel(" (samo za reinforcement learning)");
		//constraints.weightx = 0.5;
		if(prikaziUDvaReda) {
			JLabel razmakLabel = new JLabel("  ");
			constraints.gridx = 2;
			constraints.gridy = 0;
			gridBagLayout.setConstraints(razmakLabel, constraints);
			panelZaEpsilon.add(razmakLabel);
			
			constraints.gridx = 0;
			constraints.gridy = 1;
		}
		else {
			constraints.gridx = 2;
			constraints.gridy = 0;
		}
		gridBagLayout.setConstraints(labelOstatakTeksta, constraints);
		panelZaEpsilon.add(labelOstatakTeksta);
		
		return panelZaEpsilon;
	}

	public JPanel napraviPanelZaJedenje(JLabel pojediLabel) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		JPanel panelZaJedenje = new JPanel(gridBagLayout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		//constraints.fill = GridBagConstraints.HORIZONTAL; 
		
		JLabel label1 = new JLabel("  Pojedite jednu ");
		JLabel label2 = new JLabel(" figuru!  ");
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(label1, constraints);
		panelZaJedenje.add(label1);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(pojediLabel, constraints);
		panelZaJedenje.add(pojediLabel);
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(label2, constraints);
		panelZaJedenje.add(label2);
		
		return panelZaJedenje;
	}
	
	public void setNaPotezu(TipPolja igracType) {
		String str = igracType.name();
		naPotezu.setText("  NA POTEZU:   " + str.substring(0, str.length() - 1) + "I  ");
		naPotezu.setForeground(Tabla.boje[igracType.ordinal()]);
	}

	public void setBrojPlavihNepostavljenihFigura(int broj) {
		brojPlavihNepostavljenihFigura.setText("  BROJ NEPOSTAVLJENIH FIGURA: " + broj + "  ");
	}

	public void setBrojCrvenihNepostavljenihFigura(int broj) {
		brojCrvenihNepostavljenihFigura.setText("  BROJ NEPOSTAVLJENIH FIGURA: " + broj + "  ");
	}

	public void setBrojPlavihPreostalihFigura(int broj) {
		brojPlavihPreostalihFigura.setText("  BROJ PREOSTALIH FIGURA: " + broj + "  ");
	}

	public void setBrojCrvenihPreostalihFigura(int broj) {
		brojCrvenihPreostalihFigura.setText("  BROJ PREOSTALIH FIGURA: " + broj + "  ");
	}

	public JLabel getPojediLabel() {
		return pojediLabel;
	}
	
	public void setPojediLabel(String figuraZaJedenje, TipPolja tipFigureKojuTrebaPojesti) {
		pojediLabel.setText(figuraZaJedenje);
		pojediLabel.setForeground(Tabla.boje[tipFigureKojuTrebaPojesti.ordinal()]);
	}
	
	public JLabel getBrojPreostalihPartijaLabel() {
		return brojPreostalihPartijaLabel;
	}

	public void setBrojPreostalihPartijaLabel(int brojPartija) {
		brojPreostalihPartijaLabel.setText("  Broj preostalih partija (trening): " + brojPartija);
	}
	
	public JPanel getNaPotezuPanel() {
		return naPotezuPanel;
	}

	public void setNaPotezuPanel(JPanel naPotezuPanel) {
		this.naPotezuPanel = naPotezuPanel;
	}

	public JPanel getBrojPlavihNepostavljenihFiguraPanel() {
		return brojPlavihNepostavljenihFiguraPanel;
	}

	public void setBrojPlavihNepostavljenihFiguraPanel(JPanel brojPlavihNepostavljenihFiguraPanel) {
		this.brojPlavihNepostavljenihFiguraPanel = brojPlavihNepostavljenihFiguraPanel;
	}

	public JPanel getBrojPlavihPreostalihFiguraPanel() {
		return brojPlavihPreostalihFiguraPanel;
	}

	public void setBrojPlavihPreostalihFiguraPanel(JPanel brojPlavihPreostalihFiguraPanel) {
		this.brojPlavihPreostalihFiguraPanel = brojPlavihPreostalihFiguraPanel;
	}

	public JPanel getBrojCrvenihNepostavljenihFiguraPanel() {
		return brojCrvenihNepostavljenihFiguraPanel;
	}

	public void setBrojCrvenihNepostavljenihFiguraPanel(JPanel brojCrvenihNepostavljenihFiguraPanel) {
		this.brojCrvenihNepostavljenihFiguraPanel = brojCrvenihNepostavljenihFiguraPanel;
	}

	public JPanel getBrojCrvenihPreostalihFiguraPanel() {
		return brojCrvenihPreostalihFiguraPanel;
	}

	public void setBrojCrvenihPreostalihFiguraPanel(JPanel brojCrvenihPreostalihFiguraPanel) {
		this.brojCrvenihPreostalihFiguraPanel = brojCrvenihPreostalihFiguraPanel;
	}

	public JPanel getBrojPreostalihPartijaLabelPanel() {
		return brojPreostalihPartijaLabelPanel;
	}

	public void setBrojPreostalihPartijaLabelPanel(JPanel brojPreostalihPartijaLabelPanel) {
		this.brojPreostalihPartijaLabelPanel = brojPreostalihPartijaLabelPanel;
	}
	
	public JPanel getPojediLabelPanel() {
		return pojediLabelPanel;
	}

	public void setPojediLabelPanel(JPanel pojediLabelPanel) {
		this.pojediLabelPanel = pojediLabelPanel;
	}

	public JPanel getPanelZaSekunde() {
		return panelZaSekunde;
	}

	public void setPanelZaSekunde(JPanel panelZaSekunde) {
		this.panelZaSekunde = panelZaSekunde;
	}
	
	public JPanel getPanelZaEpsilon() {
		return panelZaEpsilon;
	}
	
	public void setPanelZaEpsilon(JPanel panelZaEpsilon) {
		this.panelZaEpsilon = panelZaEpsilon;
	}
	
	public void setBrojSekundi(int brojSekundi) {
		spinnerSekunde.setValue(brojSekundi);
	}
	
	public void setEpsilon(double epsilon) {
		spinnerEpsilon.setValue(epsilon);
	}
	
	public JCheckBox getCheckBoxZaPrikazTabele() {
		return checkBoxZaPrikazTabele;
	}

	public void setCheckBoxZaPrikazTabele(JCheckBox checkBoxZaPrikazTabele) {
		this.checkBoxZaPrikazTabele = checkBoxZaPrikazTabele;
	}

	public JPanel getCheckBoxZaPrikazTabelePanel() {
		return checkBoxZaPrikazTabelePanel;
	}

	public void setCheckBoxZaPrikazTabelePanel(JPanel checkBoxZaPrikazTabelePanel) {
		this.checkBoxZaPrikazTabelePanel = checkBoxZaPrikazTabelePanel;
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

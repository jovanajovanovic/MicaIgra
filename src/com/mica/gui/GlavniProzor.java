package com.mica.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.mica.algorithms.Algoritam;
import com.mica.main.Controller;
import com.mica.main.Igrac;
import com.mica.main.PoljeAkcija;
import com.mica.main.RadSaPodacima;
import com.mica.main.Stanje;
import com.mica.main.TipPolja;


@SuppressWarnings("serial")
public class GlavniProzor extends JFrame {
	
	private static final Color LIGHT_BLUE =  new Color(0,182,155);
	private Tabla tabla;
	private PomocniPanel pomocniPanel;
	
	private Controller controller;
	
	private static Dimension SIZE_SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	
	public GlavniProzor(Controller controller) {
		super("Mica");
		
		this.controller = controller;
		
	    setLayout(new BorderLayout());
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    
	    JSplitPane mainPanel = napraviMainPanel();
	    
	    tabla = new Tabla(controller);
	    mainPanel.setLeftComponent(tabla);
	    
	    pomocniPanel = new PomocniPanel(controller);
	    mainPanel.setRightComponent(pomocniPanel);
	    
	    /*Stanje trenutnoStanje = controller.getTrenutnoStanje();
	    pomocniPanel.setNaPotezu(trenutnoStanje.getIgracNaPotezu());
	    pomocniPanel.setBrojPlavihNepostavljenihFigura(trenutnoStanje.getPlaviIgrac().getBrojNepostavljenihFigura());
	    pomocniPanel.setBrojCrvenihNepostavljenihFigura(trenutnoStanje.getCrveniIgrac().getBrojNepostavljenihFigura());
	    pomocniPanel.setBrojPlavihPreostalihFigura(trenutnoStanje.getPlaviIgrac().getBrojPreostalihFigura());
	    pomocniPanel.setBrojCrvenihPreostalihFigura(trenutnoStanje.getCrveniIgrac().getBrojPreostalihFigura());
	    */
	    
		//getContentPane().add(tabla, BorderLayout.CENTER);
		//getContentPane().add(pomocniPanel, BorderLayout.EAST);
		
	    getContentPane().add(mainPanel, BorderLayout.CENTER);
	    
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				izlaz();
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//tabla.refresh();
	}

	public Tabla getTabla() {
		return tabla;
	}

	public void setTabla(Tabla tabla) {
		this.tabla = tabla;
	}

	public PomocniPanel getPomocniPanel() {
		return pomocniPanel;
	}

	public void setPomocniPanel(PomocniPanel pomocniPanel) {
		this.pomocniPanel = pomocniPanel;
	}
	
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	private JSplitPane napraviMainPanel() {
		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		jSplitPane.setDividerLocation(SIZE_SCREEN.width * 3 / 4 - 20);
		
		return jSplitPane;
	}
	
	public void dialogPocetni(String poruka) {
		String naslov = "Mica";
		
		JButton btnNovaIgra = new JButton("Nova Igra");
		btnNovaIgra.setBackground(Color.GREEN);
		
		JButton btnTrening = new JButton("Trening");
		btnTrening.setBackground(LIGHT_BLUE);
		
		JButton btnIzlaz = new JButton("Izlaz");
		btnIzlaz.setBackground(Color.RED);
		
		JDialog dialog = new JDialog(this, naslov, true);
		
		btnNovaIgra.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sakrijPanelZaSekunde();
				sakrijPanelZaEpsilon();
				sakrijCheckBoxZaPrikazTabelePanel();
				dialog.setVisible(false);
				controller.zapocniNovuIgru();
			}
		});
		
		btnTrening.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sakrijPanelZaSekunde();
				sakrijPanelZaEpsilon();
				sakrijCheckBoxZaPrikazTabelePanel();
				dialog.setVisible(false);
				controller.zapocniTrening();
			}
		});
		
		btnIzlaz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				izlaz();
			}
		});
		
		dialog.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				izlaz();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		dialog.setLayout(new BorderLayout());
		dialog.setLocation(SIZE_SCREEN.width/3, SIZE_SCREEN.height/3);
		
		int height;
		
		if(poruka != null) {
			JPanel northPanel = new JPanel(new BorderLayout());
			northPanel.add(new JLabel("     "), BorderLayout.NORTH);
			JLabel porukaLabel = new JLabel(poruka, JLabel.CENTER);
			northPanel.add(porukaLabel, BorderLayout.SOUTH);
			dialog.getContentPane().add(northPanel, BorderLayout.NORTH);
			height = 150;
		}
		else {
			height = 125;
		}
		
		JPanel dugmadPanel = new JPanel(new FlowLayout());
		dugmadPanel.add(btnNovaIgra);
		dugmadPanel.add(btnTrening);
		dugmadPanel.add(btnIzlaz);
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(dugmadPanel, BorderLayout.NORTH);
		southPanel.add(new JLabel("    "), BorderLayout.SOUTH);
		dialog.getContentPane().add(southPanel, BorderLayout.SOUTH);
		dialog.setSize(400, height);
		dialog.setVisible(true);
		
	}
	
	private void izlaz() {
		if(controller.isKoristioReinforcmentLearning()) {
			boolean predjenaGranica = controller.getReinforcementLearning().daLiJePredjenaGranicaZaPrelazNaAproksimaciju();
				
			int res = dialogZaCuvanjeQVrednostiITezina(predjenaGranica);
			if (res == 2 || res == -1) {
				System.out.println("Nema cuvanja");
				return;
			}
			
			if (res == 0 || res == 1) {
				if(res == 0) {
					RadSaPodacima.sacuvajStanjaAkcijeIQVrednostiUFajl(controller.getReinforcementLearning().getqVrednosti());
					
					if(predjenaGranica) {
						RadSaPodacima.sacuvajTezineUFajl(controller.getReinforcementLearning().getTezine());
					}
				}

			}
		}
				
		System.exit(0);
	}
	
	public int[] dialogZaNovuIgru() {
		GridBagLayout gridBagLayoutMainPanel = new GridBagLayout();
		JPanel mainPanel = new JPanel(gridBagLayoutMainPanel);
		
		GridBagLayout gridBagLayoutPanelZaComboBoxPlavog = new GridBagLayout();
		JPanel panelZaComboBoxPlavog = new JPanel(gridBagLayoutPanelZaComboBoxPlavog);
		
		GridBagLayout gridBagLayoutPanelZaComboBoxCrvenog = new GridBagLayout();
		JPanel panelZaComboBoxCrvenog = new JPanel(gridBagLayoutPanelZaComboBoxCrvenog);
		
		GridBagLayout gridBagLayoutCheckBoxZaPrikazTabelePanel = new GridBagLayout();
		JPanel checkBoxZaPrikazTabelePanel = new JPanel(gridBagLayoutCheckBoxZaPrikazTabelePanel);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		
		JLabel plaviIgracLabel = new JLabel("Plavi igrač:    ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxPlavog.setConstraints(plaviIgracLabel, constraints);
		panelZaComboBoxPlavog.add(plaviIgracLabel);
		
		JComboBox<String> comboPlavi = new JComboBox<String>(Igrac.algoritmi);
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxPlavog.setConstraints(comboPlavi, constraints);
		panelZaComboBoxPlavog.add(comboPlavi);
		
		JLabel crveniIgracLabel = new JLabel("Crveni igrač: ");
	    constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxCrvenog.setConstraints(crveniIgracLabel, constraints);
		panelZaComboBoxCrvenog.add(crveniIgracLabel);
		
		JComboBox<String> comboCrveni = new JComboBox<String>(Igrac.algoritmi);
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxCrvenog.setConstraints(comboCrveni, constraints);
		panelZaComboBoxCrvenog.add(comboCrveni);
		
		JCheckBox checkBoxZaPrikazTabele = new JCheckBox("Prikazuj dialog sa tabelom za akcije i Q-vrednosti", true);
		checkBoxZaPrikazTabele.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				controller.setPrikaziDialogTabelaAkcijaIQVrednosti(checkBoxZaPrikazTabele.isSelected(), true);
				
			}
		});
	    constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutCheckBoxZaPrikazTabelePanel.setConstraints(checkBoxZaPrikazTabele, constraints);
		checkBoxZaPrikazTabelePanel.add(checkBoxZaPrikazTabele);
		
		
		constraints.insets = new Insets(0,0,2,0);
		
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutMainPanel.setConstraints(panelZaComboBoxPlavog, constraints);
		mainPanel.add(panelZaComboBoxPlavog);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		gridBagLayoutMainPanel.setConstraints(panelZaComboBoxCrvenog, constraints);
		mainPanel.add(panelZaComboBoxCrvenog);
	 
	    JSpinner spinnerSekunde = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
	    JPanel panelZaSekunde = pomocniPanel.napraviPanelZaSekunde(spinnerSekunde, true);
	    constraints.gridx = 0;
		constraints.gridy = 2;
		gridBagLayoutMainPanel.setConstraints(panelZaSekunde, constraints);
		mainPanel.add(panelZaSekunde);
	    ukluciIliIskluciPanelZaSekunde(panelZaSekunde, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
	    
	    JSpinner spinnerEpsilon = new JSpinner(new SpinnerNumberModel(0.10, 0.00, 1.00, 0.01));
	    spinnerEpsilon.getEditor().setPreferredSize(new Dimension(25, 15));
	    JPanel panelZaEpsilon = pomocniPanel.napraviPanelZaEpsilon(spinnerEpsilon, true, false);
	    constraints.gridx = 0;
		constraints.gridy = 3;
		gridBagLayoutMainPanel.setConstraints(panelZaEpsilon, constraints);
		mainPanel.add(panelZaEpsilon);
	    ukluciIliIskluciPanelZaEpsilon(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
	    
	    constraints.gridx = 0;
		constraints.gridy = 4;
		gridBagLayoutMainPanel.setConstraints(checkBoxZaPrikazTabelePanel, constraints);
		mainPanel.add(checkBoxZaPrikazTabelePanel);
	    ukluciIliIskluciCheckBoxZaPrikazTabelePanel(checkBoxZaPrikazTabelePanel, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
	    
	    comboPlavi.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				ukluciIliIskluciPanelZaSekunde(panelZaSekunde, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
				ukluciIliIskluciPanelZaEpsilon(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
				ukluciIliIskluciCheckBoxZaPrikazTabelePanel(checkBoxZaPrikazTabelePanel, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
			}
		});
	    comboCrveni.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				ukluciIliIskluciPanelZaSekunde(panelZaSekunde, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
				ukluciIliIskluciPanelZaEpsilon(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
				ukluciIliIskluciCheckBoxZaPrikazTabelePanel(checkBoxZaPrikazTabelePanel, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
			}
		});
	    
	    String[] options = { "Počni Novu Igru"};

	    String title = "Nova Igra...";
	    int res = JOptionPane.showOptionDialog(null, mainPanel, title,
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
	        options[0]);

	    if (res >= 0) {
	    	int selektovaniIndeksPlavi = comboPlavi.getSelectedIndex();
		    int selektovaniIndeksCrveni = comboCrveni.getSelectedIndex();
		  
		    System.out.println("Plavi igrač: " + Igrac.algoritmi[selektovaniIndeksPlavi]);
		    System.out.println("Crveni igrač: " + Igrac.algoritmi[selektovaniIndeksCrveni]);
		    
		    return new int[] {selektovaniIndeksPlavi, selektovaniIndeksCrveni};
	    }
	    else {
	    	System.out.println("Exit");
	    	return new int[] {-1,-1};
	    }
  
	}

	private void ukluciIliIskluciPanelZaSekunde(JPanel panelZaSekunde, Algoritam algoritamPlavi, Algoritam algoritamCrveni) {
		boolean enable;
		
		if(algoritamPlavi == Algoritam.COVEK && algoritamCrveni == Algoritam.COVEK) {
			enable = false;
		}
		else {
			enable = true;
		}
		
		for (Component component : panelZaSekunde.getComponents()) {
			component.setEnabled(enable);
		}
	}

	private void ukluciIliIskluciPanelZaEpsilon(JPanel panelZaEpsilon, Algoritam algoritamPlavi, Algoritam algoritamCrveni) {
		boolean enable;
		
		if(algoritamPlavi == Algoritam.RL || algoritamCrveni == Algoritam.RL) {
			enable = true;
		}
		else {
			enable = false;
		}
		
		for (Component component : panelZaEpsilon.getComponents()) {
			component.setEnabled(enable);
		}
		
	}
	
	protected void ukluciIliIskluciCheckBoxZaPrikazTabelePanel(JPanel checkBoxZaPrikazTabelePanel, Algoritam algoritamPlavi, Algoritam algoritamCrveni) {
		boolean enable;
		
		if(algoritamPlavi == Algoritam.RL || algoritamCrveni == Algoritam.RL) {
			enable = true;
		}
		else {
			enable = false;
		}
		
		for (Component component : checkBoxZaPrikazTabelePanel.getComponents()) {
			component.setEnabled(enable);
		}
	}
	
	public int[] dialogZaTreniranje() {
		GridBagLayout gridBagLayoutMainPanel = new GridBagLayout();
		JPanel mainPanel = new JPanel(gridBagLayoutMainPanel);
		
		GridBagLayout gridBagLayoutPanelZaComboBoxPlavog = new GridBagLayout();
		JPanel panelZaComboBoxPlavog = new JPanel(gridBagLayoutPanelZaComboBoxPlavog);
		
		GridBagLayout gridBagLayoutPanelZaComboBoxCrvenog = new GridBagLayout();
		JPanel panelZaComboBoxCrvenog = new JPanel(gridBagLayoutPanelZaComboBoxCrvenog);
		
		GridBagLayout gridBagLayoutBrojPartijaPanel = new GridBagLayout();
		JPanel panelZaBrojPartija = new JPanel(gridBagLayoutBrojPartijaPanel);
		
		GridBagLayout gridBagLayoutCheckBoxZaPrikazTabelePanel = new GridBagLayout();
		JPanel checkBoxZaPrikazTabelePanel = new JPanel(gridBagLayoutCheckBoxZaPrikazTabelePanel);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		
		JLabel plaviIgracLabel = new JLabel("Plavi igrač:    ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxPlavog.setConstraints(plaviIgracLabel, constraints);
		panelZaComboBoxPlavog.add(plaviIgracLabel);
		
		JComboBox<String> comboPlavi = new JComboBox<String>(Igrac.algoritmiZaTrening);
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxPlavog.setConstraints(comboPlavi, constraints);
		panelZaComboBoxPlavog.add(comboPlavi);
		
		JLabel crveniIgracLabel = new JLabel("Crveni igrač: ");
	    constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxCrvenog.setConstraints(crveniIgracLabel, constraints);
		panelZaComboBoxCrvenog.add(crveniIgracLabel);
		
		JComboBox<String> comboCrveni = new JComboBox<String>(Igrac.algoritmiZaTrening);
		constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayoutPanelZaComboBoxCrvenog.setConstraints(comboCrveni, constraints);
		panelZaComboBoxCrvenog.add(comboCrveni);
	
		JLabel brojPartijaLabel = new JLabel("Broj partija:   ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutBrojPartijaPanel.setConstraints(brojPartijaLabel, constraints);
		panelZaBrojPartija.add(brojPartijaLabel);
		
		JSpinner spinnerBrojPartija = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
	    constraints.gridx = 1;
		constraints.gridy = 0;
		gridBagLayoutBrojPartijaPanel.setConstraints(spinnerBrojPartija, constraints);
		panelZaBrojPartija.add(spinnerBrojPartija);
		
		JCheckBox checkBoxZaPrikazTabele = new JCheckBox("Prikazuj dialog sa tabelom za akcije i Q-vrednosti", true);
		checkBoxZaPrikazTabele.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				controller.setPrikaziDialogTabelaAkcijaIQVrednosti(checkBoxZaPrikazTabele.isSelected(), true);
				
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutCheckBoxZaPrikazTabelePanel.setConstraints(checkBoxZaPrikazTabele, constraints);
		checkBoxZaPrikazTabelePanel.add(checkBoxZaPrikazTabele);
		
		
		constraints.insets = new Insets(0,0,2,0);
		
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayoutMainPanel.setConstraints(panelZaComboBoxPlavog, constraints);
		mainPanel.add(panelZaComboBoxPlavog);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		gridBagLayoutMainPanel.setConstraints(panelZaComboBoxCrvenog, constraints);
		mainPanel.add(panelZaComboBoxCrvenog);
		    
		constraints.gridx = 0;
		constraints.gridy = 2;
		gridBagLayoutMainPanel.setConstraints(panelZaBrojPartija, constraints);
		mainPanel.add(panelZaBrojPartija);
	   
	    JSpinner spinnerSekunde = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
	    JPanel panelZaSekunde = pomocniPanel.napraviPanelZaSekunde(spinnerSekunde, true);
	    constraints.gridx = 0;
		constraints.gridy = 3;
		gridBagLayoutMainPanel.setConstraints(panelZaSekunde, constraints);
		mainPanel.add(panelZaSekunde);
	    
	    JSpinner spinnerEpsilon = new JSpinner(new SpinnerNumberModel(0.10, 0.00, 1.00, 0.01));
	    spinnerEpsilon.getEditor().setPreferredSize(new Dimension(25, 15));
	    JPanel panelZaEpsilon = pomocniPanel.napraviPanelZaEpsilon(spinnerEpsilon, true, false);
	    constraints.gridx = 0;
		constraints.gridy = 4;
		gridBagLayoutMainPanel.setConstraints(panelZaEpsilon, constraints);
		mainPanel.add(panelZaEpsilon);
	    ukluciIliIskluciPanelZaEpsilon(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
	    
	    constraints.gridx = 0;
		constraints.gridy = 5;
		gridBagLayoutMainPanel.setConstraints(checkBoxZaPrikazTabelePanel, constraints);
		mainPanel.add(checkBoxZaPrikazTabelePanel);
	    ukluciIliIskluciCheckBoxZaPrikazTabelePanel(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
	    
	    comboPlavi.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				ukluciIliIskluciPanelZaEpsilon(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
				ukluciIliIskluciCheckBoxZaPrikazTabelePanel(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
			    
			}
		});
	    comboCrveni.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				ukluciIliIskluciPanelZaEpsilon(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
				ukluciIliIskluciCheckBoxZaPrikazTabelePanel(panelZaEpsilon, Igrac.getEnumAlgoritam((String) comboPlavi.getSelectedItem()), Igrac.getEnumAlgoritam((String) comboCrveni.getSelectedItem()));
			    
			}
		});
	    
	    String[] options = { "Počni Trening"};

	    String title = "Trening...";
	    int res = JOptionPane.showOptionDialog(null, mainPanel, title,
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
	        options[0]);

	    if (res >= 0) {
	    	int selektovaniIndeksPlavi = comboPlavi.getSelectedIndex();
		    int selektovaniIndeksCrveni = comboCrveni.getSelectedIndex();
		    int brojPartija = (Integer) spinnerBrojPartija.getValue();
		  
		    System.out.println("Plavi igrač: " + Igrac.algoritmiZaTrening[selektovaniIndeksPlavi]);
		    System.out.println("Crveni igrač: " + Igrac.algoritmiZaTrening[selektovaniIndeksCrveni]);
		    System.out.println("Broj partija: " + brojPartija);
		    
		    return new int[] {selektovaniIndeksPlavi, selektovaniIndeksCrveni, brojPartija};
	    }
	    else {
	    	System.out.println("Exit");
	    	return new int[] {-1, -1, -1};
	    }
  
	}

	public int dialogZaCuvanjeQVrednostiITezina(boolean predjenaGranicaZaPrelazNaAproksimaciju) {
		String poruka = "Da li želite da sačuvate nove Q-vrednosti";
		if(predjenaGranicaZaPrelazNaAproksimaciju) poruka += " i tezine";
		poruka += "?";
		
		String naslov = "Čuvanje Q-vrednosti...";
		
		int res = JOptionPane.showConfirmDialog(null, poruka, naslov, JOptionPane.YES_NO_CANCEL_OPTION);
		
		return res;
	}
	
	public void prikaziTabeluAkcijaiQVrednosti(HashMap<PoljeAkcija, Double> qVrednostiZaMoguceAkcije, PoljeAkcija odabranoSelektovanoPoljeAkcija) {
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel(new BorderLayout());
        JCheckBox prikazujovajDialogCheckBox = new JCheckBox("Prikazuj i dalje ovaj dialog", controller.isPrikaziDialogTabelaAkcijaIQVrednosti());
        northPanel.add(prikazujovajDialogCheckBox, BorderLayout.NORTH);
        northPanel.add(new JLabel("    "), BorderLayout.SOUTH);
		
		if(qVrednostiZaMoguceAkcije != null) {
			DefaultTableModel dm = new DefaultTableModel(new String[] {"Selektovano polje", "Akcija", "Q-vrednost"}, qVrednostiZaMoguceAkcije.size())   {

			    @Override
			    public boolean isCellEditable(int row, int column) { return false; }
			    
			    @Override
			    public Class<?> getColumnClass(int column) {
					if (column == 0 || column == 1) {
						return String.class;
					}
					
					return Double.class;
			    	
			    	//return String.class;
			    }
			};
			
			String strPolje;
			double vrednost;
			int i = 0;
			//int indeksOdabraneAkcije = -1;
			
	        for(PoljeAkcija selektovanoPoljeAkcija : qVrednostiZaMoguceAkcije.keySet()) {
	            if(selektovanoPoljeAkcija.getPolje() != null) strPolje = selektovanoPoljeAkcija.getPolje().mojToString();
	            else strPolje = "null";
	            
	            dm.setValueAt(strPolje, i, 0);
	            dm.setValueAt(selektovanoPoljeAkcija.getAkcija().name(), i, 1);
	            vrednost = qVrednostiZaMoguceAkcije.get(selektovanoPoljeAkcija);
	            dm.setValueAt(vrednost, i, 2);
	            //if(selektovanoPoljeAkcija.getAkcija() == odabranaAkcija)  indeksOdabraneAkcije = i;
	            i++;
	        }
	        
	        JTable table = new JTable(dm);
	       
	        String odabranoSelektovanoPoljeStr;
	        if(odabranoSelektovanoPoljeAkcija.getPolje() != null) odabranoSelektovanoPoljeStr = odabranoSelektovanoPoljeAkcija.getPolje().mojToString();
            else odabranoSelektovanoPoljeStr = "null";
	        for (int j = 0; j < dm.getColumnCount(); j++) {
				table.getColumnModel().getColumn(j).setCellRenderer(new CustomTableCellRenderer(odabranoSelektovanoPoljeStr, odabranoSelektovanoPoljeAkcija.getAkcija().name()));
			}
	        
	        //table.setRowSelectionInterval(indeksOdabraneAkcije, indeksOdabraneAkcije);
	        //table.setSelectionBackground(new Color(255, 51, 51)); // svetlo crvena boja
	        
	        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
	        sorter.addRowSorterListener (new RowSorterListener() {
	            @Override
	            public void sorterChanged (RowSorterEvent e) {
	                if (e.getType () == RowSorterEvent.Type.SORTED) {
	                    System.out.println ("selected row: "+table.getSelectedRow());
	                    if (table.getSelectedRow () != -1) {
	                        table.scrollRectToVisible (table.getCellRect (table.getSelectedRow (), 0, false));
	                    }
	                }
	            }
	        });
	        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(3);
	        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
	        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
	        sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
	        sorter.setSortKeys(sortKeys);
	        sorter.toggleSortOrder(2); // po default-u tabela ce biti sortirana po q-vrednostima (2 kolona brojeci od 0)
	        table.setRowSorter(sorter);
	        
	        table.getTableHeader().setBackground(Color.GREEN);
	        
	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.getVerticalScrollBar().setBackground(Color.DARK_GRAY);
	        
	        mainPanel.add(northPanel, BorderLayout.NORTH);
	        mainPanel.add(scrollPane, BorderLayout.CENTER);
	        
		}
		else {
			JPanel northPanel2 = new JPanel(new BorderLayout());
			northPanel2.add(northPanel, BorderLayout.NORTH);
			northPanel2.add(new JLabel("Izabrana je random akcija."), BorderLayout.SOUTH);
			
			mainPanel.add(northPanel2, BorderLayout.NORTH);
		}
        
        String[] options = { "Zatvori"};

        JOptionPane.showOptionDialog(null, mainPanel, "Akcije i Q-vrednosti", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        controller.setPrikaziDialogTabelaAkcijaIQVrednosti(prikazujovajDialogCheckBox.isSelected(), true);
	}
	
	public void osveziTablu() {
		tabla.refresh();
	}

	public void resetujPomocniPanelZaNovuIgru(Stanje trenutnoStanje) {
		pomocniPanel.resetujSveZaNovuIgru(trenutnoStanje);
	}
	
	public void podesiBrojPlavihNepostavljenihFigura(int brojNepostavljenihPlavihFigura) {
		pomocniPanel.setBrojPlavihNepostavljenihFigura(brojNepostavljenihPlavihFigura);
	}
	
	public void podesiBrojCrvenihNepostavljenihFigura(int brojNepostavljenihCrvenihFigura) {
		pomocniPanel.setBrojCrvenihNepostavljenihFigura(brojNepostavljenihCrvenihFigura);
	}
	
	public void podesiBrojPlavihPreostalihFigura(int brojPlavihPreostalihFigura) {
		pomocniPanel.setBrojPlavihPreostalihFigura(brojPlavihPreostalihFigura);
	}
	
	public void podesiBrojCrvenihPreostalihFigura(int brojCrvenihPreostalihFigura) {
		pomocniPanel.setBrojCrvenihPreostalihFigura(brojCrvenihPreostalihFigura);
	}
	
	public void podesiNaPotezu(TipPolja igracNaPotezu) {
		pomocniPanel.setNaPotezu(igracNaPotezu);
	}
	
	public void podesiBrojPreostalihPartijaLabel(int brojPreostalihPartija) {
		pomocniPanel.setBrojPreostalihPartijaLabel(brojPreostalihPartija);
	}
	
	public void prikaziNaPotezuPanel() {
		pomocniPanel.getNaPotezuPanel().setVisible(true);
	}
	
	public void sakrijNaPotezuPanel() {
		pomocniPanel.getNaPotezuPanel().setVisible(false);
	}
	
	public void prikaziPanelZaBrojPlavihNepostavljenihFigura() {
		pomocniPanel.getBrojPlavihNepostavljenihFiguraPanel().setVisible(true);
	}
	
	public void sakrijPanelZaBrojPlavihNepostavljenihFigura() {
		pomocniPanel.getBrojPlavihNepostavljenihFiguraPanel().setVisible(false);
	}
	
	public void prikaziPanelZaBrojPlavihPreostalihFigura() {
		pomocniPanel.getBrojPlavihPreostalihFiguraPanel().setVisible(true);
	}
	
	public void sakrijPanelZaBrojPlavihPreostalihFigura() {
		pomocniPanel.getBrojPlavihPreostalihFiguraPanel().setVisible(false);
	}
	
	public void prikaziPanelZaBrojCrvenihNepostavljenihFigura() {
		pomocniPanel.getBrojCrvenihNepostavljenihFiguraPanel().setVisible(true);
	}
	
	public void sakrijPanelZaBrojCervenihNepostavljenihFigura() {
		pomocniPanel.getBrojCrvenihNepostavljenihFiguraPanel().setVisible(false);
	}
	
	public void prikaziPanelZaBrojCrvenihPreostalihFigura() {
		pomocniPanel.getBrojCrvenihPreostalihFiguraPanel().setVisible(true);
	}
	
	public void sakrijPanelZaBrojCrvenihPreostalihFigura() {
		pomocniPanel.getBrojCrvenihPreostalihFiguraPanel().setVisible(false);
	}
	
	public void prikaziPanelZaBrojPreostalihPartija() {
		pomocniPanel.getBrojPreostalihPartijaLabelPanel().setVisible(true);
	}
	
	public void sakrijPanelZaBrojPreostalihPartija() {
		pomocniPanel.getBrojPreostalihPartijaLabelPanel().setVisible(false);
	}
	
	public void podesiPojediLabel(String figuraZaJedenje, TipPolja tipFigureKojuTrebaPojesti) {
		pomocniPanel.setPojediLabel(figuraZaJedenje, tipFigureKojuTrebaPojesti);
	}

	/*public void prikaziPojediLabel() {
		pomocniPanel.getPojediLabel().setVisible(true);
	}
	
	public void sakrijPojediLabel() {
		pomocniPanel.getPojediLabel().setVisible(false);
	}*/
	
	public void prikaziPanelZaSekunde() {
		pomocniPanel.getPanelZaSekunde().setVisible(true);
	}

	public void sakrijPanelZaSekunde() {
		pomocniPanel.getPanelZaSekunde().setVisible(false);
	}
	
	public void prikaziPanelZaEpsilon() {
		pomocniPanel.getPanelZaEpsilon().setVisible(true);
	}
	
	public void sakrijPanelZaEpsilon() {
		pomocniPanel.getPanelZaEpsilon().setVisible(false);
	}
	
	public void prikaziPanelZaPojediLabel() {
		pomocniPanel.getPojediLabelPanel().setVisible(true);
	}
	
	public void sakrijPanelZaPojediLabel() {
		pomocniPanel.getPojediLabelPanel().setVisible(false);
	}
	
	public void prikaziCheckBoxZaPrikazTabelePanel() {
		pomocniPanel.getCheckBoxZaPrikazTabelePanel().setVisible(true);
	}
	
	public void sakrijCheckBoxZaPrikazTabelePanel() {
		pomocniPanel.getCheckBoxZaPrikazTabelePanel().setVisible(false);
	}
	
	public void podesiCheckBoxZaPrikazTabele(boolean prikazujTabelu) {
		pomocniPanel.getCheckBoxZaPrikazTabele().setSelected(prikazujTabelu);;
	}
	
	public void setBrojSekundiUPomocnomPanelu(int brojSekundi) {
		pomocniPanel.setBrojSekundi(brojSekundi);
	}
	
	public void setEpsilonUPomocnomPanelu(double epsilon) {
		pomocniPanel.setEpsilon(epsilon);
	}
	
}

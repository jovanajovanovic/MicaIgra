package com.mica.gui;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.mica.main.Algoritam;
import com.mica.main.Controller;
import com.mica.main.Igrac;
import com.mica.main.Stanje;

@SuppressWarnings("serial")
public class GlavniProzor extends JFrame {
	
	private Tabla tabla;
	private PomocniPanel pomocniPanel;
	
	private Controller controller;
	
	public GlavniProzor(Controller controller) {
		super("Mica");
		
		this.controller = controller;
		
	    setLayout(new BorderLayout());
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	        
	    tabla = new Tabla(controller);
	    controller.setTabla(tabla);
	    
	    pomocniPanel = new PomocniPanel();
	    controller.setPomocniPanel(pomocniPanel);
	    
	    Stanje trenutnoStanje = controller.getTrenutnoStanje();
	    pomocniPanel.setNaPotezu(trenutnoStanje.getIgracNaPotezu());
	    pomocniPanel.setBrojPlavihNepostavljenihFigura(trenutnoStanje.getPlaviIgrac().getBrojNepostavljenihFigura());
	    pomocniPanel.setBrojCrvenihNepostavljenihFigura(trenutnoStanje.getCrveniIgrac().getBrojNepostavljenihFigura());
	    pomocniPanel.setBrojPlavihPreostalihFigura(trenutnoStanje.getPlaviIgrac().getBrojPreostalihFigura());
	    pomocniPanel.setBrojCrvenihPreostalihFigura(trenutnoStanje.getCrveniIgrac().getBrojPreostalihFigura());
	    
		getContentPane().add(tabla, BorderLayout.CENTER);
		getContentPane().add(pomocniPanel, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//tabla.refresh();
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public static int[] dialogZaNovuIgru() {
	    final JComboBox<Algoritam> comboPlavi = new JComboBox<Algoritam>(Igrac.algoritmi);
	    final JComboBox<Algoritam> comboCrveni = new JComboBox<Algoritam>(Igrac.algoritmi);
	    
	    final JPanel westPanel = new JPanel(new BorderLayout());
	    westPanel.add(new JLabel("Plavi igrac:"), BorderLayout.NORTH);
	    westPanel.add(new JLabel("Crveni igrac:"), BorderLayout.SOUTH);
	    
	    final JPanel eastPanel = new JPanel(new BorderLayout());
	    eastPanel.add(comboPlavi, BorderLayout.NORTH);
	    eastPanel.add(comboCrveni, BorderLayout.SOUTH);
	    
	    final JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.add(westPanel, BorderLayout.WEST);
	    mainPanel.add(eastPanel, BorderLayout.EAST);
	    
	    String[] options = { "Počni Novu Igru"};

	    String title = "Nova Igra...";
	    int res = JOptionPane.showOptionDialog(null, mainPanel, title,
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
	        options[0]);

	    if (res >= 0) {
	    	int selektovaniIndeksPlavi = comboPlavi.getSelectedIndex();
		    int selektovaniIndeksCrveni = comboCrveni.getSelectedIndex();
		  
		    System.out.println("Plavi igrac: " + Igrac.algoritmi[selektovaniIndeksPlavi]);
		    System.out.println("Crveni igrac: " + Igrac.algoritmi[selektovaniIndeksCrveni]);
		    
		    return new int[] {selektovaniIndeksPlavi, selektovaniIndeksCrveni};
	    }
	    else {
	    	System.out.println("Exit");
	    	return new int[] {-1,-1};
	    }
  
	}

	public static int[] dialogZaTreniranje() {
	    final JComboBox<Algoritam> comboPlavi = new JComboBox<Algoritam>(Igrac.algoritmiZaTrening);
	    final JComboBox<Algoritam> comboCrveni = new JComboBox<Algoritam>(Igrac.algoritmiZaTrening);
	    JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
	    
	    final JPanel westPanel = new JPanel(new BorderLayout());
	    westPanel.add(new JLabel("Plavi igrac:"), BorderLayout.NORTH);
	    westPanel.add(new JLabel("Crveni igrac:"), BorderLayout.SOUTH);
	    
	    final JPanel eastPanel = new JPanel(new BorderLayout());
	    eastPanel.add(comboPlavi, BorderLayout.NORTH);
	    eastPanel.add(comboCrveni, BorderLayout.SOUTH);
	    
	    final JPanel southPanel = new JPanel(new BorderLayout());
	    southPanel.add(new JLabel("Broj partija:"), BorderLayout.WEST);
	    southPanel.add(spinner, BorderLayout.EAST);
	    
	    final JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.add(westPanel, BorderLayout.WEST);
	    mainPanel.add(eastPanel, BorderLayout.EAST);
	    mainPanel.add(southPanel, BorderLayout.SOUTH);
	    
	    String[] options = { "Počni Novu Igru"};

	    String title = "Trening...";
	    int res = JOptionPane.showOptionDialog(null, mainPanel, title,
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
	        options[0]);

	    if (res >= 0) {
	    	int selektovaniIndeksPlavi = comboPlavi.getSelectedIndex();
		    int selektovaniIndeksCrveni = comboCrveni.getSelectedIndex();
		    int brojPartija = (Integer) spinner.getValue();
		  
		    System.out.println("Plavi igrac: " + Igrac.algoritmiZaTrening[selektovaniIndeksPlavi]);
		    System.out.println("Crveni igrac: " + Igrac.algoritmiZaTrening[selektovaniIndeksCrveni]);
		    System.out.println("Broj partija: " + brojPartija);
		    
		    return new int[] {selektovaniIndeksPlavi, selektovaniIndeksCrveni, brojPartija};
	    }
	    else {
	    	System.out.println("Exit");
	    	return new int[] {-1, -1, -1};
	    }
  
	}
	
}

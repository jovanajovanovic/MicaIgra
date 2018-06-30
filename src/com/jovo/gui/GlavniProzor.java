package com.jovo.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.jovo.main.Controller;
import com.jovo.main.Stanje;

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
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
}

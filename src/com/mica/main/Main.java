package com.mica.main;

import com.mica.gui.GlavniProzor;

public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();
		GlavniProzor glavniProzor = new GlavniProzor(controller);
		controller.setGlavniProzor(glavniProzor);
		glavniProzor.setVisible(true);
		
		//controller.zapocniNovuIgru();
		glavniProzor.dialogPocetni(null);

	}

}

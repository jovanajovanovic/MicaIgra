package com.mica.main;

import com.mica.gui.GlavniProzor;

public class Main {

	public static void main(String[] args) {
		TipPolja igracNaPotezu = TipPolja.PLAVO;
		Controller con = new Controller(igracNaPotezu);
		GlavniProzor gp = new GlavniProzor(con);
		gp.setVisible(true);

	}

}

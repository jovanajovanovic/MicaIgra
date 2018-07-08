package com.mica.algorithms;

import java.util.Comparator;

import com.mica.main.Polje;
import com.mica.main.PoljeAkcija;

public class HeuristikaMinPoredjenje implements Comparator<PoljeAkcija> {

	@Override
	public int compare(PoljeAkcija p0, PoljeAkcija p1) {
		// TODO Auto-generated method stub
		return p0.getScore() - p1.getScore();
	}

}

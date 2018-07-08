package com.mica.algorithms;

import java.util.Comparator;

import com.mica.main.PoljeAkcija;

public class HeuristikaMaxPoredjenje implements Comparator<PoljeAkcija> {

	@Override
	public int compare(PoljeAkcija p0, PoljeAkcija p1) {
		// TODO Auto-generated method stub
		return p1.getScore() - p0.getScore();
	}

}

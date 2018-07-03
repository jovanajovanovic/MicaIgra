package com.mica.main;

import java.util.HashMap;

public class Akcije {
	public final static Akcija[] POSTAVLJANJE = { Akcija.POSTAVI_0, Akcija.POSTAVI_1, Akcija.POSTAVI_2, Akcija.POSTAVI_3, 
												  Akcija.POSTAVI_4, Akcija.POSTAVI_5, Akcija.POSTAVI_6, Akcija.POSTAVI_7, 
												  Akcija.POSTAVI_8, Akcija.POSTAVI_9, Akcija.POSTAVI_10, Akcija.POSTAVI_11,
												  Akcija.POSTAVI_12, Akcija.POSTAVI_13, Akcija.POSTAVI_14, Akcija.POSTAVI_15, 
												  Akcija.POSTAVI_16, Akcija.POSTAVI_17, Akcija.POSTAVI_18, Akcija.POSTAVI_19, 
												  Akcija.POSTAVI_20, Akcija.POSTAVI_21, Akcija.POSTAVI_22, Akcija.POSTAVI_23 };
	
	public static final Akcija[] GDLD = { Akcija.GORE, Akcija.DOLE, Akcija.LEVO, Akcija.DESNO };
	
	public static final Akcija[] SKOKOVI = {	Akcija.SKOK_0, Akcija.SKOK_1, Akcija.SKOK_2, Akcija.SKOK_3, Akcija.SKOK_4,
												Akcija.SKOK_5, Akcija.SKOK_6, Akcija.SKOK_7, Akcija.SKOK_8,
												Akcija.SKOK_9, Akcija.SKOK_10, Akcija.SKOK_11, Akcija.SKOK_12, Akcija.SKOK_13, 
												Akcija.SKOK_14, Akcija.SKOK_15, Akcija.SKOK_16, Akcija.SKOK_17, Akcija.SKOK_18,
												Akcija.SKOK_19, Akcija.SKOK_20, Akcija.SKOK_21, Akcija.SKOK_22, Akcija.SKOK_23,
											};
	
	public static final Akcija[] JEDENJE = { Akcija.POJEDI_0, Akcija.POJEDI_1, Akcija.POJEDI_2, Akcija.POJEDI_3, Akcija.POJEDI_4,
											 Akcija.POJEDI_5, Akcija.POJEDI_6, Akcija.POJEDI_7, Akcija.POJEDI_8, Akcija.POJEDI_9, 
											 Akcija.POJEDI_10, Akcija.POJEDI_11, Akcija.POJEDI_12, Akcija.POJEDI_13, 
											 Akcija.POJEDI_14, Akcija.POJEDI_15, Akcija.POJEDI_16, Akcija.POJEDI_17, 
											 Akcija.POJEDI_18, Akcija.POJEDI_19, Akcija.POJEDI_20, Akcija.POJEDI_21, 
											 Akcija.POJEDI_22, Akcija.POJEDI_23 };
	
	public static final HashMap<Akcija, Pozicija> mapiranjeGDLDAkcijaNaKorake;
	 
    static {
    	mapiranjeGDLDAkcijaNaKorake = new HashMap<Akcija, Pozicija>();
    	mapiranjeGDLDAkcijaNaKorake.put(Akcija.GORE, new Pozicija(-1, 0));
    	mapiranjeGDLDAkcijaNaKorake.put(Akcija.DOLE, new Pozicija(1, 0));
    	mapiranjeGDLDAkcijaNaKorake.put(Akcija.LEVO, new Pozicija(0, 1));
    	mapiranjeGDLDAkcijaNaKorake.put(Akcija.DESNO, new Pozicija(0, -1));
	}
}

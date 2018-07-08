package com.mica.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import com.mica.main.Akcija;
import com.mica.main.Akcije;
import com.mica.main.Controller;
import com.mica.main.Igrac;
import com.mica.main.Polje;
import com.mica.main.PoljeAkcija;
import com.mica.main.Potez;
import com.mica.main.Pozicija;
import com.mica.main.Stanje;
import com.mica.main.TipPolja;

public class MiniMax {

	private int dubinaStabla;
	private Igrac protivnik;
	private Igrac igrac;
	private int maxRezultat = 1000000;
	private int rezultat = 0;
	private Polje[][] micaKombinacije;

	public int getDubinaStabla() {
		return dubinaStabla;
	}

	public void setDubinaStabla(int dubinaStabla) {
		this.dubinaStabla = dubinaStabla;
	}

	public Igrac getIgrac() {
		return igrac;
	}

	public void setIgrac(Igrac igrac) {
		this.igrac = igrac;
	}

	public MiniMax() {
	}

	public MiniMax(int dubinaStabla, Igrac protivnik, Igrac igrac) {
		if (dubinaStabla < 1) {
			// greska
		}
		this.dubinaStabla = dubinaStabla;
		if (igrac.getTipIgraca() == TipPolja.CRVENO) {
			this.protivnik.setTipIgraca(TipPolja.PLAVO);
		} else {
			this.protivnik.setTipIgraca(TipPolja.CRVENO);
		}
	}

	public Potez noviPotez(Stanje trenutnoStanje) {
		Stanje novoStanje = new Stanje(trenutnoStanje);
		Polje[][] polja = trenutnoStanje.getPolja();
		napraviSveMice(polja);
		// vratimo najbolju akciju od svih akcija, koju god akciju vratimo
		// moramo pogledati da li zahteva jedenje i koje figure
		PoljeAkcija akcijaSelektovanogPolja = getAkcijaZaSelektovanoPolje(novoStanje); // ovde
																						// treba
																						// vartiti
																						// najbolju
																						// akciju
		Akcija najboljaAkcija = akcijaSelektovanogPolja.getAkcija();
		String nazivAkcije = najboljaAkcija.name();
		Polje novoPolje;
		Polje selektovanoPolje;

		if (nazivAkcije.contains("POSTAVI")) {
			selektovanoPolje = null;
			novoPolje = akcijaSelektovanogPolja.getPolje();

		} else {
			selektovanoPolje = akcijaSelektovanogPolja.getPrethodnoPolje();
			if (nazivAkcije.contains("SKOK")) {
				int indeks = Integer.parseInt(nazivAkcije.split("_")[1]);
				novoPolje = polja[indeks / Controller.BROJ_POLJA_U_KRUGU][indeks % Controller.BROJ_POLJA_U_KRUGU];

			} else if (nazivAkcije.contains("POJEDI")) {
				int indeks = Integer.parseInt(nazivAkcije.split("_")[1]);
				novoPolje = polja[indeks / Controller.BROJ_POLJA_U_KRUGU][indeks % Controller.BROJ_POLJA_U_KRUGU];
			} else {
				// povlacenje po tabli, gore, dole, levo i desno
				Pozicija korak = Akcije.mapiranjeGDLDAkcijaNaKorake.get(najboljaAkcija);
				novoPolje = akcijaSelektovanogPolja.getPolje();
				/*
				 * int slojNovogPolja = selektovanoPolje.getPozicija().getX() +
				 * korak.getX(); int indeksUNovomSloju =
				 * (selektovanoPolje.getPozicija().getY() + korak.getY()) %
				 * Controller.BROJ_POLJA_U_KRUGU; if (indeksUNovomSloju == -1) {
				 * indeksUNovomSloju = Controller.BROJ_POLJA_U_KRUGU - 1; } try
				 * { novoPolje = polja[slojNovogPolja][indeksUNovomSloju]; }
				 * catch (Exception e) { return null; }
				 */
			}
		}

		if (novoPolje != null) {
			novoPolje = trenutnoStanje.getPolja()[novoPolje.getPozicija().getX()][novoPolje.getPozicija().getY()];
		}

		if (selektovanoPolje != null) {
			selektovanoPolje = trenutnoStanje.getPolja()[selektovanoPolje.getPozicija().getX()][selektovanoPolje
					.getPozicija().getY()];
		}

		return new Potez(novoPolje, selektovanoPolje, najboljaAkcija);
	}

	private void napraviSveMice(Polje[][] svaPolja) {
		// pravimo sve mica kombinacije
		micaKombinacije = new Polje[Controller.BROJ_MICA_KOMBINACIJA][Controller.BROJ_FIGURA_U_MICI];

		// spoljasnji krug
		micaKombinacije[0][0] = svaPolja[0][0];
		micaKombinacije[0][1] = svaPolja[0][1];
		micaKombinacije[0][2] = svaPolja[0][2];

		micaKombinacije[1][0] = svaPolja[0][2];
		micaKombinacije[1][1] = svaPolja[0][3];
		micaKombinacije[1][2] = svaPolja[0][4];

		micaKombinacije[2][0] = svaPolja[0][4];
		micaKombinacije[2][1] = svaPolja[0][5];
		micaKombinacije[2][2] = svaPolja[0][6];

		micaKombinacije[3][0] = svaPolja[0][6];
		micaKombinacije[3][1] = svaPolja[0][7];
		micaKombinacije[3][2] = svaPolja[0][0];

		// sredisnji krug
		micaKombinacije[4][0] = svaPolja[1][0];
		micaKombinacije[4][1] = svaPolja[1][1];
		micaKombinacije[4][2] = svaPolja[1][2];

		micaKombinacije[5][0] = svaPolja[1][2];
		micaKombinacije[5][1] = svaPolja[1][3];
		micaKombinacije[5][2] = svaPolja[1][4];

		micaKombinacije[6][0] = svaPolja[1][4];
		micaKombinacije[6][1] = svaPolja[1][5];
		micaKombinacije[6][2] = svaPolja[1][6];

		micaKombinacije[7][0] = svaPolja[1][6];
		micaKombinacije[7][1] = svaPolja[1][7];
		micaKombinacije[7][2] = svaPolja[1][0];

		// unutrasnji krug
		micaKombinacije[8][0] = svaPolja[2][0];
		micaKombinacije[8][1] = svaPolja[2][1];
		micaKombinacije[8][2] = svaPolja[2][2];

		micaKombinacije[9][0] = svaPolja[2][2];
		micaKombinacije[9][1] = svaPolja[2][3];
		micaKombinacije[9][2] = svaPolja[2][4];

		micaKombinacije[10][0] = svaPolja[2][4];
		micaKombinacije[10][1] = svaPolja[2][5];
		micaKombinacije[10][2] = svaPolja[2][6];

		micaKombinacije[11][0] = svaPolja[2][6];
		micaKombinacije[11][1] = svaPolja[2][7];
		micaKombinacije[11][2] = svaPolja[2][0];

		// sredisnje mice koje spajaju sva tri kruga
		micaKombinacije[12][0] = svaPolja[0][1];
		micaKombinacije[12][1] = svaPolja[1][1];
		micaKombinacije[12][2] = svaPolja[2][1];

		micaKombinacije[13][0] = svaPolja[0][3];
		micaKombinacije[13][1] = svaPolja[1][3];
		micaKombinacije[13][2] = svaPolja[2][3];

		micaKombinacije[14][0] = svaPolja[0][5];
		micaKombinacije[14][1] = svaPolja[1][5];
		micaKombinacije[14][2] = svaPolja[2][5];

		micaKombinacije[15][0] = svaPolja[0][7];
		micaKombinacije[15][1] = svaPolja[1][7];
		micaKombinacije[15][2] = svaPolja[2][7];

	}

	public Polje[] getMicaKombinacija(int i) {
		return micaKombinacije[i];
	}

	public PoljeAkcija getAkcijaZaSelektovanoPolje(Stanje trenutnoStanje) {
		// pronalazi najbolje polje i akciju za trenutno stanje
		PoljeAkcija najboljePolje = null;
		ArrayList<PoljeAkcija> svaMogucaPoljaIAkcije = null;
		try {
			svaMogucaPoljaIAkcije = generisanjeKoraka(igrac, trenutnoStanje);
			if (svaMogucaPoljaIAkcije.isEmpty()) {
				return null;
			}
			// izabrati najbolje polje i akciju
			for (PoljeAkcija pa : svaMogucaPoljaIAkcije) {
				odradiKorak(pa, trenutnoStanje);
				int s = pa.getScore();
				s += alphBetaPruning(igrac, pa, trenutnoStanje, dubinaStabla - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
				pa.setScore(s);
				vratiKorak(pa, trenutnoStanje);
			}

			/// ovde svima setujemo polje na zuto, ovo moze samo kad smo u fazi
			/// postavljanja figura

			for (PoljeAkcija pa : svaMogucaPoljaIAkcije) {
				if (pa.getAkcija().name().contains("POSTAVI")) {
					pa.getPolje().setTipPolja(TipPolja.ZUTO);
				}
			}
			Collections.sort(svaMogucaPoljaIAkcije, new HeuristikaMaxPoredjenje());

			ArrayList<PoljeAkcija> najboljeAkcije = new ArrayList<PoljeAkcija>();
			int najboljiScore = svaMogucaPoljaIAkcije.get(0).getScore();
			najboljeAkcije.add(svaMogucaPoljaIAkcije.get(0));
			for (int i = 0; i < svaMogucaPoljaIAkcije.size(); i++) {
				if (svaMogucaPoljaIAkcije.get(i).getScore() == najboljiScore) {
					najboljeAkcije.add(svaMogucaPoljaIAkcije.get(i));
				} else {
					break;
				}
			}
			Random random = new Random();
			najboljePolje = najboljeAkcije.get(random.nextInt(najboljeAkcije.size()));
			return najboljePolje;

		} catch (Exception e) {

		}

		return najboljePolje;

	}

	private void vratiKorak(PoljeAkcija pa, Stanje trenutnoStanje) {
		// vratimo na stanje pre izvrsene akcije
		Polje selektovanoPolje = pa.getPrethodnoPolje();// polje na kojem se
														// nalazila figura pre
														// pomeranja
		Polje novoPolje = pa.getPolje(); // polje na kojem se figura trenutno
											// nalazi

		novoPolje.setTipPolja(TipPolja.ZUTO);// samo vratimo novo polje na zuto
		if (trenutnoStanje.daLiSuSveFigurePostavljene() == true) {
			// sve figure postavljene, nalazimo se u fazi pomeranja
			// moramo da vratimo figuru na prethodnu poziciju
			selektovanoPolje.setTipPolja(igrac.getTipIgraca());
		}

		// ako smo imali jedenje vratimo i figuru na poziciju sa koje smo je
		// pojeli
		

	}

	private int alphBetaPruning(Igrac igra, PoljeAkcija pa, Stanje trenutnoStanje, int dubina, int minValue,
			int maxValue) {
		// TODO Auto-generated method stub
		// uzmemo sve korake
		ArrayList<PoljeAkcija> koraci;
		try {
			if (this.dubinaStabla == 0) {
				return izracunaj(pa, trenutnoStanje);
			} else if ((koraci = generisanjeKoraka(igra, trenutnoStanje)).isEmpty()) {
				// igrac ne moze vise nista da odigra
				if (igra.getTipIgraca() == igrac.getTipIgraca()) {
					return -maxRezultat;
				} else {
					return maxRezultat;
				}
			} else {

				// prodjemo kroz listu svih koraka
				koraci = generisanjeKoraka(igra, trenutnoStanje);
				for (PoljeAkcija p : koraci) {
					odradiKorak(p, trenutnoStanje);

					if (igrac.getTipIgraca() == igra.getTipIgraca()) {
						// maksimizujemo igraca
						minValue = Math.max(minValue,
								alphBetaPruning(protivnik, pa, trenutnoStanje, dubina, minValue, maxValue));

						if (maxValue <= minValue) {
							vratiKorak(pa, trenutnoStanje);
							break;
						}
					} else {
						// minimizujemo igraca
						maxValue = Math.min(maxValue,
								alphBetaPruning(igrac, pa, trenutnoStanje, dubina - 1, minValue, maxValue));

						if (maxValue <= minValue) {
							vratiKorak(pa, trenutnoStanje);
							break;
						}
					}
					vratiKorak(pa, trenutnoStanje);

					if (igrac.getTipIgraca() == igra.getTipIgraca()) {
						return minValue;
					} else {
						return maxValue;
					}

				}
			}

		} catch (Exception e) {

		}
		return -1;
	}

	private int izracunaj(PoljeAkcija pa, Stanje trenutnoStanje) {
		// vrednost akcije
		int rez = 0;
		int brojMica = 0;
		int brojProtivnikovihMica = 0;
		int brojSastavljenih2 = 0;
		int brojProtivnikovihSpojenih2 = 0;

		for (int i = 0; i < Controller.BROJ_MICA_KOMBINACIJA; i++) {
			int igracevaPolja = 0;
			int praznaPolja = 0;
			int protivnikovaPolja = 0;

			try {
				Polje[] mica = getMicaKombinacija(i);

				for (int j = 0; j < Controller.BROJ_FIGURA_U_MICI; j++) {
					if (mica[j].getTipPolja() == igrac.getTipIgraca()) {
						igracevaPolja++;
					} else if (mica[j].getTipPolja() == TipPolja.ZUTO) {
						praznaPolja++;
					} else {
						protivnikovaPolja++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (igracevaPolja == 3) {
				// sklopljena mica
				brojMica++;
			} else if (igracevaPolja == 2 && praznaPolja == 1) {
				brojSastavljenih2++;
			} else if (igracevaPolja == 1 && praznaPolja == 2) {
				rez += 1;
			} else if (protivnikovaPolja == 2 && praznaPolja == 1) {
				brojProtivnikovihSpojenih2++;
			} else if (protivnikovaPolja == 3) {
				// protivnik ima sklopljenu micu
				brojProtivnikovihMica++;
			} else if (protivnikovaPolja == 1 && praznaPolja == 2) {
				rez += -1;
			}

			// proverimo da li se protivnik nalazi na kljucnim pozicijama u
			// sredinama
			// kljucne pozicije su stanja polja [1][1], [1][3], [1][5], [1][7] i
			// tu dobije 2 boda
			// manje kljucne ali bitne pozicije su i sredine stranica velikog i
			// najmanjeg kvadrata , za njih dobija 1 bod
			Polje[][] polja = trenutnoStanje.getPolja();
			if (polja[1][1].getTipPolja() == igrac.getTipIgraca() || polja[1][3].getTipPolja() == igrac.getTipIgraca()
					|| polja[1][5].getTipPolja() == igrac.getTipIgraca()
					|| polja[1][7].getTipPolja() == igrac.getTipIgraca()) {
				rez += 2;
			} else if (polja[1][1].getTipPolja() == TipPolja.ZUTO || polja[1][3].getTipPolja() == TipPolja.ZUTO
					|| polja[1][5].getTipPolja() == TipPolja.ZUTO || polja[1][7].getTipPolja() == TipPolja.ZUTO) {
				rez -= 2;
			} else if (polja[0][1].getTipPolja() == igrac.getTipIgraca()
					|| polja[0][3].getTipPolja() == igrac.getTipIgraca()
					|| polja[0][5].getTipPolja() == igrac.getTipIgraca()
					|| polja[0][7].getTipPolja() == igrac.getTipIgraca()
					|| polja[2][1].getTipPolja() == igrac.getTipIgraca()
					|| polja[2][3].getTipPolja() == igrac.getTipIgraca()
					|| polja[2][5].getTipPolja() == igrac.getTipIgraca()
					|| polja[2][7].getTipPolja() == igrac.getTipIgraca()) {
				rez += 1;
			} else if (polja[0][1].getTipPolja() == TipPolja.ZUTO || polja[0][3].getTipPolja() == TipPolja.ZUTO
					|| polja[0][5].getTipPolja() == TipPolja.ZUTO || polja[0][7].getTipPolja() == TipPolja.ZUTO
					|| polja[2][1].getTipPolja() == TipPolja.ZUTO || polja[2][3].getTipPolja() == TipPolja.ZUTO
					|| polja[2][5].getTipPolja() == TipPolja.ZUTO || polja[2][7].getTipPolja() == TipPolja.ZUTO) {
				rez -= 1;
			}

		}

		int koeficijentMica;
		int koeficijentNiz;
		// broj mica i bodovi za dve figure u nizu
		if (igrac.getBrojNepostavljenihFigura() >= 0) {
			// jos u fazi postavljanja

			koeficijentMica = 50;
			koeficijentNiz = 6;
		} else {
			koeficijentMica = 100;
			koeficijentNiz = 12;
		}

		rez += koeficijentMica * brojMica + koeficijentNiz * brojSastavljenih2;
		rez -= koeficijentMica * brojProtivnikovihMica - koeficijentNiz * brojProtivnikovihSpojenih2;

		return rez;
	}

	private ArrayList<PoljeAkcija> generisanjeKoraka(Igrac igra, Stanje trenutnoStanje) {
		// generisanje svih mogucih koraka na osnovu trenutnog stanja
		ArrayList<PoljeAkcija> koraci = new ArrayList<PoljeAkcija>();
		Polje selektovanoPolje;
		Polje[] susednaPolja;

		try {
			if (trenutnoStanje.daLiSuSveFigurePostavljene() == false) {
				// jos je u fazi postavljanja
				// prodjemo kroz sva polja na tabli, pronadjemo polja koja nisu
				// zauzeta
				for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
					for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
						if (trenutnoStanje.getPolja()[i][j].getTipPolja() == TipPolja.ZUTO) {
							// slobodno polje
							// zauzmemo ga,a zatim proverimo korak koji smo
							// napravili
							// napravimo akciju za zauzimanje tog polja
							Akcija a = getAkcijuPostavi(i, j);
							PoljeAkcija postaviFiguru = new PoljeAkcija(trenutnoStanje.getPolja()[i][j],
									getAkcijuPostavi(i, j));
							// trenutnoStanje.getPolja()[i][j].setTipPolja(igrac.getTipIgraca());
							proveriKorak(trenutnoStanje, koraci, postaviFiguru);// trenutno
																				// stanje,
																				// svi
																				// prethodni
																				// koraci
																				// i
																				// polje
																				// koje
																				// hocemo
																				// da
																				// zauzmemo
							trenutnoStanje.getPolja()[i][j].setTipPolja(TipPolja.ZUTO);

						}
					}
				}
			} else if (trenutnoStanje.getCrveniIgrac().getTipIgraca() == igrac.getTipIgraca()
					&& trenutnoStanje.getCrveniIgrac().getBrojPreostalihFigura() <= 3) {
				// treba da proverimo da li se nalazimo u fazi skoka,
				// provericemo tako sto cemo videti koji nam je igrac i koliko
				// figura ima na tabli
				fazaSkakanja(koraci, trenutnoStanje);

			} else if (trenutnoStanje.getPlaviIgrac().getTipIgraca() == igrac.getTipIgraca()
					&& trenutnoStanje.getPlaviIgrac().getBrojPreostalihFigura() <= 3) {
				// skakanje
				fazaSkakanja(koraci, trenutnoStanje);

			} else {
				// faza pomeranja figura
				for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
					for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
						if (trenutnoStanje.getPolja()[i][j].getTipPolja() == igrac.getTipIgraca()) {
							// selektujemo to polje i za njega izvucemo sve
							// korake
							ArrayList<PoljeAkcija> dozvoljeneAkcije = dajDozvoljeneAkcije(trenutnoStanje, i, j);

							for (int n = 0; n < dozvoljeneAkcije.size(); n++) {
								if (dozvoljeneAkcije.get(n).getPolje().getTipPolja() == TipPolja.ZUTO) {
									// slobodno polje i mozemo da pomerimo
									// figuru na njega
									// prosledimo akciju da bi videli da li nam
									// odgovara, zajedno sa akcijom prosledimo i
									// selektovano polje
									PoljeAkcija uraditiAkciju = dozvoljeneAkcije.get(n);
									uraditiAkciju.setPrethodnoPolje(trenutnoStanje.getPolja()[i][j]);
									proveriKorak(trenutnoStanje, koraci, uraditiAkciju);
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {

		}

		// ako je dubina stabla veca od 3 onda rangiramo korake i sortiramo ih
		if (dubinaStabla > 3) {
			for (PoljeAkcija a : koraci) {
				Polje p = a.getPolje();// uzmemo polje na koje se pomeramo

				// zauzemo polje
				p.setTipPolja(igrac.getTipIgraca());

				/*
				 * if (trenutnoStanje.isPojedi() == true) { // zahteva da
				 * pojedemo protivnickog igraca // napravimo akciju za jedenje
				 * figure Akcija pojediPolje = getAkcijaPojedi(a.getJediPolje(),
				 * trenutnoStanje); if (pojediPolje != null) { PoljeAkcija
				 * pojedi = new PoljeAkcija(a.getJediPolje(), pojediPolje);//
				 * treba // na // osnovu // polja // izvuci // akciju // pojedi
				 * // proveriKorak(trenutnoStanje, koraci, pojedi); }
				 * 
				 * a.getJediPolje().setTipPolja(TipPolja.ZUTO); }
				 */

				// izracunamo skor za potez
				a.setScore(izracunaj(a, trenutnoStanje));

				// vratimo potez
				p.setTipPolja(TipPolja.ZUTO);

			}

			if (igra.getTipIgraca() == igrac.getTipIgraca()) {
				Collections.sort(koraci, new HeuristikaMaxPoredjenje());
			} else {
				Collections.sort(koraci, new HeuristikaMinPoredjenje());
			}
		}

		return koraci;
	}

	private boolean daLiJePoljeUMici(Polje pojediPolje) {
		// prodjemo kroz sve mice i pronadjemo trazeno polje, ako je u mici
		// vracamo true ako nije vratimo false
		// izvucemo sva polja protivnika koja se nalaze u mici
		ArrayList<Polje> poljaUMici = new ArrayList<Polje>();
		int brojPoljaUMici = 0;
		for (int i = 0; i < Controller.BROJ_MICA_KOMBINACIJA; i++) {
			Polje[] mica = micaKombinacije[i]; // jedna mica kombinacija;
			for (int m = 0; m < Controller.BROJ_FIGURA_U_MICI; m++) {
				if (mica[m].getTipPolja() == protivnik.getTipIgraca()) {
					brojPoljaUMici++;
				}
			}
			if (brojPoljaUMici == 3) {
				// protivnik ima micu i sva polja ubacujemo u listu
				poljaUMici.add(mica[0]);
				poljaUMici.add(mica[1]);
				poljaUMici.add(mica[2]);
			}
			brojPoljaUMici = 0;
		}

		for (Polje p : poljaUMici) {
			if (p.getPozicija().getX() == pojediPolje.getPozicija().getX()
					&& p.getPozicija().getY() == pojediPolje.getPozicija().getY()) {
				return true; // polje u mici
			}
		}

		return false;// polje nije u mici
	}

	private Akcija getAkcijaPojedi(Polje polje, Stanje trenutnoStanje) {
		// na osnovu polja izvucemo akciju za jedenje tog polja
		// prodnadjemo indekse tog polja
		int brojKruga = -1;
		int brojPoljaUKrugu = -1;
		int indeks = -1;
		Polje[][] svaPolja = trenutnoStanje.getPolja();
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				// po koordinatama pronadjemo trazeno polje
				if (svaPolja[i][j].getPozicija().getX() == polje.getPozicija().getX()
						&& svaPolja[i][j].getPozicija().getY() == polje.getPozicija().getY()) {
					// pronasli smo ga i sad samo uzmemo indekse
					brojKruga = i;
					brojPoljaUKrugu = j;
				}
			}
		}

		if (brojKruga > -1 && brojPoljaUKrugu > -1) {
			return getAkcijaPojedi(brojKruga, brojPoljaUKrugu);
		}
		return null;
	}

	private void fazaSkakanja(ArrayList<PoljeAkcija> koraci, Stanje trenutnoStanje) {
		// faza skanja
		// izdvojimo slobodna polja i polja zauzeta od strane igraca koji
		// trenutno igra
		ArrayList<Polje> slobodnaPolja = getSlobodnaPolja(trenutnoStanje);
		ArrayList<Polje> igracZauzeoPolja = getZauzetaPoljaIgraca(trenutnoStanje); // treba
																					// da
																					// bi
																					// smo
																					// znali
																					// sta
																					// pomeramo

		// prodjemo kroz listu zauzetih polja i biramo jednu figuru sa kojom
		// skacemo
		for (int i = 0; i < igracZauzeoPolja.size(); i++) {
			Polje p = igracZauzeoPolja.get(i);
			// pomerimo figuru sa tog polja na drugo mesto
			for (int j = 0; j < slobodnaPolja.size(); j++) {
				// biramo na koje polje prenosimo figuru
				// pravimo akciju skoka
				Akcija akcijaSkoka = getAkcijaSkoka(slobodnaPolja.get(j));
				PoljeAkcija pa = new PoljeAkcija(slobodnaPolja.get(j), akcijaSkoka);
				proveriKorak(trenutnoStanje, koraci, pa);
				slobodnaPolja.get(j).setTipPolja(TipPolja.ZUTO);
			}
		}
	}

	private Akcija getAkcijaSkoka(Polje polje) {
		// na osnovu prosledjenog polja, vraca odgovarajucu akciju skoka na to
		// polje
		// treba izracunati indeks tog polja
		return null;
	}

	private ArrayList<Polje> getSlobodnaPolja(Stanje trenutnoStanje) {
		// vraca sva slobodna polja na tabli
		ArrayList<Polje> slobodna = new ArrayList<Polje>();
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if (trenutnoStanje.getPolja()[i][j].getTipPolja() == TipPolja.ZUTO) {
					// polje slobodno
					slobodna.add(trenutnoStanje.getPolja()[i][j]);
				}
			}
		}
		return slobodna;
	}

	private ArrayList<Polje> getZauzetaPoljaIgraca(Stanje trenutnoStanje) {
		// polja koja su zauzeta od strane igraca
		ArrayList<Polje> zauzetaPolja = new ArrayList<Polje>();
		for (int i = 0; i < Controller.BROJ_KRUGOVA; i++) {
			for (int j = 0; j < Controller.BROJ_POLJA_U_KRUGU; j++) {
				if (trenutnoStanje.getPolja()[i][j].getTipPolja() == igrac.getTipIgraca()) {
					// polje zauzeto
					zauzetaPolja.add(trenutnoStanje.getPolja()[i][j]);
				}
			}
		}
		return zauzetaPolja;
	}

	private Akcija getAkcijuPostavi(int brojKruga, int brojPoljaUKrugu) {
		// vraca akciju postavljanja na odredjeno polje na tabli
		int indeks = 8 * brojKruga + brojPoljaUKrugu; // zato sto u krugu ima 9
														// figura
		return Akcije.POSTAVLJANJE[indeks];
	}

	private ArrayList<PoljeAkcija> dajDozvoljeneAkcije(Stanje trenutnoStanje, int i, int j) {
		// izvlacimo dozvoljene akcije na osnovu trenutnog stanja i selektovanog
		// polja
		ArrayList<PoljeAkcija> dozvoljeneAkcije = new ArrayList<PoljeAkcija>();
		Polje[][] svaPolja = trenutnoStanje.getPolja();
		if (j == 0) {
			// moze da ide desno(0,7 | 1,7 | 2,7) ili dole (0,1 | 1,1 | 2, 1);
			dozvoljeneAkcije.add(new PoljeAkcija(svaPolja[i][j + 1], Akcija.DOLE));
			dozvoljeneAkcije.add(new PoljeAkcija(svaPolja[i][7], Akcija.DESNO));
		} else if (j == 1 | j == 3 || j == 5) {
			dozvoljeneAkcije = generisiAkcijaZaNeparne(i, j, svaPolja);
		} else if (j == 2 || j == 4 || j == 6) {
			dozvoljeneAkcije = generisiAkcijeZaParne(i, j, svaPolja);
		} else if (j == 7) {
			dozvoljeneAkcije = generisiAkcijeZaPolje7(i, j, svaPolja);
		}
		return dozvoljeneAkcije;
	}

	private ArrayList<PoljeAkcija> generisiAkcijeZaPolje7(int i, int j, Polje[][] svaPolja) {
		ArrayList<PoljeAkcija> akcije = new ArrayList<PoljeAkcija>();
		// razlikuje se od ostalih polja zato sto mu je susedno polje 0
		Polje p0 = svaPolja[i][0];
		Polje p1 = svaPolja[i][j - 1];
		akcije.add(new PoljeAkcija(p0, Akcija.LEVO));
		akcije.add(new PoljeAkcija(p1, Akcija.DESNO));
		// ostala polja se razlikuju od nivoa na kojem se polje 7 nalazi
		if (i == 0) {
			// moze da ide jos samo dole u unutrasnji krug
			Polje p00 = svaPolja[i + 1][j];
			akcije.add(new PoljeAkcija(p00, Akcija.DOLE));
		} else if (i == 1) {
			// moze da ide gore ili dole
			akcije.add(new PoljeAkcija(svaPolja[i - 1][j], Akcija.GORE));// spoljasnji
																			// krug
			akcije.add(new PoljeAkcija(svaPolja[i + 1][j], Akcija.DOLE)); // unutrasnji
																			// krug;
		} else if (i == 2) {
			// samo moze gore
			akcije.add(new PoljeAkcija(svaPolja[i - 1][j], Akcija.GORE));
		}

		return akcije;
	}

	private ArrayList<PoljeAkcija> generisiAkcijeZaParne(int i, int j, Polje[][] svaPolja) {
		// generisemo sve moguce akcije za parna polja
		ArrayList<PoljeAkcija> akcije = new ArrayList<PoljeAkcija>();
		// razlikuju se samo u akcijama, polja se istovremeno genesrisu
		Polje p0 = svaPolja[i][j + 1];
		Polje p1 = svaPolja[i][j - 1];

		if (j == 2) {
			akcije.add(new PoljeAkcija(p0, Akcija.DESNO));
			akcije.add(new PoljeAkcija(p1, Akcija.GORE));
		} else if (j == 4) {
			akcije.add(new PoljeAkcija(p0, Akcija.GORE));
			akcije.add(new PoljeAkcija(p1, Akcija.LEVO));
		} else if (j == 6) {
			akcije.add(new PoljeAkcija(p0, Akcija.LEVO));
			akcije.add(new PoljeAkcija(p1, Akcija.DOLE));
		}

		return akcije;
	}

	private ArrayList<PoljeAkcija> generisiAkcijaZaNeparne(int i, int j, Polje[][] svaPolja) {
		// TODO Auto-generated method stub

		ArrayList<PoljeAkcija> dozvoljeneAkcije = new ArrayList<PoljeAkcija>();
		// ovde zavisi i od nivoa na kojem se nalazimo
		// 1. napravicemo polja posto se ne razlikuju i onda cemo im samo
		// dodavati odgovarajuce akcije
		Polje p0 = svaPolja[i][j + 1];
		Polje p1 = svaPolja[i][j - 1]; // polje na istom nivou
		// generisanje polja na razlicitim nivoima
		if (i == 0) {// spoljasnji krug
			Polje p00 = svaPolja[i + 1][j];
			if (j == 1) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.DOLE));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p00, Akcija.DESNO));
			} else if (j == 3) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.DESNO));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.LEVO));
				dozvoljeneAkcije.add(new PoljeAkcija(p00, Akcija.GORE));
			} else if (j == 5) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.DOLE));
				dozvoljeneAkcije.add(new PoljeAkcija(p00, Akcija.LEVO));
			}
		} else if (i == 1) {// srednji krug
			Polje p10 = svaPolja[i + 1][j];
			Polje p11 = svaPolja[i - 1][j];
			if (j == 1) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.DOLE));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p10, Akcija.DESNO));
				dozvoljeneAkcije.add(new PoljeAkcija(p11, Akcija.LEVO));
			} else if (j == 3) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.DESNO));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.LEVO));
				dozvoljeneAkcije.add(new PoljeAkcija(p10, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p11, Akcija.DOLE));

			} else if (j == 5) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.DOLE));
				dozvoljeneAkcije.add(new PoljeAkcija(p10, Akcija.LEVO));
				dozvoljeneAkcije.add(new PoljeAkcija(p11, Akcija.DESNO));

			}
		} else if (i == 2) {// unutrasnju krug
			Polje p20 = svaPolja[i - 1][j];
			if (j == 1) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.DOLE));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p20, Akcija.LEVO));
			} else if (j == 3) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.DESNO));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.LEVO));
				dozvoljeneAkcije.add(new PoljeAkcija(p20, Akcija.DOLE));
			} else if (j == 5) {
				dozvoljeneAkcije.add(new PoljeAkcija(p0, Akcija.GORE));
				dozvoljeneAkcije.add(new PoljeAkcija(p1, Akcija.DOLE));
				dozvoljeneAkcije.add(new PoljeAkcija(p20, Akcija.DESNO));
			}
		}

		return dozvoljeneAkcije;
	}

	private void proveriKorak(Stanje trenutnoStanje, ArrayList<PoljeAkcija> koraci, PoljeAkcija akcija) {
		// proveravamo u kakvu poziciju cemo doci ako odradimo korak
		// cilj nam je da napravimo micu
		boolean mica = false;
		for (int i = 0; i < Controller.BROJ_MICA_KOMBINACIJA; i++) {
			// prolazimo kroz sve kombinacije mica i gledamo u kakvoj smo
			// poziciji
			int brojFigura = 0;
			boolean selektovanoPolje = false;

			Polje[] kombinacijaMice = getMicaKombinacija(i); // 1 mica
																// kombinacija

			for (int j = 0; j < Controller.BROJ_FIGURA_U_MICI; j++) {
				if (kombinacijaMice[j].getTipPolja() == igrac.getTipIgraca()) {
					brojFigura++;
				}
				if (kombinacijaMice[j].getPozicija().getX() == akcija.getPolje().getPozicija().getX()
						&& kombinacijaMice[j].getPozicija().getY() == akcija.getPolje().getPozicija().getY()) {
					// znaci da je to polje selektovano
					selektovanoPolje = true;
				}
			}

			// proveravamo da li smo postavljanjem te figure napravili micu
			if (brojFigura == 3 && selektovanoPolje) {
				mica = true;
				
				 
			}
			selektovanoPolje = false;
		}


	// ako nismo napravili micu dodamo korak
	if(!mica)

	{
		koraci.add(akcija);
	}else
	{
		mica = false;
	}

	}

	private Akcija getAkcijaPojedi(int brojKruga, int redniBrojUKrugu) {
		int indeks = 8 * brojKruga + redniBrojUKrugu;
		return Akcije.JEDENJE[indeks];
	}

	private void odradiKorak(PoljeAkcija pa, Stanje trenutnoStanje) {
		// TODO Auto-generated method stub
		// uzmemo polje sa kojeg pomeramo figuru
		Polje selektovanoPolje = pa.getPrethodnoPolje(); // sa ovog polja
															// pomeramo figuru
		Polje novoPolje = pa.getPolje(); // polje na koje postavljamo figuru
		novoPolje.setTipPolja(igrac.getTipIgraca()); // postavimo figuru
		pa.setPolje(novoPolje);
		// ako se nalazimo u fazi pomeranja
		if (trenutnoStanje.daLiSuSveFigurePostavljene() == true) {
			// postavljene sve figure
			// skinemo figuru sa prethodnog polja
			selektovanoPolje.setTipPolja(TipPolja.ZUTO);
			pa.setPrethodnoPolje(selektovanoPolje);
		}

		

	}
	
	public PoljeAkcija najboljaAkcijaZaJedenje(Stanje trenutnoStanje){
		//1. proverimo da li su sve protivnicke figure u mici, ako jesu mozemo da jedemo bilo koju
		ArrayList<PoljeAkcija> koraci = new ArrayList<PoljeAkcija>();
		if(trenutnoStanje.daLiSuSvaProtivnickaPoljaUTari(protivnik.getTipIgraca()) == true){
			//bilo koje polje moze da se pojede, prodjemo kroz sva i pogledamo koje nm daje najbolje performanse
			for(int i =0; i < Controller.BROJ_KRUGOVA; i++){
				for(int j =  0; j < Controller.BROJ_POLJA_U_KRUGU; j++){
					//proverimo da li je polje od protivnika
					if (trenutnoStanje.getPolja()[i][j].getTipPolja() == protivnik.getTipIgraca()){
						PoljeAkcija pojedi = new PoljeAkcija(trenutnoStanje.getPolja()[i][j], getAkcijaPojedi(i,j));
						koraci.add(pojedi);
					}
				}
			}
			
			//svejedno koju cemo pojesti pa samo random izaberemo 
			Random r = new Random();
			return koraci.get(r.nextInt(koraci.size()));
			
		}else {
			//uzmemo sva polja od protivnika i proveravamo koje je najbolje da pojedemo 
			for(int i =0; i < Controller.BROJ_KRUGOVA; i++){
				for(int j =  0; j < Controller.BROJ_POLJA_U_KRUGU; j++){
					//proverimo da li je polje od protivnika
					if (trenutnoStanje.getPolja()[i][j].getTipPolja() == protivnik.getTipIgraca()){
						PoljeAkcija pojedi = new PoljeAkcija(trenutnoStanje.getPolja()[i][j], getAkcijaPojedi(i,j));
						koraci.add(pojedi);
					}
				}
			}
			
			for(PoljeAkcija pa : koraci){
				//svaki korak proverimo i izracunamo vrednost za svaki 
				int rez = izracunajScoreJedenja(pa);
				if(pa.getScore() < rez){
					pa.setScore(rez);
				}
			}
			
			Collections.sort(koraci, new HeuristikaMaxPoredjenje());
			
			return koraci.get(0);
			
		}
		
		
	}
	
	private int izracunajScoreJedenja(PoljeAkcija pojedi){
		int rezultat = 0;
		int brojProtivnikovihFigura = 0;
		int brojMojihFigura = 0;
		int brojSlobodnihFigura = 0;
		
		for(int i =0 ; i < Controller.BROJ_MICA_KOMBINACIJA; i++){
			//uzmemo jednu kombinaciju
			Polje[] mica = micaKombinacije[i];
			//prodjemo kroz nju i vidimo da li se u njoj nalazi trazeno polje
			for(int j = 0; j< Controller.BROJ_FIGURA_U_MICI; j++){
				if(mica[j].getTipPolja() == igrac.getTipIgraca()){
					brojMojihFigura++;
				}else if(mica[j].getTipPolja()== protivnik.getTipIgraca()){
					brojProtivnikovihFigura++;
				}else {
					brojSlobodnihFigura++;
				}
				if(mica[j].getPozicija().getX() == pojedi.getPolje().getPozicija().getX() && mica[j].getPozicija().getY() == pojedi.getPolje().getPozicija().getY()){
					//u trazenoj mici je i polje za brisanje 
					//ako je polje tu onda pogledamo kakva su nam ostala polja
					if (brojProtivnikovihFigura == 3){
						//ne mozemo da jedemo figuru stavimo da nam je skor minimalan
						rezultat = -100;
					}else if(brojProtivnikovihFigura == 2 && brojMojihFigura == 1){
						rezultat = 0;
					}else if(brojProtivnikovihFigura ==  2 && brojSlobodnihFigura == 1){
						rezultat = 50;
					}else if (brojProtivnikovihFigura ==1 && brojMojihFigura == 2){
						rezultat = 50;
					}
				}
				
				if(pojedi.getScore() < rezultat){
					return rezultat;
				}
			}
		}
		
		return 0;
		
	}

	public Potez noviPotezJediFiguru(Stanje trenutnoStanje2) {
		// vraca potez za jedenje figure
		// trazimo figuru koju cemo da pojedemo
		Stanje trenutnoStanje = new Stanje(trenutnoStanje2);
		Polje pojediPolje = null;
		
		PoljeAkcija najboljaAkcija = najboljaAkcijaZaJedenje(trenutnoStanje);
		Akcija akcija = najboljaAkcija.getAkcija();
		
		pojediPolje = najboljaAkcija.getPolje();
		
		
		if (pojediPolje != null) {
			pojediPolje = trenutnoStanje2.getPolja()[pojediPolje.getPozicija().getX()][pojediPolje.getPozicija().getY()];
		}

		
		
		return new Potez(pojediPolje, pojediPolje, akcija);
	}

}

package com.pl.maciej.prog;

import java.io.Serializable;



public class Plyta implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String wykonawca = "";
	private String tytul = "";
	private PlytaGatunek gatunek = PlytaGatunek.nieokreslony;
	private int rok;
	private long id;
	private	long id_osoby;
	
	public Plyta() {
		
	}
	
	public Plyta(long par_id, String par_wykonawca, String par_tytul, PlytaGatunek par_gatunek, int par_rok)
	{
		id = par_id;
		wykonawca = par_wykonawca;
		tytul = par_tytul;
		gatunek = par_gatunek;
		rok = par_rok;
	}
	
	public long getId_osoby() {
		return id_osoby;
	}

	public void setId_osoby(long id_osoby) {
		this.id_osoby = id_osoby;
	}

	public Plyta(long par_id, String par_wykonawca, String par_tytul, int par_gatunek, int par_rok, long par_id_osoby)
	{
		id = par_id;
		wykonawca = par_wykonawca;
		tytul = par_tytul;
		gatunek = PlytaGatunek.values()[ par_gatunek ];
		rok = par_rok;
		id_osoby = par_id_osoby;
	}
	
	void wyswietlPlyte() {
		System.out.println("Wykonawca: " + wykonawca + "\nTytul: " + tytul + "\nGatunek: " + gatunek + "\nRok: " + rok);
	}
	
	public	String	tytul() {
		return	tytul;
	}
	
	public	String	wykonawca() {
		return	wykonawca;
	}
	
	public	PlytaGatunek	gatunek() {
		return	gatunek;
	}
	
	public int 		gatunekInt() {
		return gatunek.ordinal();
	}
	
	public	int		rok() {
		return	rok;
	}
	
	public long 	dajIdPlyty() {
		return id;
	}
	
	
	
	public String getWykonawca() {
		return wykonawca;
	}

	public void setWykonawca(String wykonawca) {
		this.wykonawca = wykonawca;
	}

	public String getTytul() {
		return tytul;
	}

	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	public PlytaGatunek getGatunek() {
		return gatunek;
	}

	public void setGatunek(PlytaGatunek gatunek) {
		this.gatunek = gatunek;
	}

	public int getRok() {
		return rok;
	}

	public void setRok(int rok) {
		this.rok = rok;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public	String	plyta() {
		return	"Wykonawca-" + wykonawca + " Tytul-" + tytul + " Gtunek-" + gatunek + " Rok wydania-" + rok;
	}
}

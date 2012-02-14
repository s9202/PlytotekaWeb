package com.pl.maciej.prog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class KolekcjaPlyt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Plyta> plyty 				= new ArrayList<Plyta>();


	
	void	addToList( Plyta pPlyta ) throws MojWyjatek {
		
		Calendar kalendarz = Calendar.getInstance(); 		// ustalanie bie��cego roku wewn�trz metody dodawania p�yty
		int rokBiezacy = kalendarz.get( Calendar.YEAR ); 	// umo�liwia poprawn� reakcj� na wej�cie w nowy rok (z 31.12 na 01.01) 
		
		if ( plyty == null )
			throw new MojWyjatek("Lista jest niezainicjowana");
		if ( pPlyta.rok() > rokBiezacy)
			throw new MojWyjatek("BLAD: Podany rok jest pozniejszy niz obecny. Wpis bledny: "
					+ pPlyta.wykonawca() + " | " + pPlyta.tytul() + " | " + pPlyta.gatunek() + " | " + pPlyta.rok() );
		if ( pPlyta.rok() < 1885)
			throw new MojWyjatek("Pierwsze plyty nagrano w 1885, podano zbyt wczesny rok. Wpis bledny: "
					+ pPlyta.wykonawca() + " | " + pPlyta.tytul() + " | " + pPlyta.gatunek() + " | " + pPlyta.rok() );
		
		int 	pozycja 	= wyszukajPlyte( pPlyta.tytul(), pPlyta.wykonawca(), pPlyta.gatunek(), pPlyta.rok() );

		if (pozycja >= 0) 
			throw new MojWyjatek("Taka p�yta ju� istnieje na liscie. Wpis bledny: "
					+ pPlyta.wykonawca() + " | " + pPlyta.tytul() + " | " + pPlyta.gatunek() + " | " + pPlyta.rok() );
		
		plyty.add( pPlyta );
		
	}
	
	
	
	
	public void	dodajPlyte(long pId, String pWykonawca, String pTytul, PlytaGatunek pGatunek, int pRok ) {
		
		try {
			addToList( new Plyta( pId, pWykonawca, pTytul, pGatunek, pRok ) );
		}
		catch (MojWyjatek e) {
			
			System.out.println( e );
		}
	}
	
	/*-----------------------------------------------------------------------------------
	 * "wyszukajPlyte" przeszukuje kolekcj� zawart� w polu "plyty" w celu znalezienia takiej,  
	 * kt�rej pola: "wykonawca", "tytul", "gatunek", rok" b�d� mia�y takie same warto�ci,
	 * jak odpowiadaj�ce im parametry wywo�ania metody: "pWykonawca", "pTytul", "pGatunek", 
	 * "pRok".
	 * 
	 * Metoda zwraca:
	 * pozycj� (indeks) znalezionego obiektu albo
	 * -1, gdy obiekt nie zostanie znaleziony.
	 * ----------------------------------------------------------------------------------
	 */
	public int wyszukajPlyte( String pWykonawca, String pTytul, PlytaGatunek pGatunek, int pRok ) {
		
		int 	pozycja = plyty.size()-1;
		boolean	jest	= false;
		
		while (!jest && pozycja >= 0 ) {
			if( plyty.get(pozycja).tytul().equals(pTytul ) && plyty.get(pozycja).wykonawca().equals( pWykonawca ) && plyty.get(pozycja).gatunek().equals( pGatunek ) && ( plyty.get(pozycja).rok() == pRok ) )
				jest = true;
			else
				pozycja--;
		}
		
		return pozycja;
	}
	
	
	public void edytujPlyte( String pWykonawca, String pTytul, PlytaGatunek pGatunek, int pRok,
							 long nowyId, String nowyWyk, String nowyTyt, PlytaGatunek nowyGat, int nowyRok ) {
		
		int nrPlyty = wyszukajPlyte( pWykonawca, pTytul, pGatunek, pRok );
		edytujPlyte( nrPlyty, nowyId, nowyWyk, nowyTyt, nowyGat, nowyRok );
	}

	
	public void edytujPlyte( int pNumerPlyty, long nowyId, String nowyWyk, String nowyTyt, PlytaGatunek nowyGat, int nowyRok ) {
		
		Plyta plyta = new Plyta(nowyId, nowyWyk, nowyTyt, nowyGat, nowyRok );
		plyty.set( pNumerPlyty , plyta );
	}
	
	
	public void usunPlyte( String pWykonawca, String pTytul, PlytaGatunek pGatunek, int pRok ) {
		
		int nrPlyty = wyszukajPlyte( pWykonawca, pTytul, pGatunek, pRok );
		usunPlyte( nrPlyty );
	}
	
	
	public void usunPlyte( int pNumerPlyty ) {
		plyty.remove( pNumerPlyty );
	}
	

	public int liczbaPlyt() {
		return plyty.size();
	}


	public Plyta dajPlyte(int nrPlyty) {
		return plyty.get(nrPlyty);
	}
	

	public List<Plyta> dajPlyty() {
		return plyty;
	}
}

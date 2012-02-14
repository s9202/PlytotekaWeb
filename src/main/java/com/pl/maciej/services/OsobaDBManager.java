package com.pl.maciej.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import com.pl.maciej.prog.Osoba;
import com.pl.maciej.prog.Plyta;

@ApplicationScoped
public class OsobaDBManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Connection 			polaczenie;
	private Statement 			polecenie;
	private PreparedStatement 	polecenieDodaniaOsoby;
	private PreparedStatement 	polecenieOdczytuOsob;
	private PreparedStatement 	polecenieUsunieciaWszystkich;
	private PreparedStatement 	polecenieUsunieciaOsoby;
	private PreparedStatement 	polecenieOdczytuOsoby;
	
	private String 				adresUrl = 	"jdbc:hsqldb:hsql://localhost/workdb";

	public OsobaDBManager() {
		
		
		try {
/*			
			InputStream	is	= null;
			Properties wlasciwosci = new Properties();
			
			try {
				
				is	= ClassLoader.getSystemResourceAsStream("com/pl/maciej/jdbc.properties");
				wlasciwosci.load( is );
			}
			catch (NullPointerException e) {
				System.out.println("blad ladowania wlasciwosci - jdbc.properties:" + e.getMessage());
			}
			catch (IOException e) {
				System.out.println("6 blad bazy danych:" + e.getMessage());
			}
			
			 // Load the HSQL Database Engine JDBC driver
			 // hsqldb.jar should be in the class path or made part of the current jar
			Driver drv = (Driver) Class.forName( "org.hsqldb.jdbcDriver" ).newInstance(); 
			DriverManager.registerDriver( drv );

			
			if ( is != null )
				polaczenie = DriverManager.getConnection( wlasciwosci.getProperty( "url" ) );
			else
				polaczenie = DriverManager.getConnection( adresUrl );
*/						
			polaczenie = DriverManager.getConnection( adresUrl );
			polecenie = polaczenie.createStatement();
			
			boolean tablicaOsobIstnieje = false;
			
			ResultSet rs = polaczenie.getMetaData().getTables(null, null, null, null);
			
			while(rs.next() && !tablicaOsobIstnieje) {
				if("osoba".equalsIgnoreCase(rs.getString("TABLE_NAME")))
					tablicaOsobIstnieje = true;
			}
						
			if(!tablicaOsobIstnieje)
			{
				polecenie.executeUpdate("" +
					"CREATE TABLE osoba(" +
					"id bigint IDENTITY PRIMARY KEY," +
					"imie varchar(20)," +
					"nazwisko varchar(20)," +
					"" +
					")");
			}
			
			polecenieDodaniaOsoby = polaczenie.prepareStatement("" +
					"INSERT INTO osoba (id, imie, nazwisko) VALUES (?, ?, ?)" +
					"");
			
			polecenieOdczytuOsob = polaczenie.prepareStatement("" +
					"SELECT * FROM osoba" +
					"");
						
			polecenieUsunieciaWszystkich = polaczenie.prepareStatement("" +
					"DELETE FROM osoba" +
					"");
			
			polecenieUsunieciaOsoby = polaczenie.prepareStatement("" +
					"DELETE FROM osoba WHERE id = ?" +
					"");
			
			polecenieOdczytuOsoby = polaczenie.prepareStatement("" +
					"SELECT * FROM osoba WHERE id = ?" +
					"");
			
		}
		catch (SQLException e) {
			System.out.println("!Blad inicjowania polaczenia z baza danych:" + e.getMessage());
		} 
		/*
		catch (ClassNotFoundException e) {
			System.out.println("!Blad ladowania sterownika jdbc:" + e.getMessage());		
		} 
		catch (InstantiationException e) {
			System.out.println("!Blad ?:" + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("!Blad ??:" + e.getMessage());
		}*/
	}
	
	public void dodajOsobe(Osoba o) {
		try {
			polecenieDodaniaOsoby.setLong(1, o.dajId());
			polecenieDodaniaOsoby.setString(2, o.dajImie());
			polecenieDodaniaOsoby.setString(3, o.dajNazwisko());
			polecenieDodaniaOsoby.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("dodajOsobe (id=" + Long.toString(o.dajId())  + ") - blad bazy danych:" + e.getMessage());
			//e.printStackTrace();
		}
	
	}
	
	public Osoba dajOsobe( long id ) {
		Osoba osoba = null;
		try {
			polecenieOdczytuOsoby.setLong(1, id);
			ResultSet rs = polecenieOdczytuOsoby.executeQuery();
			if(rs.next()) {
				osoba = new Osoba( rs.getString("imie"), rs.getString("nazwisko"), null, rs.getLong("id") );
			}
		}
		catch (SQLException e) {
			System.out.println("dajOsobe - blad bazy danych:" + e.getMessage());
		}
			
		return osoba;
	}
	
	public List<Osoba> dajWszystkieOsoby() {
		
		List<Osoba> listaOsob = new ArrayList<Osoba>();
		try {
			ResultSet rs = polecenieOdczytuOsob.executeQuery();
			while(rs.next()) {
				listaOsob.add(new Osoba(rs.getString("imie"), rs.getString("nazwisko"), null, rs.getLong("id")));
			}
		}
		catch (SQLException e) {
			System.out.println("9 blad bazy danych:" + e.getMessage());
		}
		return listaOsob;
		
	}
	
public List<Osoba> getWszystkieOsoby() {
		
		List<Osoba> listaOsob = new ArrayList<Osoba>();
		try {
			ResultSet rs = polecenieOdczytuOsob.executeQuery();
			while(rs.next()) {
				listaOsob.add(new Osoba(rs.getString("imie"), rs.getString("nazwisko"), null, rs.getLong("id")));
			}
		}
		catch (SQLException e) {
			System.out.println("9 blad bazy danych:" + e.getMessage());
		}
		return listaOsob;
		
	}
	
	public void usunOsobe( Osoba o, PlytaDBManager plytaDB ) {
		try {
			if( o != null ) {
				if( plytaDB != null ) {
					for( Plyta p : o.dajPlyty() )
						plytaDB.usunPlyte( p );
				}
				polecenieUsunieciaOsoby.setLong(1, o.dajId());
				polecenieUsunieciaOsoby.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("usunPlyte - blad bazy danych:" + e.getMessage());
	}
	}
	public void usunOsobe( Osoba o ) {
		try {
			PlytaDBManager plytaDB = new PlytaDBManager();
			if( o != null ) {
				if( plytaDB != null ) {
					for( Plyta p : plytaDB.dajWszystkiePlyty( o ) )
						plytaDB.usunPlyte( p );
				}
				polecenieUsunieciaOsoby.setLong(1, o.dajId());
				polecenieUsunieciaOsoby.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("usunOsobe z jej plytami - blad bazy danych:" + e.getMessage());
	}
	}
	
	public void usunWszystkieOsoby() {
		try {
			PlytaDBManager plyta = new PlytaDBManager();
			plyta.usunWszystkiePlyty();
			polecenieUsunieciaWszystkich.executeUpdate();
		} catch (SQLException e) {
			System.out.println("10 blad bazy danych:" + e.getMessage());
		}
	}

	
}

package com.pl.maciej.services;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import com.pl.maciej.prog.Osoba;
import com.pl.maciej.prog.Plyta;

@ApplicationScoped
public class PlytaDBManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1535097229681874721L;
	private Connection polaczenie;
	private Statement polecenie;
	private PreparedStatement polecenieDodaniaPlyty;
	private PreparedStatement polecenieOdczytuPlytOsoby;
	private PreparedStatement polecenieOdczytuPlyt;
	private PreparedStatement polecenieUsunieciaWszystkich;
	private PreparedStatement polecenieUsunieciaPlyty;
	
	private String 				adresUrl = 	"jdbc:hsqldb:hsql://localhost/workdb";
	
	public PlytaDBManager()
	{
		try {
			/*
			Properties wlasciwosci = new Properties();
			
			try {
				wlasciwosci.load(ClassLoader.getSystemResourceAsStream("com/pl/maciej/jdbc.properties"));
			}
			catch ( NullPointerException e) {
				System.out.println("1 blad jdbc.properties:" + e.getMessage());
			}
			catch (IOException e) {
				System.out.println("1 blad bazy danych:" + e.getMessage());
			}
			*/
			polaczenie = DriverManager.getConnection(adresUrl);
					
			polecenie = polaczenie.createStatement();
			boolean tablicaPlytIstnieje = false;
			
			ResultSet rs = polaczenie.getMetaData().getTables(null, null, null, null);
			
			while(rs.next() && !tablicaPlytIstnieje) {
				if("plyta".equalsIgnoreCase(rs.getString("TABLE_NAME")))
					tablicaPlytIstnieje = true;
			}
						
			if(!tablicaPlytIstnieje) {

				polecenie.executeUpdate("" +
					"CREATE TABLE plyta(" +
					"id bigint IDENTITY PRIMARY KEY," +
					"wykonawca varchar(30)," +
					"tytul varchar(50)," +
					"gatunek int," +
					"rok int," + 
					"id_osoba bigint, " + "CONSTRAINT id_osoba_fk FOREIGN KEY (id_osoba) REFERENCES osoba (id)" +
					"" +
					")");
			}
			
			polecenieDodaniaPlyty = polaczenie.prepareStatement("" +
					"INSERT INTO plyta (id, wykonawca, tytul, gatunek, rok, id_osoba) VALUES (?, ?, ?, ?, ?, ?)" +
					"");
			
			polecenieOdczytuPlytOsoby = polaczenie.prepareStatement("" +
					"SELECT * FROM plyta" +
					" WHERE id_osoba = ?" +
					"");
			
			polecenieOdczytuPlyt = polaczenie.prepareStatement("" +
					"SELECT * FROM plyta" +
					"");
			
			polecenieUsunieciaWszystkich = polaczenie.prepareStatement("" +
					"DELETE FROM plyta" +
					"");
			
			polecenieUsunieciaPlyty = polaczenie.prepareStatement("" +
					"DELETE FROM plyta WHERE id = ?" +
					"");
		}
		catch (SQLException e) {
			System.out.println("2 blad bazy danych:" + e.getMessage());
		}
	}
	
	public void dodajPlyte( Plyta p ) {
		try {
			polecenieDodaniaPlyty.setLong(1, p.dajIdPlyty());
			polecenieDodaniaPlyty.setString(2, p.wykonawca());
			polecenieDodaniaPlyty.setString(3, p.tytul());
			polecenieDodaniaPlyty.setInt(4, p.gatunekInt());
			polecenieDodaniaPlyty.setInt(5, p.rok());
			polecenieDodaniaPlyty.setLong(6, p.getId_osoby());
			polecenieDodaniaPlyty.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("3 blad bazy danych:" + e.getMessage());
		}
	}
	/*
	public void dodajKolekcjePlyt( Osoba o ) {
		for( Plyta plyta : o.dajPlyty() ) 
			dodajPlyte( plyta, o );
	}
	
	*/
	
	public List<Plyta> dajWszystkiePlyty(Osoba o) {
		
		List<Plyta> listaPlyt = new ArrayList<Plyta>();
		try {
			ResultSet rs; 
			polecenieOdczytuPlytOsoby.setLong(1, o.dajId() );
			rs = polecenieOdczytuPlytOsoby.executeQuery();
			while(rs.next()) {
				listaPlyt.add(new Plyta(rs.getLong("id"), rs.getString("wykonawca"), rs.getString("tytul"), 
				rs.getInt("gatunek"), rs.getInt("rok"), rs.getInt("id_osoba")));
			}
		}
		catch (SQLException e) {
			System.out.println("4 blad bazy danych:" + e.getMessage());
		}
		return listaPlyt;
		
	}
	
public List<Plyta> dajWszystkiePlyty() {
		
		List<Plyta> listaPlyt = new ArrayList<Plyta>();
		try {
			ResultSet rs; 
			rs = polecenieOdczytuPlyt.executeQuery();
			while(rs.next()) {
				listaPlyt.add(new Plyta(rs.getLong("id"), rs.getString("wykonawca"), rs.getString("tytul"), 
				rs.getInt("gatunek"), rs.getInt("rok"), rs.getLong("id_osoba")));
			}
		}
		catch (SQLException e) {
			System.out.println("dajwszystkieplyty blad bazy danych:" + e.getMessage());
		}
		return listaPlyt;
		
	}
	
	public void usunWszystkiePlyty() {
		try {
			polecenieUsunieciaWszystkich.executeUpdate();
		} catch (SQLException e) {
			System.out.println("5 blad bazy danych:" + e.getMessage());
		}
	}
	
	public void usunPlyte( Plyta p ) {
		try {
			polecenieUsunieciaPlyty.setLong(1, p.dajIdPlyty());
			polecenieUsunieciaPlyty.executeUpdate();
		} catch (SQLException e) {
			System.out.println("usunPlyte - blad bazy danych:" + e.getMessage());
		}
		
	}
}

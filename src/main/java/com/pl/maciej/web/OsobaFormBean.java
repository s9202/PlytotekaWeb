package com.pl.maciej.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;


import com.pl.maciej.prog.Osoba;
import com.pl.maciej.services.OsobaDBManager;
import com.pl.maciej.services.PlytaDBManager;


@SessionScoped
@Named("osobaBean")
public class OsobaFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Osoba osoba = new Osoba();
	
	
	private ListDataModel<Osoba> osoby = new ListDataModel<Osoba>();
	private ListDataModel<Osoba> plytyOsoby = new ListDataModel<Osoba>();

	@Inject
	private OsobaDBManager odb	= new OsobaDBManager();
	
	public Osoba getOsobeBean() {
		return osoba;
	}

	public void setOsobeBean(Osoba pOsoba) {
		osoba = pOsoba;
	}

	private List<Osoba> getWszystkieOsoby() {
		List<Osoba> listaOsob	= new ArrayList<Osoba>();
			listaOsob.add(new Osoba("Janko", "Walski", null, 1007 ) );
			listaOsob.add(new Osoba("Janko", "Muzykant", null, 7 ) );
			listaOsob.add(new Osoba("Janko", "Ciniak", null, 107 ) );
			listaOsob.add(new Osoba("Janko", "Buszewski", null, 15 ) );
		return listaOsob;
	}
	
	public ListDataModel<Osoba> getWszystkieOsobyBean() {
		osoby.setWrappedData(odb.getWszystkieOsoby());
		//osoby.setWrappedData(getWszystkieOsoby());
		return osoby;
	}

	// Actions
	public String dodajOsobeBean() {
		odb.dodajOsobe(osoba);
		return "showPersons";
		//return null;
	}
	
	public String pokazPlytyOsobyBean() {
		Osoba osobaZPlytami = osoby.getRowData();
		setOsoba( osobaZPlytami );
		return "showPlytyOsoby";
	}
	public ListDataModel<Osoba> getWszystkiePlytyOsobyBean() {
		PlytaDBManager pdb = new PlytaDBManager();
		plytyOsoby.setWrappedData(pdb.dajWszystkiePlyty( osoba ));
		//osoby.setWrappedData(getWszystkieOsoby());
		return plytyOsoby;
	}

	public String usunOsobeBean() {
		Osoba osobaDoUsuniecia = osoby.getRowData();
		odb.usunOsobe(osobaDoUsuniecia);
		return null;
	}

	public void getUnikalneIdOsoby(FacesContext context, UIComponent component,
			Object value) {

		Long id = (Long) value;

		for (Osoba osoba : odb.getWszystkieOsoby()) {
			if ( osoba.getId() == id ) {
				FacesMessage message = new FacesMessage(
						"Osoba z takim identyfikatorem juz istnieje w bazie.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	public Osoba getOsoba() {
		return osoba;
	}

	public void setOsoba(Osoba osoba) {
		this.osoba = osoba;
	}

	public ListDataModel<Osoba> getOsoby() {
		return osoby;
	}

	public void setOsoby(ListDataModel<Osoba> osoby) {
		this.osoby = osoby;
	}

	public OsobaDBManager getOdb() {
		return odb;
	}

	public void setOdb(OsobaDBManager odb) {
		this.odb = odb;
	}
}

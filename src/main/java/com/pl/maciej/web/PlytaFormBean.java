package com.pl.maciej.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import com.pl.maciej.prog.Plyta;
import com.pl.maciej.services.PlytaDBManager;

@SessionScoped
@Named("plytaBean")
public class PlytaFormBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Plyta plyta = new Plyta();
	//private OsobaFormBean osobaBean = new OsobaFormBean();
	
	
	private ListDataModel<Plyta> plyty = new ListDataModel<Plyta>();

	@Inject
	private PlytaDBManager pdb = new PlytaDBManager();
	
	public Plyta getPlyteBean() {
		return plyta;
	}

	public Plyta getPlyta() {
		return plyta;
	}

	public void setPlyta(Plyta plyta) {
		this.plyta = plyta;
	}

	public ListDataModel<Plyta> getPlyty() {
		return plyty;
	}

	public void setPlyty(ListDataModel<Plyta> plyty) {
		this.plyty = plyty;
	}

	public PlytaDBManager getPdb() {
		return pdb;
	}

	public void setPdb(PlytaDBManager pdb) {
		this.pdb = pdb;
	}

	public void setPlyteBean(Plyta pPlyta) {
		plyta = pPlyta;
	}

	public ListDataModel<Plyta> getWszystkiePlytyBean() {
		plyty.setWrappedData( pdb.dajWszystkiePlyty() );
		return plyty;
	}

	// Actions
	public String dodajPlyteBean() {
		pdb.dodajPlyte(plyta);
		return "showPlyty";
		//return null;
	}

	public String usunPlyteBean() {
		Plyta plytaDoUsuniecia = plyty.getRowData();
		pdb.usunPlyte(plytaDoUsuniecia);
		return null;
	}

	public void unikalneIdPlyty(FacesContext context, UIComponent component,
			Object value) {

		Long id = (Long) value;

		for (Plyta plyta : pdb.dajWszystkiePlyty()) {
			if ( plyta.dajIdPlyty() == id ) {
				FacesMessage message = new FacesMessage(
						"Plyta z takim identyfikatorem juz istnieje w bazie.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}
}

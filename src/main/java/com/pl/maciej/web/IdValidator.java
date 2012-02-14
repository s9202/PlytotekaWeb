package com.pl.maciej.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("idValidator")
public class IdValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		String id = ((Long) value).toString();
		
		if (id.length() <= 0) {
			FacesMessage message = new FacesMessage();
			message.setDetail("ID musi składać się z conajmniej 1 cyfry");
			message.setSummary("ID musi składać się z conajmniej 1 cyfry");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
}

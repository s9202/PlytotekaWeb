package com.pl.maciej.prog;

import java.io.Serializable;


public class MojWyjatek extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	private String kom;
	
	public MojWyjatek (String komunikat) {
		super(komunikat);
		kom = komunikat;
	}
	
	public String toString() {
		return kom;
	}

}

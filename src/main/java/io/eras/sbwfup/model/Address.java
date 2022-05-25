package io.eras.sbwfup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	private String street;
	private String suite;
	private String city;
	private String zipCode;
	private Geo geo;
	

}

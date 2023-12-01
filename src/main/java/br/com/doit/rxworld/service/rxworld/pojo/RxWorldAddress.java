package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RxWorldAddress {
	@JsonbProperty("AddressLine1")
	public String addressLine1;
	@JsonbProperty("AddressLine2")
	public String addressLine2;
	@JsonbProperty("City")
	public String city;
	@JsonbProperty("State")
	public String state;
	@JsonbProperty("ZipCode")
	public String zipCode;
	@JsonbProperty("Country")
	public String country;
}
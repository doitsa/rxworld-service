package br.com.doit.rxworld.service.doit.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class DOitAddress {
    public String city;
    public String country;
    public String countryCode;
    public String district;
    public String more;
    public String state;
    public String streetName;
    @JsonbProperty("streetNum")
    public String streetNumber;
    public String zipCode;
}

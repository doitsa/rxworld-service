package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class AuthenticationResponse {
    @JsonbProperty("token_type") 
    public String tokenType;
    @JsonbProperty("access_token") 
    public String accessToken;
	@JsonbProperty("expires_in") 
    public Integer expiresIn;
}

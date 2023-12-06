package br.com.doit.rxworld.service.rxworld.pojo;

import javax.ws.rs.FormParam;

public class TokenBody {
	@FormParam("title")
	  public String grantType;
	@FormParam("username")
	public String username;
	@FormParam("password")
	public String password;
	
	public TokenBody(String username, String password) {
		this.grantType = "password";
		this.username = username;
		this.password = password;
	}
}


package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class Response {
	@JsonbProperty("statusCode")
	public String statusCode;
	@JsonbProperty("statusMessage")
	public String statusMessage;
}

package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class PostedProductResponse {
	@JsonbProperty("StatusCode")
	public String statusCode;
	@JsonbProperty("StatusMessage")
	public String statusMessage;
	@JsonbProperty("Result")
	public final Object result = null;
}
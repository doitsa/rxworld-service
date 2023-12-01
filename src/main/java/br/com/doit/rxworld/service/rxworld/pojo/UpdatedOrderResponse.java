
package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class UpdatedOrderResponse {
	@JsonbProperty("StatusCode")
	public Integer statusCode;
	@JsonbProperty("StatusMessage")
	public String statusMessage;
	@JsonbProperty("Result")
	public final Object result = "";
	
	public Boolean isSuccessful() {
		return statusCode.equals(200);
	}
}
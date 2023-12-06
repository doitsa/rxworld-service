
package br.com.doit.rxworld.service.rxworld.pojo;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

public class ProductsResponse {
	@JsonbProperty("StatusCode")
	public Integer statusCode;
	@JsonbProperty("StatusMessage")
	public String statusMessage;
	@JsonbProperty("Result")
	public List<RxWorldProduct> result;
	
	public Boolean isSuccessful() {
		return statusCode.equals(200);
	}
}
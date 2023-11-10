package br.com.doit.rxworld.service.rxworld.pojo;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

public class OrderIDsResponse {
	@JsonbProperty("StatusCode")
	public String statusCode;
	@JsonbProperty("StatusMessage")
	public String statusMessage;
	@JsonbProperty("Result")
	public List<Integer> result;
	
	public Boolean successfulRequest() {
		return "200".equals(statusCode);
	}
}
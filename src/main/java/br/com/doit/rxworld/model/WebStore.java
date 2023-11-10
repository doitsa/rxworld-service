package br.com.doit.rxworld.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity(name = "web_store")
public class WebStore extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Column(name = "client_id")
	public String clientId;

	@Column(name = "client_secret")
	public String clientSecret;

	@Column(name = "doit_url")
	public String doitUrl;

	@Column(name = "doit_web_store_id")
	public Integer doitWebStoreId;

	@Column(name = "order_criterias")
	private String orderCriterias;

	public String organization;

	@Column(name = "password")
	public String password;

	@Column(name = "rxworld_url")
	public String rxworldUrl;

	@Column(name = "username")
	public String username;

	public String authorization() {
		return "consumer_key=" + "&consumer_secret=";
	}

	public List<String> orderCriterias() {
		return Arrays.asList(orderCriterias.toLowerCase().replaceAll("\\s", "").split(","));
	}

	public void setOrderCriterias(String orderCriterias) {
		this.orderCriterias = orderCriterias;
	}
}
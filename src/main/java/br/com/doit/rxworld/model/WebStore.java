package br.com.doit.rxworld.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity(name = "web_store")
public class WebStore extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Column(name = "doit_url")
	public String doitUrl;

	@Column(name = "doit_web_store_id")
	public Integer doitWebStoreId;

	@Column(name = "order_criterias")
	private String orderCriterias;

	public String organization;

	@Column(name = "rxworld_url")
	public String rxWorldUrl;
	
	@Column(name = "username")
	public String username;
	
	@Column(name = "password")
	public String password;
	
	@Column(name = "client_id")
	public String clientId;
	
	@Column(name = "client_secret")
	public String clientSecret;
	
	@OneToOne
	@JoinColumn(name = "rxworld_config_id", referencedColumnName = "id")
    public RxWorldConfig rxWorldConfig;

	public List<String> orderCriterias() {
		return Arrays.asList(orderCriterias.toLowerCase().replaceAll("\\s", "").split(","));
	}

	public void setOrderCriterias(String orderCriterias) {
		this.orderCriterias = orderCriterias;
	}
}

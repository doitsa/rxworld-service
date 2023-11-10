package br.com.doit.rxworld.model;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity(name = "product")
public class Product extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Column(name = "doit_product_id")
	public Integer doitProductId;
	
	@Column(name = "date_created")
	public Date dateCreated;

	@Column(name = "date_updated")
	public Date dateUpdated;

	@Column(name = "sku")
	public String sku;

	@ManyToOne
	@JoinColumn(name = "web_store_id", insertable = true, updatable = true)
	public WebStore webStore;
}

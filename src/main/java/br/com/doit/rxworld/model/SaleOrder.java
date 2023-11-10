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

@Entity(name = "sale_order")
public class SaleOrder extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@Column(name = "date_created")
	public Date dateCreated;

	@Column(name = "date_updated")
	public Date dateUpdated;

	@Column(name = "rxworld_id")
	public String rxworldId;

	@Column(name = "doit_id")
	public String doitId;
	
	@ManyToOne
    @JoinColumn(name = "web_store_id", insertable = true, updatable = true)
    public WebStore webStore;
}

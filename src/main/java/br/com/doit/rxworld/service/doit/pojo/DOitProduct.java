package br.com.doit.rxworld.service.doit.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.json.bind.annotation.JsonbDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DOitProduct {
    public BigDecimal availableQuantity;
    @JsonbDateFormat("yyyy-MM-dd hh:mm:ss")
    public LocalDate batchExpirationDate;
    public String batchNumber;
    public String ndc;
    public BigDecimal price;
    public BigDecimal quantity;
    public BigDecimal salePrice;
    public Integer sequentialCode;
    public String sku;
    public String status;
}

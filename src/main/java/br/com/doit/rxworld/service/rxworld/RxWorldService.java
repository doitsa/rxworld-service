package br.com.doit.rxworld.service.rxworld;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.doit.quarkus.logging.LoggingFilter;
import br.com.doit.rxworld.service.rxworld.pojo.OrderIDsResponse;
import br.com.doit.rxworld.service.rxworld.pojo.OrderResponse;
import br.com.doit.rxworld.service.rxworld.pojo.PostedProductResponse;
import br.com.doit.rxworld.service.rxworld.pojo.ProductsResponse;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldOrder;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldProduct;
import br.com.doit.rxworld.service.rxworld.pojo.ShippingInformation;

@Path("/api")
@RegisterRestClient
@RegisterProvider(LoggingFilter.class)
@Encoded
public interface RxWorldService {
	@POST
	@Path("/products/v1/product")
	PostedProductResponse postProduct(RxWorldProduct product);
	
	@GET
	@Path("/products/v1/productsbysku")
	ProductsResponse getProduct(@QueryParam("skucode") String sku);
	
	@GET
	@Path("/orders/v1/recentorders")
	OrderIDsResponse getOrdersByCriteria(@QueryParam("status") String status);
	
	@GET
	@Path("/orders/v1/order")
	OrderResponse getOrderById(@QueryParam("ordernumber") Integer orderNumber);
	
	@PUT
	@Path("/orders/v1/updateorderstatus")
	Response updateOrderStatus(RxWorldOrder order);
	
	@POST
	@Path("/orders/v1/shippinginformation")
	Response postShippingInformation(ShippingInformation shippingInformation);
}
package br.com.doit.rxworld.service.rxworld;

import static br.com.doit.rxworld.service.rxworld.RxWorldServiceHeaderGenerator.authorizationHeaderFor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import br.com.doit.rxworld.model.WebStore;

@ApplicationScoped
public class RxWorldServiceProvider {
	public RxWorldService get(WebStore webStore) {
		URL url;

		try {
			url = new URL(webStore.rxworldUrl);
		} catch (MalformedURLException exception) {
			throw new IllegalArgumentException(exception);
		}
		

		return RestClientBuilder.newBuilder()
								.baseUrl(url)
								.register(new CustomHeaderProvider("Authorization", authorizationHeaderFor(webStore)))
								.build(RxWorldService.class);
	}

	public class CustomHeaderProvider implements ClientRequestFilter {
	    private final String name;
	    private final String value;

	    public CustomHeaderProvider(String name, String value) {
	       this.name = name;
	       this.value = value;
	    }

	    @Override
	    public void filter(ClientRequestContext requestContext) throws IOException {
	        requestContext.getHeaders().add(name, value);
	    }
	}
}
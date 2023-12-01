package br.com.doit.rxworld.service.rxworld;

import static br.com.doit.rxworld.service.rxworld.RxWorldAuthenticationServiceHeaderGenerator.authorizationHeaderFor;
import static br.com.doit.rxworld.service.rxworld.RxWorldServiceHeaderGenerator.rxWorldServiceCredentials;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedHashMap;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.RestClientDefinitionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.doit.rxworld.model.RxWorldConfig;
import br.com.doit.rxworld.model.WebStore;

@ApplicationScoped
public class RxWorldServiceProvider {
	public RxWorldService get(WebStore webStore) throws JsonProcessingException, IllegalStateException, RestClientDefinitionException {
		var rxWorldConfig = webStore.rxWorldConfig;

		if (rxWorldConfig == null || !rxWorldConfig.isTokenValid() || rxWorldConfig.clientId != webStore.clientId || rxWorldConfig.clientSecret != webStore.clientSecret) {
			var authenticationService = rxWorldAuthenticationService(webStore.rxWorldUrl, webStore.clientId, webStore.clientSecret);

			var headers = new MultivaluedHashMap<String, String>();
			headers.add("grant_type", "password");
			headers.add("username", webStore.username);
			headers.add("password", webStore.password);

			var authenticationResponse = authenticationService.getTokenResponse(headers);

			if (rxWorldConfig == null) {
				rxWorldConfig = new RxWorldConfig();
			}
			
			rxWorldConfig.clientId = webStore.clientId;
			rxWorldConfig.clientSecret = webStore.clientSecret;
			rxWorldConfig.currentBearerToken = authenticationResponse.accessToken;
			rxWorldConfig.tokenExpiresAt = LocalDateTime.now().plusSeconds(authenticationResponse.expiresIn);

			rxWorldConfig.persistAndFlush();
		}

		URL url;

		try {
			url = new URL(webStore.rxWorldUrl);
		} catch (MalformedURLException exception) {
			throw new IllegalArgumentException(exception);
		}

		return RestClientBuilder.newBuilder().baseUrl(url)
				.register(new CustomHeaderProvider("Authorization", rxWorldServiceCredentials(rxWorldConfig)))
				.build(RxWorldService.class);
	}

	public RxWorldAuthenticationService rxWorldAuthenticationService(String rxWorldUrl, String clientId, String clientSecret) {
		URL url;

		try {
			url = new URL(rxWorldUrl);
		} catch (MalformedURLException exception) {
			throw new IllegalArgumentException(exception);
		}

		return RestClientBuilder.newBuilder().baseUrl(url)
				.register(new CustomHeaderProvider("Authorization", authorizationHeaderFor(clientId, clientSecret)))
				.build(RxWorldAuthenticationService.class);
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
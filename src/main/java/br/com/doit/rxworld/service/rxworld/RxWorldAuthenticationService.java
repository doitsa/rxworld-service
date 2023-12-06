package br.com.doit.rxworld.service.rxworld;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.doit.quarkus.logging.LoggingFilter;
import br.com.doit.rxworld.service.rxworld.pojo.AuthenticationResponse;

@RegisterRestClient
@RegisterProvider(LoggingFilter.class)
public interface RxWorldAuthenticationService {
    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    AuthenticationResponse getTokenResponse(MultivaluedMap<String,String> params);
}
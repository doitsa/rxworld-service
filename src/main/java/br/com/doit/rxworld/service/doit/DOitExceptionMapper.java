package br.com.doit.rxworld.service.doit;

import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class DOitExceptionMapper implements ResponseExceptionMapper<DOitException>{
	@Override
    public DOitException toThrowable(Response response) {
        return new DOitException(response.getStatus() + " - " + response.getEntity().toString());
    }
}

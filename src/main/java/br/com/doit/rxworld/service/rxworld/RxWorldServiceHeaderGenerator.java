package br.com.doit.rxworld.service.rxworld;

import java.util.Base64;

import br.com.doit.rxworld.model.WebStore;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RxWorldServiceHeaderGenerator {
    public static String authorizationHeaderFor(WebStore webStore) {
        var base64Credentials = Base64.getEncoder().encodeToString(credentialsFor(webStore).getBytes());

        return "Basic " + base64Credentials;
    }

    private static String credentialsFor(WebStore webStore) {
    	// TODO
        return "";
    }
}
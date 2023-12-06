package br.com.doit.rxworld.service.rxworld;

import java.util.Base64;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RxWorldAuthenticationServiceHeaderGenerator {
	public static String authorizationHeaderFor(String clientId, String clientSecret) {
        var base64Credentials = Base64.getEncoder().encodeToString(credentialsFor(clientId, clientSecret).getBytes());

        return "Basic " + base64Credentials;
    }

    private static String credentialsFor(String clientId, String clientSecret) {
        return clientId + ":" + clientSecret;
    }
}
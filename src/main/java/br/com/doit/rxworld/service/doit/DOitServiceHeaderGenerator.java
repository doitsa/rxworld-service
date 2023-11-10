package br.com.doit.rxworld.service.doit;

import java.util.Base64;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DOitServiceHeaderGenerator {
    public static String authorizationHeader() {
        var base64Credentials = Base64.getEncoder().encodeToString(credentials().getBytes());

        return "Basic " + base64Credentials;
    }

    private static String credentials() {
        var config = ConfigProvider.getConfig();
        var username = config.getValue("doit.service.username", String.class);
        var password = config.getValue("doit.service.password", String.class);

        return username + ":" + password;
    }
}
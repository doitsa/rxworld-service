package br.com.doit.rxworld.service.doit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class DOitServiceProvider {
    @ConfigProperty(name = "doit.service.base-url")
    public String baseUrl;

    public DOitService get(String system) {
        URL url;

        try {
            url = new URL(this.baseUrl.replaceAll(Pattern.quote("{system}"), system));
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException(exception);
        }

        return RestClientBuilder.newBuilder()
                .baseUrl(url)
                .build(DOitService.class);
    }
}
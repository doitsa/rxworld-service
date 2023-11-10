package br.com.doit.rxworld.utils;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class WireMockUtils {
    public static int numberOfRequestsMatching(WireMockServer wireMock, String url) {
        return wireMock.countRequestsMatching(anyRequestedFor(urlEqualTo(url)).build()).getCount();
    }

    public static boolean noRequestMatching(WireMockServer wireMock, String url) {
        return numberOfRequestsMatching(wireMock, url) <= 0;
    }

    private WireMockUtils() {
        throw new UnsupportedOperationException(WireMockUtils.class.getSimpleName() + " cannot be instantiated. ");
    }
}

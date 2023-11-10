package br.com.doit.rxworld.orchestration;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.List;

public class OrchestratorProfile implements QuarkusTestProfile {
    @Override
    public List<TestResourceEntry> testResources() {
        return List.of(new TestResourceEntry(WireMockResource.class), new TestResourceEntry(SqsLocalStackResource.class));
    }
}

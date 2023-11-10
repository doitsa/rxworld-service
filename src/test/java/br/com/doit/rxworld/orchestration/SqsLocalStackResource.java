package br.com.doit.rxworld.orchestration;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

/**
 * Use this resource to start the LocalStack container before and stop it after running tests.
 * <p>
 * Usage:
 * <pre>
 * &#64;QuarkusTest
 * &#64;QuarkusTestResource(LocalStackResource.class)
 * public class MyTest {
 *    ...
 * }
 * </pre>
 * This resource overrides the SQS endpoint property automatically.
 */
public class SqsLocalStackResource implements QuarkusTestResourceLifecycleManager {
    private final DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:latest");
    private final LocalStackContainer localstack = new LocalStackContainer(localstackImage).withServices(SQS);

    @Override
    public Map<String, String> start() {
        localstack.start();

        return Map.of("quarkus.sqs.endpoint-override", localstack.getEndpointOverride(SQS).toString());
    }

    @Override
    public void stop() {
        localstack.close();
    }
}

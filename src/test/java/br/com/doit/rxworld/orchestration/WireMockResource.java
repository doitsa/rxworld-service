package br.com.doit.rxworld.orchestration;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Use this resource to start WireMock before and stop it after running tests.
 * <p>
 * Usage:
 * <pre>
 * &#64;QuarkusTest
 * &#64;QuarkusTestResource(WireMockResource.class)
 * public class MyTest {
 *    ...
 * }
 * </pre>
 * If you need access to the {@code WireMockServer} instance, just declare an attribute of that type and this resource will inject it automatically.
 * <pre>
 * &#64;QuarkusTest
 * &#64;QuarkusTestResource(WireMockResource.class)
 * public class MyTest {
 *     WireMockServer wireMock;
 * }
 * </pre>
 */
public class WireMockResource implements QuarkusTestResourceLifecycleManager {
    // We must initialize the WireMockServer once per class to avoid Apache HttpClient errors.
    // See https://stackoverflow.com/a/55919043/10586727 for more information
    private WireMockServer wireMock;

    @Override
    public Map<String, String> start() {
        wireMock = new WireMockServer(options().port(9090));

        wireMock.start();

        return Map.of();
    }

    @Override
    public void stop() {
        wireMock.stop();
    }

    @Override
    public void inject(Object testInstance) {
        var testType = testInstance.getClass();
        var fields = allDeclaredFieldsInHierarchy(testType);

        fields.filter(field -> field.getType().isAssignableFrom(WireMockServer.class))
              .forEach(field -> setFieldValue(testInstance, field, wireMock));
    }

    private Stream<Field> allDeclaredFieldsInHierarchy(Class<?> type) {
        var superType = type.getSuperclass();
        var fields = Arrays.stream(type.getDeclaredFields());

        if (superType != null) {
            var superTypeFields = allDeclaredFieldsInHierarchy(superType);

            return Stream.concat(fields, superTypeFields);
        }

        return fields;
    }

    private void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);

            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new TestInstantiationException("Error while injecting WireMock into the test instance.", e);
        }
    }
}

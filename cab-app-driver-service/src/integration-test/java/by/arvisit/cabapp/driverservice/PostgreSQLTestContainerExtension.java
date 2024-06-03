package by.arvisit.cabapp.driverservice;


import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PostgreSQLTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    }

}

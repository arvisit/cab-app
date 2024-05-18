package by.arvisit.cabapp.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CabAppApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabAppApiGatewayApplication.class, args);
    }

}

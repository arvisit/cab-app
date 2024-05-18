package by.arvisit.cabapp.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class CabAppConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabAppConfigServerApplication.class, args);
    }

}

package by.arvisit.cabapp.ridesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CabAppRidesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabAppRidesServiceApplication.class, args);
    }

}

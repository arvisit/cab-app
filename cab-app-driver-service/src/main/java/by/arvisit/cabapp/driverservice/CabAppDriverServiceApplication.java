package by.arvisit.cabapp.driverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CabAppDriverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabAppDriverServiceApplication.class, args);
    }

}

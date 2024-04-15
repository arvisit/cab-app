package by.arvisit.cabapp.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CabAppPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabAppPaymentServiceApplication.class, args);
    }

}

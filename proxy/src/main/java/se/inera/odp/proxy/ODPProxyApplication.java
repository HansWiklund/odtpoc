package se.inera.odp.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"se.inera.odp.proxy"})
public class ODPProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ODPProxyApplication.class, args);
    }

}

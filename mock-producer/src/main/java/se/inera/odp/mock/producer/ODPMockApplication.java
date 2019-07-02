package se.inera.odp.mock.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"se.inera.odp.mock.producer", "se.inera.odp.mock.producer.*", "se.inera.odp.core.*"})
public class ODPMockApplication {

 
    public static void main(String[] args) {
        SpringApplication.run(ODPMockApplication.class, args);
    }
    
}

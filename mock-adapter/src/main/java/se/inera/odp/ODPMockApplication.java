package se.inera.odp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"se.inera.odp", "se.inera.odp.*", "se.inera.odp.core.*"})
public class ODPMockApplication {

 
    public static void main(String[] args) {
        SpringApplication.run(ODPMockApplication.class, args);
    }
    
}

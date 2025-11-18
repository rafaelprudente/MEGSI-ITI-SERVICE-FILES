package pt.iti.umdrive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"pt.megsi", "pt.iti"})
@EntityScan(basePackages = {"pt.megsi.fwk.entities", "pt.iti.umdrive.persistence.entities"})
@EnableJpaRepositories(basePackages = {"pt.megsi.fwk.repositories", "pt.iti.umdrive.persistence.repositories"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

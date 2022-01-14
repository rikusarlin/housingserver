package fi.rikusarlin.housingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("fi.rikusarlin.housingserver.data")
@ComponentScan(basePackages = {"fi.rikusarlin.housingserver"})
@EnableJpaRepositories(basePackageClasses=fi.rikusarlin.housingserver.repository.HouseholdMemberRepository.class)
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
    

}

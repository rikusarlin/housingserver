package fi.rikusarlin.housingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
@EntityScan("fi.rikusarlin.housingserver.data")
@ComponentScan(basePackages = {"fi.rikusarlin.housingserver.bottomup.controller"})
@EnableJpaRepositories(basePackageClasses=fi.rikusarlin.housingserver.repository.HouseholdMemberRepository.class)
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Housing server API").description(
                        "This is a simple housing benefit service API"));
    }

}

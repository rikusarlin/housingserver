package fi.rikusarlin.housingserver.topdown.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @GetMapping(path="/housing-api-docs", produces = "application/json")
    public @ResponseBody String getApiDocument() {
		File resource;
		String text = "";
		try {
			resource = new ClassPathResource("openapi.json").getFile();
			text = new String(Files.readAllBytes(resource.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return text;
    }

    @GetMapping(path="/housing-api-docs/swagger-config", produces = "application/json")
    public @ResponseBody String getSwaggerConfig() {
        return  "{\"configUrl\":\"/swagger-config\",\"url\":\"/housing-api-docs\",\"validatorUrl\":\"\"}";
    }
}
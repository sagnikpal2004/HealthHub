package nic.HealthHub.config;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InterpretationConfig {
    private static final String RANGES_DB = "src/main/resources/InterpretationRanges.json";

    @Bean
    public JSONObject interpretationRanges() throws IOException {
        String JSONString = Files.readString(Paths.get(RANGES_DB));
        return new JSONObject(JSONString);
    }
}

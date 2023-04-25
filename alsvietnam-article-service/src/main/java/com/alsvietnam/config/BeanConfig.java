package com.alsvietnam.config;

import com.alsvietnam.properties.GoogleFirebaseProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Duc_Huy
 * Date: 8/24/2022
 * Time: 10:49 AM
 */

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final GoogleFirebaseProperties googleFirebaseProperties;

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        //sign up credential with private key
        GoogleCredentials credentials = GoogleCredentials.fromStream(Files.newInputStream(
                Paths.get(googleFirebaseProperties.getPrivateKey())));

        //init firebase options
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket(googleFirebaseProperties.getBucket())
                .build();

        //init firesbase app
        return FirebaseApp.initializeApp(options);
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Tika getTika() {
        return new Tika();
    }
}

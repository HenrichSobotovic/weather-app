package com.example.weatherapp; // Balík, kde je aplikácia umiestnená

import org.springframework.boot.SpringApplication; // Trieda na spustenie Spring Boot aplikácie
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotácia na označenie hlavnej Spring Boot triedy
import org.springframework.context.annotation.Bean; // Anotácia na definovanie Beanov(objekt, ktorý Spring spravuje) v konfiguračných triedach
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Trieda na konfiguráciu CORS (komunikacia medzi backendom a frontendom)
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Rozhranie na konfiguráciu MVC(vzor, ktorý Spring používa na organizovanie webovej aplikácie) nastavení

@SpringBootApplication // Označuje, že je to hlavná trieda Spring Boot aplikácie (kombinuje viac anotácií)
public class WeatherAppApplication {

    // Hlavná metóda, ktorá spustí Spring Boot aplikáciu
    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }

    // Definícia Bean-u pre konfiguráciu CORS (Cross-Origin Resource Sharing)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            // prepíše metódy na pridanie CORS pravidiel
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Pravidlo pre všetky endpointy
                        .allowedOrigins("http://localhost:3000") // moj frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Povolené HTTP metódy
                        .allowedHeaders("*") // Povolené všetky HTTP hlavičky
                        .allowCredentials(true); // Povolenie posielania cookies alebo autorizácie
            }
        };
    }
}

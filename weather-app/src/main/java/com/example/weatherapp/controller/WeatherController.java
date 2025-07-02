package com.example.weatherapp.controller; // Balík, kde sa controller nachádza

import org.springframework.web.bind.annotation.GetMapping; // Import anotácie (anotacie-hovoria frameworku, čo má s tým kódom robiť) pre HTTP GET
import org.springframework.web.bind.annotation.RequestMapping; // Import anotácie pre základnú URL cestu
import org.springframework.web.bind.annotation.RestController; // Import anotácie pre REST kontrolér(spracovava HTTP poziadavku a posiela vo formate JSON)
import org.springframework.web.client.RestTemplate; // Import na volanie HTTP API

import java.util.*; // Import všetkých utilitárnych tried (List, Map, atď. - obsahujú pomocné metódy a funkcie)

@RestController // Označuje triedu ako REST API kontrolér
@RequestMapping("/api/weather") // Základná cesta pre tento kontrolér
public class WeatherController {

    // moj API kluc
    private final String API_KEY = "a4f5344d790cd947f5c0f051a2618e53";
    // Mesto
    private final String CITY = "Bratislava";
    // Jednotka merania
    private final String UNITS = "metric";
    // Jazyk
    private final String LANG = "sk";

    // HTTP GET endpoint na /api/weather (spracováva HTTP GET požiadavky)
    @GetMapping
    public Map<String, Object> getWeather() {
        // Objekt na posielanie HTTP požiadaviek
        RestTemplate restTemplate = new RestTemplate();

        // URL pre aktuálne počasie
        String currentUrl = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s&lang=%s",
                CITY, API_KEY, UNITS, LANG
        );

        // URL pre predpoveď počasia na 5 dní
        String forecastUrl = String.format(
                "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=%s&lang=%s",
                CITY, API_KEY, UNITS, LANG
        );

        // Volanie OpenWeatherMap API na získanie aktuálneho počasia, výsledok mapujem na Mapu
        Map<String, Object> currentWeather = restTemplate.getForObject(currentUrl, Map.class);
        // Volanie OpenWeatherMap API na získanie predpovede počasia, tiež do Mapy
        Map<String, Object> forecastData = restTemplate.getForObject(forecastUrl, Map.class);

        // Z predpovedných dát získavame zoznam jednotlivých záznamov s časom a teplotou
        List<Map<String, Object>> forecastList = (List<Map<String, Object>>) forecastData.get("list");

        // Pripravím si zoznam, kde budeme ukladať dátumy a teploty na graf
        List<Map<String, Object>> history = new ArrayList<>();

        // Prejdem každý záznam z predpovede a vytiahnem z neho čas a teplotu
        for (Map<String, Object> item : forecastList) {
            // Čas v textovej forme, "2025-07-01 15:00:00"
            String dt = (String) item.get("dt_txt");
            // Z hlavnej časti (main) získam teplotu
            Map<String, Object> main = (Map<String, Object>) item.get("main");

            // Teplota môže byť buď Integer(cele cislo) alebo Double(desatinne), takže to tu konvertnem na Double
            Double temp = main.get("temp") instanceof Integer
                    ? ((Integer) main.get("temp")).doubleValue()
                    : (Double) main.get("temp");

            // Vytvorím nový záznam s dátumom a teplotou
            Map<String, Object> entry = new HashMap<>();
            entry.put("dt", dt);
            entry.put("temp", temp);
            // Pridáme do zoznamu history
            history.add(entry);
        }

        // Pripravím mapu odpovede, kde dáme aktuálne počasie a zoznam dát pre graf
        Map<String, Object> response = new HashMap<>();
        response.put("current", currentWeather);
        response.put("history", history);

        // Vrátim túto mapu ako JSON odpoveď
        return response;
    }
}


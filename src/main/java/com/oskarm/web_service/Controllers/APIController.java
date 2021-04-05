package com.oskarm.web_service.Controllers;

import com.oskarm.web_service.WebServiceApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * A controller of the API. This is where the REST clients are, that send any requests to the APIs.
 * 
 */

@RestController
public class APIController {

    /**
     * 
     * The main API call that is used. Incorporates all the other APIs
     * 
     */
    @RequestMapping(value = "/apicall/{city}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String apiCall(@PathVariable String city) {
        try {
            // Responses to the API calls that do not require any extra information
            String customResponse = customApiCall(city);
            String weatherResponse = weatherApiCall(city);

            // Load the responses correctly/
            JSONArray customJSON = new JSONArray(customResponse);
            JSONObject weatherJSON = new JSONObject(weatherResponse);

            // For each response from my API get the landmark and call Wikipedia's API to get information and photo.
            for (int i = 0; i < customJSON.length(); i++){
                JSONObject landmark = customJSON.getJSONObject(i);
                JSONObject wikipediaJSON = new JSONObject(wikipediaApiCall(landmark.getString("name")));
                landmark.put("wikipedia", wikipediaJSON);
            }
            // Create the final response JSON.
            JSONObject response = new JSONObject();

            // Add all the relevant information to the JSON.
            response.put("weather", weatherJSON);
            response.put("landmark", customJSON);

            // Return the JSON and let the client configure it.
            return response.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * 
     * The wikipedia API REST client.
     * 
     */
    @RequestMapping(value = "/apicall/wikipedia/{landmark}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String wikipediaApiCall(@PathVariable String landmark) {
        // start timer
        long startTime = System.nanoTime();

        // Initialise rest request
        RestTemplate restTemplate = new RestTemplate();

        // Create any headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-cache");
        headers.set("Connection", "keep-alive");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Build the URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://en.wikipedia.org/api/rest_v1/page/summary/"+landmark.replace(" ", "_"));

        // Send the request
        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        //End of timer and print results
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        WebServiceApplication.log.info("WIKIPEDIA TIME TAKEN IN SECONDS:" + (duration/ Math.pow(10, 9)));

        return stringResponseEntity.getBody();
    }

    /**
     * 
     * The landmark API REST client.
     * 
     */
    @RequestMapping(value = "/apicall/custom/{city}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String customApiCall(@PathVariable String city) {
        // start timer
        long startTime = System.nanoTime();

        // Initialise the request
        RestTemplate restTemplate = new RestTemplate();

        //Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Build the URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9999/landmark/city/"+city)
                .queryParam("format", "json");

        // Send the request
        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        // end timer and print results
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        WebServiceApplication.log.info("CUSTOM TIME TAKEN IN SECONDS:" + (duration/ Math.pow(10, 9)));
        return stringResponseEntity.getBody();
    }

    /**
     * 
     * The openweathermap API REST client.
     * 
     */
    @RequestMapping(value = "/apicall/weather/{city}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String weatherApiCall(@PathVariable String city) {
        // timer
        long startTime = System.nanoTime();
        // Start creating a request
        RestTemplate restTemplate = new RestTemplate();

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Build the request
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://api.openweathermap.org/data/2.5/weather")
                .queryParam("q", city+",uk")
                .queryParam("units", "metric")
                .queryParam("APPID", WebServiceApplication.weatherAPIKey);

        // Send the request
        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        // End timer and print results
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        WebServiceApplication.log.info("WEATHER TIME TAKEN IN SECONDS:" + (duration/ Math.pow(10, 9)));

        return stringResponseEntity.getBody();
    }
}

package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";
   

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                printMenu();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        getWeather();
                        break;
                    case 2:
                        getWindSpeed();
                        break;
                    case 3:
                        getPressure();
                        break;
                    case 0:
                        System.out.println("Exiting the program. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
    }

    private static void getWeather() throws IOException, ParseException {
        JSONObject firstHourData = getFirstHourData();
        JSONObject weather = (JSONObject) ((JSONArray) firstHourData.get("weather")).get(0);
        String mainWeather = (String) weather.get("main");
        System.out.println("Weather in London: " + mainWeather);
    }

    private static void getWindSpeed() throws IOException, ParseException {
        JSONObject firstHourData = getFirstHourData();
        JSONObject windData = (JSONObject) firstHourData.get("wind");
        if (windData != null) {
            double windSpeed = (double) windData.get("speed");
            System.out.println("Wind Speed in London: " + windSpeed + " m/s");
        } else {
            System.out.println("Wind speed data not available.");
        }
    }

    private static void getPressure() throws IOException, ParseException {
        JSONObject firstHourData = getFirstHourData();
        JSONObject mainData = (JSONObject) firstHourData.get("main");
        if (mainData != null) {
            double pressure = (double) mainData.get("pressure");
            System.out.println("Pressure in London: " + pressure + " hPa");
        } else {
            System.out.println("Pressure data not available.");
        }
    }

    private static JSONObject getFirstHourData() throws IOException, ParseException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
        JSONArray weatherData = (JSONArray) jsonResponse.get("list");
        return (JSONObject) weatherData.get(0);
    }
}

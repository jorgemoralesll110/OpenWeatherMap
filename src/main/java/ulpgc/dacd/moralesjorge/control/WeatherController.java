package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class WeatherController {

    private final OpenWeatherMapProvider weatherProvider;
    private final SQLiteWeatherStore weatherStore;

    public WeatherController() {
        this.weatherProvider = new OpenWeatherMapProvider();
        this.weatherStore = new SQLiteWeatherStore();
    }

    public void execute() {
        List<Location> locations = locationsFromFile("C:\\Users\\tomas\\Desktop\\DACD\\OpenWeatherMap\\src\\main\\resources\\locations.csv");

        for (Location location : locations) {
            try {
                Instant timestamp = Instant.now();
                String APIResponse = weatherProvider.getWeather(location, timestamp);
                List<Weather> weatherList = parseAndSaveData(location, timestamp, APIResponse);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<Location> locationsFromFile(String path) {
        List<Location> locations = new ArrayList<>();

        try (BufferedReader file = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] data = line.split(",");
                String island = data[0];
                double latitude = Double.parseDouble(data[1]);
                double longitude = Double.parseDouble(data[2]);

                Location location = new Location(island, latitude, longitude);
                locations.add(location);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not Found: " + path);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error reading the File: " + path);
            e.printStackTrace();
        }
        return locations;
    }

    private List<Weather> parseAndSaveData(Location location, Instant timestamp, String apiResponse) throws ParseException {
        List<Weather> weatherList = weatherProvider.parseWeatherData(apiResponse);

        for (Weather weather : weatherList) {
            try {
                weatherStore.saveWeather(location, timestamp, weather);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return weatherList;
    }
}
package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;

import java.io.*;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class WeatherController {

    private final OpenWeatherMapProvider weatherProvider;
    private final SQLiteWeatherStore weatherStore;

    public WeatherController() {
        this.weatherProvider = new OpenWeatherMapProvider();
        this.weatherStore = new SQLiteWeatherStore();
        scheduleTask();
    }

    public void execute() {
        List<Location> locations = locationsFromFile("locations.csv");

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

    public static List<Location> locationsFromFile(String filename) {
        List<Location> locations = new ArrayList<>();

        ClassLoader classLoader = WeatherController.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        try (BufferedReader file = new BufferedReader(new BufferedReader((new InputStreamReader(inputStream))))) {
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
            System.err.println("Error: File not Found: " + filename);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error reading the File: " + filename);
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

    private void scheduleTask() {
        Timer timer = new Timer();

        long delay = 0;
        long period = 6*3600*1000;

        TimerTask task = new TimerTask() {
            public void run() {
                execute();
            }
        };
        timer.scheduleAtFixedRate(task, delay, period);
    }
}

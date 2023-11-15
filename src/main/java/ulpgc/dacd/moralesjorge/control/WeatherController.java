package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class WeatherController {
    private Location location;
    private int days;
    private WeatherProvider provider;
    private WeatherStore store;

    List<Location> locations = locationsFromFile("locations.csv");

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }

    public void executePeriodically() {
        Timer timer = new Timer();
        long delay = 0;
        long period = 6*3600*1000;

        timer.scheduleAtFixedRate(new Timer() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < days; i++) {
                        LocalDate date = LocalDate.now().plusDays(i);
                        Weather weather = provider.getWeather(location.getLatitude(), location.setLatitude());
                        store.saveWeather(location, date, weather);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }


    public void execute() {

    }
}

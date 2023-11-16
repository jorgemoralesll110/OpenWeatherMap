package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class WeatherController {
    private Location location;
    private int days;
    private WeatherProvider provider;
    private WeatherStore store;

    List<Location> locations = locationsFromFile("C:\\Users\\tomas\\Desktop\\DACD\\OpenWeatherMap\\src\\main\\resources\\locations.csv");

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
            System.err.println("Error: No se encontró el archivo: " + path);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + path);
            e.printStackTrace();
        }
        return locations;
    }



    public void execute() {

    }
}

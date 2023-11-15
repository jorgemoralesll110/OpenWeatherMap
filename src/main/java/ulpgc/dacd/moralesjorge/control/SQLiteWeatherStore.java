package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;

import java.sql.*;
import java.time.Instant;

public class SQLiteWeatherStore implements WeatherStore {

    private static final String DATABASE_URL = "jdbc:sqlite:OpenWeatherMap.db";

    public SQLiteWeatherStore() {
        createDatabase();
    }

    private void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String createSQL = "CREATE TABLE IF NOT EXISTS Weather (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "temperature REAL," +
                    "humidity REAL," +
                    "precipitation REAL," +
                    "wind_speed REAL," +
                    "clouds REAL," +
                    "location TEXT," +
                    "time TEXT)";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createSQL);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la base de datos", e);
        }
    }

    @Override
    public void saveWeather(Location location, Instant timestamp, Weather weather) throws Exception {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)){
            String insertSQL = "INSERT INTO Weather (temperature, humidity, precipitation, wind_speed, clouds, location, time) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                statement.setDouble(1, weather.getTemperature());
                statement.setDouble(2, weather.getHumidity());
                statement.setDouble(3, weather.getRain());
                statement.setDouble(4, weather.getWind());
                statement.setDouble(5, weather.getClouds());
                statement.setString(6, weather.getLocation().toString());
                statement.setString(7, weather.getTimestamp());

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar datos en la base de datos", e);
        }

    }
}

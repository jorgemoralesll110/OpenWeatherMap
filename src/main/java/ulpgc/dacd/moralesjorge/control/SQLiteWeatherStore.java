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
            System.out.println("Connected to the database");
            crateTable(connection, "LaGraciosa");
            crateTable(connection, "Lanzarote");
            crateTable(connection, "Fuerteventura");
            crateTable(connection, "GranCanaria");
            crateTable(connection, "Tenerife");
            crateTable(connection, "LaGomera");
            crateTable(connection, "LaPalma");
            crateTable(connection, "ElHierro");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error Creating the DataBase", e);
        }
    }

    private void crateTable(Connection connection, String islandName) {
            String createSQL = "CREATE TABLE IF NOT EXISTS " + islandName + "_Weather (" +
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }


    @Override
    public void saveWeather(Location location, Instant timestamp, Weather weather) {
        String islandName = location.getIsland();
        String tableName = islandName + "_Weather";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)){
            connection.setAutoCommit(false);
            String insertSQL = "INSERT INTO " + tableName + " (temperature, humidity, precipitation, wind_speed, clouds, location, time) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                statement.setDouble(1, weather.getTemperature());
                statement.setDouble(2, weather.getHumidity());
                statement.setDouble(3, weather.getRain());
                statement.setDouble(4, weather.getWind());
                statement.setDouble(5, weather.getClouds());
                statement.setString(6, weather.getLocation().toString());
                statement.setString(7, weather.getTimestamp());

                statement.executeUpdate();
                connection.commit();
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting data into the database", e);
        }

    }
}

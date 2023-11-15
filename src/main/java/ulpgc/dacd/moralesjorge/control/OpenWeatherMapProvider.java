package ulpgc.dacd.moralesjorge.control;
import com.google.gson.*;
import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

public class OpenWeatherMapProvider implements WeatherProvider {
    private static final String TEMPLATE_URL = "https://api.openweathermap.org/data/2.5/forecast?";
    public static final String API_KEY = "44cbf52007efea1adb20817923cf5c15\n";
    private List<Weather> weatherList;

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void getWeather(Location location, Instant timestamp) throws IOException {
        try {
            String APU_URL = String.format("%s?q=%s&appid=%s", TEMPLATE_URL, location, API_KEY);
            URL url = new URL(APU_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            reader.close();

            JsonElement jsonResponse = JsonParser.parseString(response.toString());
            JsonObject main = jsonResponse.getAsJsonObject().getAsJsonObject("main");
            double temperature = main.getAsJsonPrimitive("temp").getAsDouble();
            double humidity = main.getAsJsonPrimitive("humidity").getAsDouble();
            double wind_speed = jsonResponse.getAsJsonObject().getAsJsonObject("wind").get("speed").getAsDouble();
            double clouds = jsonResponse.getAsJsonObject().getAsJsonObject("clouds").get("all").getAsDouble();
            double precipitation = main.getAsJsonPrimitive("pop").getAsDouble();

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String time = dateFormat.format(currentTime);

            Location location_api = new Location("Gran Canaria", 28.12, -15.43);

            Weather weather = new Weather(temperature, humidity, precipitation, wind_speed, clouds, location_api, time);
            weatherList.add(weather);

            } catch (IOException | JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
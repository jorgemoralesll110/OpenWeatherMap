package ulpgc.dacd.moralesjorge.control;

import com.google.gson.*;
import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class OpenWeatherMapProvider implements WeatherProvider {
    private static final String TEMPLATE_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String API_KEY = "44cbf52007efea1adb20817923cf5c15";
    private List<Weather> weatherList;

    public OpenWeatherMapProvider() {
        this.weatherList = new ArrayList<>();
    }

    public String getWeather(Location location, Instant timestamp) {
        try {
            String API_URL = buildAPIURL(location);
            String jsonResponse = makeAPIRequest(API_URL);

            List<Weather> weather = parseWeatherData(jsonResponse);
            weatherList.addAll(weather);
            return jsonResponse;

        } catch (IOException | JsonSyntaxException e) {
            handleException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String buildAPIURL(Location location) {
        return String.format("%s?lat=%s&lon=%s&appid=%s", TEMPLATE_URL, location.getLatitude(), location.getLongitude(), API_KEY);
    }

    private String makeAPIRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    List<Weather> parseWeatherData(String jsonResponse) throws ParseException {
        List<Weather> weatherList = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject responseObject = parser.parse(jsonResponse).getAsJsonObject();

        JsonArray list = responseObject.getAsJsonArray("list");

        for (int i = 0; i < list.size(); i++) {
            JsonObject weatherData = list.get(i).getAsJsonObject();
            String dataTimeString = weatherData.get("dt_txt").getAsString();

            if (isMidday(dataTimeString) && isWithinNext7Days(dataTimeString)) {
                JsonObject main = weatherData.getAsJsonObject("main");
                double temperature = main.getAsJsonPrimitive("temp").getAsDouble();
                double humidity = main.getAsJsonPrimitive("humidity").getAsDouble();
                double wind_speed = weatherData.getAsJsonObject().getAsJsonObject("wind").get("speed").getAsDouble();
                double clouds = weatherData.getAsJsonObject().getAsJsonObject("clouds").get("all").getAsDouble();
                JsonElement popElement = main.get("pop");
                double precipitation = (popElement != null && !popElement.isJsonNull()) ? popElement.getAsDouble() : 0.0;

                Location location_api = new Location("GranCanaria", 28.12, -15.43);

                Weather weather = new Weather(temperature, humidity,precipitation, wind_speed, clouds, location_api, dataTimeString);
                weatherList.add(weather);
            }
        }
        return weatherList;
    }

    private boolean isMidday(String dateTimeString) {
        return dateTimeString.endsWith("12:00:00");
    }

    private boolean isWithinNext7Days(String dateTimeString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Date weatherDate = dateFormat.parse(dateTimeString);

        long differenceInMilliseconds = weatherDate.getTime() - currentDate.getTime();
        long differenceInDays = differenceInMilliseconds / (24 * 60 * 60 * 1000);

        return differenceInDays >= 0 && differenceInDays <= 7;
    }

    private void handleException(Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}

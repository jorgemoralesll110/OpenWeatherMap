package ulpgc.dacd.moralesjorge.control;

import ulpgc.dacd.moralesjorge.model.Location;
import ulpgc.dacd.moralesjorge.model.Weather;

import java.time.Instant;

public interface WeatherStore {
    void saveWeather(Location location, Instant timestamp, Weather weather) throws Exception;
}
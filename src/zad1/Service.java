/**
 * @author Zaborowski Mateusz S19101
 */

package zad1;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {
    private String country;
    private String countryCode;
    private String city;
    private String rateFor;
    private String weatherJSON;
    private double rate1;
    private double rate2;

    public Service(String country) {
        setCountry(country);
    }

    public void setCountry(String country) {
        this.country = country;
        Map<String, String> countries = new HashMap<>();
        Locale.setDefault(new Locale("en"));
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        countryCode = countries.get(country);
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getRateFor() {
        return rateFor;
    }

    public String getWeatherJSON() {
        return weatherJSON;
    }

    public double getRate1() {
        return rate1;
    }

    public double getRate2() {
        return rate2;
    }

    private JSONObject getData(URLConnection connection) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();
        return new JSONObject(result.toString());
    }

    public String getWeather(String city) {
        this.city = city;
        String response;
        try {
            URLConnection connection = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + countryCode + "&appid=9ddd12eaeb3413dfba6b7bfb8a516c77").openConnection();
            JSONObject jsonResponse = getData(connection);
            response = jsonResponse.toString();
        } catch (IOException e) {
            response = e.getMessage();
        }
        weatherJSON = response;
        return response;
    }

    public double getRateFor(String currency) {
        rateFor = currency;
        double response;
        try {
            String localCurrency = Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
            if (localCurrency.equals(currency)) {
                rate1 = 1;
                return 1;
            }
            URLConnection connection = new URL("https://api.exchangeratesapi.io/latest?base=" + localCurrency + "&symbols=" + currency).openConnection();
            JSONObject jsonResponse = getData(connection);
            response = jsonResponse.getJSONObject("rates").getDouble(currency);
        } catch (Exception e) {
            weatherJSON = "Bad country name!!!";
            response = 0;
        }
        rate1 = response;
        return response;
    }

    public double getNBPRate() {
        double response;
        try {
            String localCurrency = Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
            if (localCurrency.equals("PLN")) {
                rate2 = 1;
                return 1;
            }
            URLConnection connection = new URL("http://api.nbp.pl/api/exchangerates/rates/a/" + localCurrency + "/?format=json").openConnection();
            JSONObject jsonResponse = getData(connection);
            response = jsonResponse.getJSONArray("rates").getJSONObject(0).getDouble("mid");
        } catch (Exception e) {
            weatherJSON = "Bad country name!!!";
            response = 0;
        }
        rate2 = response;
        return response;
    }
}

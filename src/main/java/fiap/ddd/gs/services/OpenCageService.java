package fiap.ddd.gs.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;

public class OpenCageService {

    private static final String OPENCAGE_API_KEY = "e203f061539e4b2b91d48e48a8da8399";
    private static final String OPENCAGE_API_URL = "https://api.opencagedata.com/geocode/v1/json?key=" + OPENCAGE_API_KEY + "&q=";

    public Coordenadas obterCoordenadas(String localizacaoGeografica) {
        try {
            String encodedLocation = URLEncoder.encode(localizacaoGeografica, StandardCharsets.UTF_8.toString());
            URL url = new URL(OPENCAGE_API_URL + encodedLocation);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            Gson gson = new Gson();
            OpenCageResponse response = gson.fromJson(content.toString(), OpenCageResponse.class);
            double latitude = response.results[0].geometry.lat;
            double longitude = response.results[0].geometry.lng;

            return new Coordenadas(latitude, longitude);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter coordenadas: " + e.getMessage(), e);
        }
    }

    public static class Coordenadas {
        private double latitude;
        private double longitude;

        public Coordenadas(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    private static class OpenCageResponse {
        Result[] results;

        private static class Result {
            Geometry geometry;

            private static class Geometry {
                double lat;
                double lng;
            }
        }
    }
}

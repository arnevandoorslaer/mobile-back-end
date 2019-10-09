package be.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class JSONReader {
    static JSONObject getJSON(String searchTerm, String key, int maxResults) {
        try {
            searchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("fout bij encoden url -- zou niet moeten gebeuren normaal" + e.getMessage());
        }
        String url = "https://www.googleapis.com/youtube/v3/search?key=" + key + "&part=snippet&q=" + searchTerm + "&maxResults=" + maxResults;
        StringBuilder html = new StringBuilder();
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                html.append(inputLine).append("\n");
            in.close();
            return new JSONObject(html.toString());
        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package be.model;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchResultNoAPI {

    private ArrayList<Song> songs;

    public SearchResultNoAPI() {
    }

    public SearchResultNoAPI(String searchTerm) {
        songs = new ArrayList<>();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchTerm = "zoutelande";
        }
        try {
            if(searchTerm.contains("&")){
                String searchTerms[] = searchTerm.split("&");
                for (String sT:searchTerms) {
                    getContent(sT,1);
                }
            }
            else{
                getContent(searchTerm,5);
            }

        } catch (Exception e) {
            //do nothing
        }

    }

    private void getContent(String searchTerm,int count) throws JSONException {
        try {
            searchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("fout bij encoden url -- zou niet moeten gebeuren normaal" + e.getMessage());
        }
        String url = "https://www.youtube.com/results?search_query=" + searchTerm + "&sp=EgIQAQ%253D%253D";
        StringBuilder html = new StringBuilder();
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                html.append(inputLine).append("\n");
            in.close();

            String[] ids = html.toString().split("data-context-item-id=\"");
            String[] titles = html.toString().split("class=\"yt-lockup-title \"");
            for (int i = 1; i < count+1; i++) {
                String id = ids[i].substring(0, ids[i].indexOf("\""));
                int titleBegin = titles[i].indexOf("title=\"") + 7;
                String titleBeginTemp = titles[i].substring(titleBegin);
                String temp = titleBeginTemp.substring(0, titleBeginTemp.indexOf("\""));
                String title;
                String artist;
                if (temp.split(" - ", 2).length > 1) {
                    title = temp.split(" - ", 2)[1];
                } else {
                    title = temp;
                }
                if (temp.split(" - ", 2).length > 1) {
                    artist = temp.split(" - ", 2)[0];
                } else {
                    artist = "Unknown Artist";
                }
                songs.add(new Song(id, title, artist));

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Song> getSearchResults() {
        return songs;
    }
}

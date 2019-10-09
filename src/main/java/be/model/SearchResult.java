package be.model;


import be.Secret;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class SearchResult {
    private ArrayList<Song> songs;
    private Secret secret;

    public SearchResult() {
    }

    public SearchResult(String searchTerm) {
        secret = new Secret();
        songs = new ArrayList<>();

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchTerm = "zoutelande";
        }
        try {
            getContent(searchTerm);
        } catch (Exception e) {
            //do nothing
        }

    }

    private void getContent(String searchTerm) throws JSONException {
        String key = secret.getKey();
        if (searchTerm != null) {
            JSONArray optionList = Objects.requireNonNull(JSONReader.getJSON(searchTerm, key, 5)).getJSONArray("items");
            for (int i = 0; i < optionList.length(); i++) {
                String temp = optionList.getJSONObject(i).getJSONObject("snippet").getString("title");
                String id;
                String artist;
                String title;
                try {
                    id = optionList.getJSONObject(i).getJSONObject("id").getString("videoId");
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
                } catch (Exception e) {
                    //do nothing
                }
            }
        }
    }

    public ArrayList<Song> getSearchResults() {
        return songs;
    }

}

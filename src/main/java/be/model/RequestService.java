package be.model;


import be.db.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestService {

    String key = "qwerty";
    boolean authenticated = false;

    AtomicInteger priority = new AtomicInteger();
    @Autowired
    private SongRepository songRepository;

    private HashMap<Long, ArrayList<String>> skipsPerId;

    public RequestService() {
        this.skipsPerId = new HashMap<>();
    }

    public ArrayList<Song> getSongs() {
        return (ArrayList<Song>) songRepository.findAll();
    }

    public void addSong(String id, String title, String artist) throws Exception {
        Song song = new Song(id, title, artist, priority.getAndIncrement());
        Song temp = songRepository.findSongBySongId(song.getSongId());
        if (temp != null) {
            throw new Exception("Song already queued!");
        } else {
            songRepository.save(song);
        }

    }


    public ArrayList<Song> getSearchResults(String searchTerm) {
        return new SearchResultNoAPI(searchTerm).getSearchResults();
    }


    public Song getCurrent() {
        try {
            return songRepository.findAll().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Song getNext() {
        return songRepository.findAll().get(1);

    }

    public void skipSong(Long id, String remoteAddr) {
        if (!skipsPerId.containsKey(id)) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(remoteAddr);
            skipsPerId.put(id, temp);
        }
        if (!skipsPerId.get(id).contains(remoteAddr)) {
            ArrayList<String> temp = skipsPerId.get(id);
            temp.add(remoteAddr);
            skipsPerId.put(id, temp);
        }
        if (skipsPerId.get(id).size() > 2) {
            try {
                skipSong(id);
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    public void skipSong(String remoteAddr) {
        try {
            skipSong(getCurrent().getId(), remoteAddr);
        } catch (Exception e) {
            //do nothing
        }
    }

    public void skipSong() {
        try {
            skipSong(getCurrent().getId());
        } catch (Exception e) {
            //do nothing
        }
    }

    public void skipSong(Long id) {
        songRepository.deleteById(id);
        if (skipsPerId.containsKey(id)) {
            skipsPerId.remove(id);
        }
    }

    public void changePriorityUp(String songId) {
        Song song = songRepository.findSongBySongId(songId);
        Song temp = songRepository.findSongByPriority(song.getPriority() - 1);

        if (temp != null) {
            swapPriorities(song, temp);
        }
    }

    public void changePriorityDown(String songId) {
        Song song = songRepository.findSongBySongId(songId);
        Song temp = songRepository.findSongByPriority(song.getPriority() + 1);

        if (temp != null) {
            swapPriorities(song, temp);
        }
    }

    public void swapPriorities(Song song, Song temp) {
        int songP = song.getPriority();
        int tempP = temp.getPriority();

        song.setPriority(tempP);
        temp.setPriority(songP);

        songRepository.save(song);
        songRepository.save(temp);
    }

    private int getNextPriority() {
        int temp = priority.getAndIncrement();
        if (songRepository.findSongByPriority(temp) != null) {
            return getNextPriority();
        } else {
            return temp;
        }
    }

    public boolean authenticate(String key) {
        if (this.key.equals(key)) {
            this.authenticated = true;
        } else {
            this.authenticated = false;
        }
        return this.authenticated;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }
}

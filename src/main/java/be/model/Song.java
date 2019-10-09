package be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @NotNull(message = "Id can't be null")
    private long id;
    @JsonIgnore
    @NotNull(message = "url can't be null")
    @NotEmpty(message = "url can't be empty")
    private String url;
    @NotNull(message = "id can't be null")
    @NotEmpty(message = "id can't be empty")
    private String songId;
    @NotNull(message = "title can't be null")
    @NotEmpty(message = "title can't be empty")
    private String title;
    private LocalDateTime added;
    private String artist;
    private int priority;


    private boolean skipped;

    public Song() {

    }

    public Song(String songId) {
        setSongId(songId);
    }

    public Song(String songId, String title, String artist) {
        setSongId(songId);
        setTitle(title);
        setArtist(artist);
        setUrl(songId);
        setAdded(LocalDateTime.now());
        setSkipped(false);
    }

    public Song(String songId, String title, String artist, int priority) {
        setSongId(songId);
        setTitle(title);
        setArtist(artist);
        setUrl(songId);
        setAdded(LocalDateTime.now());
        setSkipped(false);
        setPriority(priority);
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String songId) {
        this.url = "https://www.youtube.com/watch?v=" + songId;
    }

    public String getSongId() {
        return songId;
    }

    private void setSongId(String songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    private void setArtist(String artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

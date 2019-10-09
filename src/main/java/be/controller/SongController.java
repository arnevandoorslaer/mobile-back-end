package be.controller;

import be.model.RequestService;
import be.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class SongController {
    @Autowired
    RequestService service;

    public SongController() {

    }

    @PostMapping(value = "/song/add")
    public Object addSong(@RequestBody Song s) {
        try {
            service.addSong(s.getSongId(), s.getTitle(), s.getArtist());
            return getSongs();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping(value = "/song/all")
    public Object getSongs() {
        try {
            return service.getSongs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

    }


    @GetMapping(value = "/song/current")
    public Object getCurrent() {
        try {
            if (service.getCurrent() == null) {
                return new Song("none");
            } else {
                return service.getCurrent();
            }

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping(value = "/song/next")
    public Object getNext() {
        try {
            if (service.getNext() == null) {
                return new Song("none");
            } else {
                return service.getNext();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(value = "/song/search/{title}")
    public Object searchSong(@PathVariable(value = "title") String title) {
        try {
            return service.getSearchResults(title);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(value = "/song/skip/{id}/player")
    public Object skipSong(@PathVariable(value = "id") Long id) {
        if (service.isAuthenticated()) {
            service.skipSong(id);
        }
        return getSongs();
    }

    @GetMapping(value = "/song/skip/player")
    public Object skipSong() {
        service.skipSong();
        return getSongs();
    }


    @GetMapping(value = "/song/skip/{id}")
    public Object skipSong(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        service.skipSong(id, request.getRemoteAddr());
        return getSongs();
    }

    @GetMapping(value = "/song/skip")
    public Object skipSong(HttpServletRequest request) {
        service.skipSong(request.getRemoteAddr());
        return getSongs();
    }

    @GetMapping(value = "/song/up/{id}")
    public Object moveUp(@PathVariable(value = "id") String id) {
        if (service.isAuthenticated()) {
            service.changePriorityUp(id);
        }
        return getSongs();
    }

    @GetMapping(value = "/song/down/{id}")
    public Object moveDown(@PathVariable(value = "id") String id) {
        if (service.isAuthenticated()) {
            service.changePriorityDown(id);
        }
        return getSongs();
    }

    @GetMapping(value = "/authenticate/{key}")
    public Object authenticate(@PathVariable(value = "key") String key) {
        return service.authenticate(key);
    }

}

package be.controller;

import be.db.DbException;
import be.model.CleverService;
import be.model.Event;
import be.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class Controller {

    @Autowired
    CleverService service;

    public Controller() {
    }

    @GetMapping("/")
    public int root() {
        return 1;
    }

    @GetMapping("/users")
    public Object getUsers() {
        return service.getUsers();
    }

    @GetMapping("/events")
    public Object getEvents() {
        return service.getEvents();
    }

    @GetMapping("/user/get/{id}")
    public Object getUser(@PathVariable("id") long id) {
        return service.getUser(id);
    }

    @GetMapping("/event/get/{id}")
    public Object getEvent(@PathVariable("id") long id) {
        return service.getEvent(id);
    }

    @PostMapping("/user/add")
    public Object addUser(@RequestBody @Valid User user, BindingResult b, Model m) {
        List<String> errors = new ArrayList<>();
        if (b.hasErrors()) {
            for (FieldError error : b.getFieldErrors()) {
                errors.add(error.toString());
            }
            m.addAttribute("errors", errors);
            return errors;
        }
        else {
            try {
                service.addUser(user);
            } catch (DbException e) {
                errors.add(e.getMessage());
                m.addAttribute("errors", errors);
                return errors;
            }
            return service.getUser(user.getId());
        }
    }

    @PostMapping("/event/add")
    public Object addEvent(@RequestBody @Valid Event event, BindingResult b, Model m) {

        List<String> errors = new ArrayList<>();
        if (b.hasErrors()) {
            for (FieldError error : b.getFieldErrors()) {
                errors.add(error.toString());
            }
            m.addAttribute("errors", errors);
            return errors;
        }
        else {
            try {
                service.addEvent(event);
            } catch (DbException e) {
                errors.add(e.getMessage());
                m.addAttribute("errors", errors);
                return errors;
            }
            return service.getEvent(event.getId());
        }
    }

    @GetMapping("/event/{eventid}/participant")
    public Object getParticipants(@PathVariable("eventid") long eventId) {
        return service.getParticipants(eventId);
    }

    @PostMapping("/event/{eventid}/participants/add/{userid}")
    public void addParticipant(@PathVariable("eventid") long eventId, @PathVariable("userid") long userId) {
        service.addParticipant(userId, eventId);
    }
}

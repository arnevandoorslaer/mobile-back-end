
package be.controller;

import be.db.DbException;
import be.model.CleverService;
import be.model.Event;
import be.model.Payment;
import be.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
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

    @PostMapping("/user/update")
    public Object updateUser(@RequestBody @Valid User user, BindingResult b, Model m) {
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
                service.updateUser(user);
            } catch (DbException e) {
                errors.add(e.getMessage());
                m.addAttribute("errors", errors);
                return errors;
            }
            return service.getUser(user.getId());
        }
    }

    @PostMapping("/event/add")
    @ResponseStatus(HttpStatus.CREATED)
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

    @PostMapping("/event/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Object updateEvent( @RequestBody @Valid Event event, BindingResult b, Model m) {

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
                service.updateEvent(event);
            } catch (DbException e) {
                errors.add(e.getMessage());
                m.addAttribute("errors", errors);
                return errors;
            }
            return service.getEvent(event.getId());
        }
    }

    @PostMapping("/event/del/{eventid}")
    public void delEvent(@PathVariable("eventid") long eventId) {
        service.removeEvent(eventId);
    }

    @PostMapping("/user/del/{userid}")
    public void delUser(@PathVariable("userid") long userId) {
        service.removeUser(userId);
    }

    @GetMapping("/event/{eventid}/participants")
    public Object getParticipants(@PathVariable("eventid") long eventId) {
        return service.getParticipants(eventId);
    }

    @PostMapping("/event/{eventid}/participants/add/{userid}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addParticipant(@PathVariable("eventid") long eventId, @PathVariable("userid") long userId) {
        service.addParticipant(userId, eventId);
    }

    @GetMapping("/event/{eventid}/participant/{userid}/debt")
    public double getDebtOfUserFromEvent(@PathVariable("eventid") long eventId, @PathVariable("userid") long userId) {
        return service.getDebtOfUserFromEvent(userId, eventId);
    }

    @GetMapping("/event/{eventid}/participant/{userid}/due")
    public double getDueOfUserFromEvent(@PathVariable("eventid") long eventId, @PathVariable("userid") long userId) {
        return service.getDueOfUserFromEvent(userId, eventId);
    }

    @PostMapping("/payment/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Object addPayment(@RequestBody @Valid Payment payment, BindingResult b, Model m) {

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
                service.addPayment(payment);
            } catch (DbException e) {
                errors.add(e.getMessage());
                m.addAttribute("errors", errors);
                return errors;
            }
            return service.getPayment(payment.getId());
        }
    }
}


package be.controller;

import be.db.DbException;
import be.model.CleverService;
import be.model.Event;
import be.model.Payment;
import be.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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

    @PostMapping("/user/login")
    public int login(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = null;
        try {
            result = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = result.findValue("username").toString().replace("\"","");
        String hashedPassword = result.findValue("password").toString().replace("\"","");
        return service.login(username, hashedPassword);
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
    public List<User> getParticipants(@PathVariable("eventid") long eventId) {
        List<User> result = new ArrayList<>();
        for (long u : service.getEvent(eventId).getParticipants()) {
            result.add(service.getUser(u));
        }
        return result;
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

    @GetMapping("/payments")
    public List<Payment> getPayments() {
        return service.getPayments();
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

    @GetMapping("/payment/get/{paymentid}")
    public Object getPayment(@PathVariable("paymentid") long paymentid) {
        return service.getPayment(paymentid);
    }

    @GetMapping("/event/{eventid}/cost")
    public double getCostOfEvent(@PathVariable("eventid") long eventid) {
        return service.getTotalCostOfEvent(eventid);
    }

    @GetMapping("/event/{eventid}/payments")
    public List<Payment> getPaymentsFromEvent(@PathVariable("eventid") long eventid) {
        return service.getPaymentsOfEvent(eventid);
    }

    @GetMapping("/event/{eventid}/debt/{userid}")
    public double getDebtOfUser(@PathVariable("eventid") long eventid, @PathVariable("userid") long userid) {
        return service.getDebtOfUserFromEvent(userid, eventid);
    }

    @GetMapping("/event/{eventid}/profit/{userid}")
    public double getProfitOfUser(@PathVariable("eventid") long eventid, @PathVariable("userid") long userid) {
        return service.getDueOfUserFromEvent(userid, eventid);
    }
}

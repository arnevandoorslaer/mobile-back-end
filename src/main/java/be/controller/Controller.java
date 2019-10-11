package be.controller;

import be.model.CleverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //get event

    //add event

    //add user

    //get user

    //add user to event
}

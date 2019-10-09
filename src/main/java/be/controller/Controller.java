package be.controller;

import be.model.CleverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class Controller {
    @Autowired
    CleverService service;

    public Controller() {

    }
}

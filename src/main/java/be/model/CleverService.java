package be.model;


import be.db.EventRepository;
import be.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CleverService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    public CleverService(){

    }

    public ArrayList<User> getUsers(){
        return (ArrayList<User>) userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.getOne(id);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void removeUser(User user){
        userRepository.delete(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public Event getEvent(long id) {
        return eventRepository.getOne(id);
    }

    public ArrayList<Event> getEvents(){
        return (ArrayList<Event>) eventRepository.findAll();
    }

    public void addEvent(Event event){
        eventRepository.save(event);
    }

    public void removeEvent(Event event){
        eventRepository.delete(event);
    }

    public void updateEvent(Event event){
        eventRepository.save(event);
    }

    public void addParticipant(long userid,long eventid){
        Event temp = eventRepository.getOne(eventid);
        temp.addParticipant(userRepository.getOne(userid));
        updateEvent(temp);
    }
}

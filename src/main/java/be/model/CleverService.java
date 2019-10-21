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

    public String getUsers(){
        String temp = "[";
        for (User user:userRepository.findAll()) {
            temp += user.getDisplay() + ",";
        }
        temp = temp.substring(0, temp.length() - 1) + "]";
        return temp;
    }

    public String getUser(long id) {
        return userRepository.getOne(id).getDisplay();
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

    public ArrayList<User> getParticipants(long eventId) {
        return eventRepository.getOne(eventId).getParticipants();
    }
}

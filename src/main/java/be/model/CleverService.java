
package be.model;


import be.db.EventRepository;
import be.db.PaymentRepository;
import be.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CleverService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public CleverService(){

    }

    public List<User> getUsers(){
        return userRepository.findAll();
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

    public void removeUser(long userId){
        userRepository.delete(this.userRepository.getOne(userId));
    }

    public void updateUser(User user){
        User oldUser = userRepository.findByUsername(user.getUsername());
        user.setId(oldUser.getId());
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

    public void removeEvent(long eventid) {
        this.eventRepository.delete(this.eventRepository.getOne(eventid));
    }

    public void updateEvent(Event event){
        Event oldEvent = eventRepository.findByEventName(event.getEventName());
        event.setId(oldEvent.getId());
        eventRepository.save(event);
    }

    public void addParticipant(long userid,long eventid){
        Event event = eventRepository.getOne(eventid);
        if (event.getParticipants().contains(userid)) {
            throw new ModelException("User is already in event");
        }
        event.addParticipant(userRepository.getOne(userid).getId());
        updateEvent(event);
    }

    public List<Long> getParticipants(long eventId) {
        return eventRepository.getOne(eventId).getParticipants();
    }

    public double getTotalCostOfEvent(long eventId) {
        double result = 0.0;
        for (Payment p : getPaymentsOfEvent(eventId)) {
            result += p.getAmount();
        }
        return result;
    }

    public Payment getPayment(long paymentId) {
        return paymentRepository.getOne(paymentId);
    }

    public void removePayment(long paymentId) {
        paymentRepository.delete(getPayment(paymentId));
    }

    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsOfEvent(long eventId) {

        List<Payment> result = new ArrayList<>();
        for (Payment p : paymentRepository.findAll()) {
            if (p.getEventId() == eventId) {
                result.add(p);
            }
        }
        return result;
    }

    public double getDebtOfUserFromEvent(long userId, long eventId) {
        // hoeveel ne user nog moet betalen van een event
        double total = 0;
        for (Payment payment : getPaymentsOfEvent(eventId)) {
            if (payment.getParticipants().contains(getUser(userId))) {
                total += payment.getAmountPerUser();
            }
        }
        // afronden trust me stackoverflow zei het zo
        return Math.round(total * 100) / 100.0;
    }

    public double getDueOfUserFromEvent(long userId, long eventId) {
        // sum of amount where user == arthurjoppart hah
        double total = 0;
        for (Payment payment : getPaymentsOfEvent(eventId)) {
            if (payment.getPayer().getId() == userId) {
                total += payment.getAmount();
            }
        }
        // afronden trust me stackoverflow zei het zo
        return Math.round(total * 100) / 100.0;

    }
}


package be.model;


import be.db.EventRepository;
import be.db.PaymentRepository;
import be.db.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
    @PersistenceContext
    EntityManager entityManager;

    public CleverService() {

    }

    public List<User> getUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public User getUser(long id) {
        return userRepository.getOne(id);
    }

    public void addUser(User user) {
        user.setUsername(user.getUsername().trim().toLowerCase());
        userRepository.save(user);
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }

    public void removeUser(long userId) {
        userRepository.delete(this.userRepository.getOne(userId));
    }

    public String getUsernameOfUser(long userid) {
        return userRepository.getOne(userid).getUsername();
    }

    public void updateUser(User user) {
        User oldUser = userRepository.findByUsername(user.getUsername());
        user.setId(oldUser.getId());
        userRepository.save(user);
    }

    public Event getEvent(long id) {
        return eventRepository.getOne(id);
    }

    public ArrayList<Event> getEvents() {
        try{
            return (ArrayList<Event>) eventRepository.findAll(Sort.by(Sort.Direction.ASC, "startDate"));
        }catch (Exception e){
            return (ArrayList<Event>) eventRepository.findAll();
        }

    }

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public void removeEvent(Event event) {
        eventRepository.delete(event);
    }

    public void removeEvent(long eventid) {
        this.eventRepository.delete(this.eventRepository.getOne(eventid));
    }

    public void updateEvent(Event event) {
        Event oldEvent = eventRepository.findByEventName(event.getEventName());
        event.setId(oldEvent.getId());
        eventRepository.save(event);
    }

    public void addParticipant(long userid, long eventid) {
        Event event = eventRepository.getOne(eventid);
        if (event.getParticipants().contains(userid)) {
            throw new ModelException("User is already in event");
        }
        event.addParticipant(userRepository.getOne(userid).getId());
        updateEvent(event);
    }

    public void removeParticipant(long userid, long eventid) {
        Event event = eventRepository.getOne(eventid);
        event.removeParticipant(userRepository.getOne(userid).getId());
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

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
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
            if (payment.getPayer() == userId) {
                total += payment.getAmount();
            }
        }
        // afronden trust me stackoverflow zei het zo
        return Math.round(total * 100) / 100.0;

    }

    public List<Object> getProfileEventData(String username) {
        JSONArray output = new JSONArray();
        for (Object[] array: getDueAndDebtPerEventForUser(username)) {
            BigInteger eventid = (BigInteger) array[0];
            Double debt = (Double) array[2];
            Double due = (Double) array[1];
            String name = eventRepository.getOne(Long.valueOf(eventid.intValue())).getEventName();

            if(due == null) due = 0.0;
            if(debt == null) debt = 0.0;

            if (!(debt == 0 && due == 0)) {
                JSONObject object = new JSONObject();
                object.put("name", name);
                object.put("debt", Math.round(debt * 100) / 100.0);
                object.put("due", Math.round(due * 100) / 100.0);
                output.put(object);
            }

        }
        return output.toList();
    }


    public List<Object> getProfileUserData(String username) {
        JSONArray output = new JSONArray();
        for (Object[] array: getDueAndDebtPerUserForUser(username)) {
            BigInteger eventid = (BigInteger) array[0];
            Double debt = (Double) array[2];
            Double due = (Double) array[1];
            String name = userRepository.getOne(Long.valueOf(eventid.intValue())).getUsername();

            if(due == null) due = 0.0;
            if(debt == null) debt = 0.0;

            if (!(debt == 0 && due == 0)) {
                JSONObject object = new JSONObject();
                object.put("name", name);
                object.put("debt", Math.round(debt * 100) / 100.0);
                object.put("due", Math.round(due * 100) / 100.0);
                output.put(object);
            }

        }
        return output.toList();
    }

    public int login(String username, String hashedPassword) {
        // Bestaat user?
        long userid = 0;
        try {
            userid = userRepository.findByUsername(username).getId();
            userRepository.getOne(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        // Juist of fout ww
        if (userRepository.getOne(userid).isCorrectPassword(hashedPassword)) {
            return 1;
        } else {
            return 0;
        }
    }


    public List<Event> getEventsFromUser(String username) {
        ArrayList<Event> events = new ArrayList<>();
        Long userid = userRepository.findByUsername(username).getId();
        for (Event event : eventRepository.findAll()) {
            for (Long id : event.getParticipants()) {
                if (userid.equals(id)) {
                    events.add(event);
                }
            }
        }
        return events;
    }

    public List<Payment> getPaymentsFromUser(long userid) {
        ArrayList<Payment> payments = new ArrayList<>();
        for (Payment payment : paymentRepository.findAll()) {
            if (payment.getPayer() == userid) {
                payments.add(payment);
            }
        }
        return payments;
    }

    public double getDueOfUserFromOtherUser(long userId, long otheruserId) {
        double total = 0;
        for (Payment payment : getPaymentsFromUser(userId)) {
            if (payment.getParticipants().contains(otheruserId)) {
                total += payment.getAmountPerUser();
                break;
            }
        }
        return Math.round(total * 100) / 100.0;
    }

    public Object getTotalDebt(String username) {
        String query = "select sum(pay.amount/(select count(distinct participants) from cleverdivide.payment_participants p group by payment_id having p.payment_id = part.payment_id)) from cleverdivide.payment_participants part inner join cleverdivide.payment pay on (pay.id = part.payment_id) where part.participants = " + userRepository.findByUsername(username).getId() + "and payer != " + userRepository.findByUsername(username).getId();
        Object result;
        try {
            result = entityManager.createNativeQuery(query).getSingleResult();
        } catch (Exception e) {
            result = 0;
        }
        if (result == null) result = 0.0;
        double rounded = Math.round((double) result * 100) / 100.0;
        return rounded;
    }

    public Object getTotalDue(String username) {
        String query = "select sum(distinct pay.amount) from cleverdivide.payment_participants part inner join cleverdivide.payment pay on (pay.id = part.payment_id) inner join cleverdivide.user on (part.participants = cleverdivide.user.id) where pay.payer = " + userRepository.findByUsername(username).getId();
        Object result;
        try {
            result = entityManager.createNativeQuery(query).getSingleResult();
        } catch (Exception e) {
            result = 0;
        }
        if (result == null) result = 0.0;
        double rounded = Math.round((double) result * 100) / 100.0;
        return rounded;
    }

    public List<Object[]> getDueAndDebtPerEventForUser(String username) {
        String query = "select * " +
                "from " +
                "(select event_id,sum(distinct amount) as due from cleverdivide.payment payment inner join cleverdivide.event eventt on (eventt.id = payment.event_id) group by event_id,payer having payer = " + userRepository.findByUsername(username).getId() +
                ")as duequery " +
                "natural full join " +
                "(select event_id,sum(amount/(select count(distinct participants) from cleverdivide.payment_participants p group by payment_id having p.payment_id = participants.payment_id)) as debt from cleverdivide.event eventt inner join cleverdivide.payment payment on (eventt.id = payment.event_id) inner join cleverdivide.payment_participants participants on (payment.id = participants.payment_id) group by participants.participants,event_id having participants.participants  = " + userRepository.findByUsername(username).getId() +
                ")as debtquery";
        return getObjects(query);
    }

    public List<Object[]> getDueAndDebtPerUserForUser(String username) {
        String query = "select payer,due,debt from (select payer,participants.participants,sum(amount/(select count(distinct participants) from cleverdivide.payment_participants p group by payment_id having p.payment_id = participants.payment_id)) as due from cleverdivide.payment payment inner join cleverdivide.payment_participants participants on (payment.id = participants.payment_id) group by payment.payer,participants.participants having payment.payer = " + userRepository.findByUsername(username).getId() + "                )as duequery" +
                "                natural full outer join " +
                "                (select payer,participants,sum(amount/(select count(distinct participants) from cleverdivide.payment_participants p group by payment_id having p.payment_id = participants.payment_id)) as debt from cleverdivide.payment payment inner join cleverdivide.payment_participants participants on (payment.id = participants.payment_id) group by payment.payer,participants.participants having participants.participants = " + userRepository.findByUsername(username).getId() +
                "                )as debtquery where payer != " + userRepository.findByUsername(username).getId();
        return getObjects(query);
    }

    private List<Object[]> getObjects(String query) {
        List<Object[]> result = new ArrayList<>();
        try {
            for (Object o : entityManager.createNativeQuery(query).getResultList()) {
                if (o instanceof Object[]) {
                    result.add((Object[]) o);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }


}
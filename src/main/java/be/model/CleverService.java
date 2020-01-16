
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
        return userRepository.findById(id).get();
    }

    public void addUser(User user) {
        user.setUsername(user.getUsername().trim().toLowerCase());
        userRepository.save(user);
    }

    public void removeUser(User user) {
        if (user.getId() == 3) return;
        userRepository.delete(user);
    }

    public void removeUser(long userId) {
        if (userId == 3) return;
        userRepository.delete(this.userRepository.getOne(userId));
    }

    public String getUsernameOfUser(long userid) {
        return userRepository.getOne(userid).getUsername();
    }

    public long getUserIdOfUsername(String username) {
        for (User u : getUsers()) {
            if (username.trim().toLowerCase().equals(u.getUsername())) {
                return u.getId();
            }
        }
        throw new IllegalArgumentException("User with username: "+username+" not found");
    }

    public void updateUser(User user) {
        User oldUser = userRepository.findByUsername(user.getUsername());
        user.setId(oldUser.getId());
        userRepository.save(user);
    }

    public Event getEvent(long id) {
        return eventRepository.findById(id).get();
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
        if (event.getId() == 3) return;
        eventRepository.delete(event);
    }

    public void removeEvent(long eventid) {
        if (eventid == 3) return;
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

    public void addParticipantByUsername(String username, long eventid) {
        long userid = getUserIdOfUsername(username);
        addParticipant(userid, eventid);
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

    public List<Object> getPaymentInformation(String username){
        JSONArray output = new JSONArray();
        for (Object[] array: getPaymentInformationForUser(username)) {
            BigInteger id = (BigInteger) array[0];
            String message = (String) array[1];
            Double price = (Double) array[2];
            Integer payment_id = (Integer) array[4];
            boolean betaald = (boolean) array[3];
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("message",message);
                object.put("price", Math.round(price * 100) / 100.0);
                object.put("betaald", betaald);
                object.put("payment_id", payment_id);
                output.put(object);
        }
        return output.toList();
    }

    public List<Object> getProfileEventData(String username) {
        JSONArray output = new JSONArray();
        for (Object[] array: getDueAndDebtPerEventForUser(username)) {
            BigInteger eventid = (BigInteger) array[0];
            Double due = (Double) array[2];
            Double debt = (Double) array[1];
            String name = eventRepository.getOne(Long.valueOf(eventid.intValue())).getEventName();

            calculateDebtAndDue(output, due, debt, name);

        }
        return output.toList();
    }


    public List<Object> getProfileUserData(String username) {
        JSONArray output = new JSONArray();
        for (Object[] array: getDueAndDebtPerUserForUser(username)) {
            BigInteger eventid = (BigInteger) array[0];
            Double due = (Double) array[2];
            Double debt = (Double) array[1];
            String name = userRepository.getOne(Long.valueOf(eventid.intValue())).getUsername();
            calculateDebtAndDue(output, due, debt, name);

        }
        return output.toList();
    }

    private void calculateDebtAndDue(JSONArray output, Double due, Double debt, String name) {
        if(due == null) due = 0.0;
        if(debt == null) debt = 0.0;

        if (!(debt == 0 && due == 0)) {
            JSONObject object = new JSONObject();
            object.put("name", name);
            object.put("geven", Math.round(due * 100) / 100.0);
            object.put("verkrijgen", Math.round(debt * 100) / 100.0);
            output.put(object);
        }
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

    public Object getTotalDue(String username) {
        long id = userRepository.findByUsername(username).getId();
        String query = "select sum(price_per_user) as topay\n" +
                "from ( SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "where payer = "+id+" and payee != "+id+" and betaald = false\n" +
                "group by payer\n" +
                "order by payer".replaceAll("\t"," ").replaceAll("\n"," ");
        Object result;
        try {
            result = entityManager.createNativeQuery(query).getSingleResult();
        } catch (Exception e) {
            result = 0.0;
        }
        if (result == null) result = 0.0;
        double rounded = Math.round((double) result * 100) / 100.0;
        return rounded;
    }

    public Object getTotalDebt(String username) {
        long id = userRepository.findByUsername(username).getId();
        String query = "select sum(price_per_user) as topay\n" +
                "from ( SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "where payee = "+id+" and payer != "+id+" and betaald = false\n" +
                "group by payee\n" +
                "order by payee".replaceAll("\t"," ").replaceAll("\n"," ");
        Object result;
        try {
            result = entityManager.createNativeQuery(query).getSingleResult();
        } catch (Exception e) {
            result = 0.0;
        }
        if (result == null) result = 0.0;
        double rounded = Math.round((double) result * 100) / 100.0;
        return rounded;
    }

    public List<Object[]> getDueAndDebtPerEventForUser(String username) {
        long id = userRepository.findByUsername(username).getId();
        String query = "select id,coalesce(verkrijgen,0) as verkrijgen ,coalesce(geven,0) as geven\n" +
                "from (select event_id as id,sum(price_per_user) as geven\n" +
                "from ( SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "where payee = "+id+" and payer != "+id+" and betaald = false\n" +
                "group by event_id) as v\n" +
                "natural full join\n" +
                "(select event_id as id,sum(price_per_user) as verkrijgen\n" +
                "from ( SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "where payer = "+id+" and payee != "+id+" and betaald = false\n" +
                "group by event_id) as g".replaceAll("\t"," ").replaceAll("\n"," ");
        return getObjects(query);
    }

    public List<Object[]> getDueAndDebtPerUserForUser(String username) {
        long id = userRepository.findByUsername(username).getId();
        String query = "select id,coalesce(verkrijgen,0) as verkrijgen,coalesce(geven,0) as geven\n" +
                "from (select payer as id,sum(price_per_user) as geven\n" +
                "from (SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "where payee = "+id+" and payer != "+id+" and betaald = false\n" +
                "group by payer) as v\n" +
                "natural full join\n" +
                "(select payee as id,sum(price_per_user) as verkrijgen\n" +
                "from (SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "where payer = "+id+" and payee != "+id+" and betaald = false\n" +
                "group by payee) as g".replaceAll("\t"," ").replaceAll("\n"," ");
        return getObjects(query);
    }

    public List<Object[]> getPaymentInformationForUser(String username){
        long id = userRepository.findByUsername(username).getId();
        String query = "\t select payer as id ,message as description,price_per_user as bedrag,betaald,payment_id\n" +
                "\t from ( SELECT payment_participants.payment_id,\n" +
                "    payment.payer,\n" +
                "\tpayment.message,\n" +
                "    payment_participants.participants AS payee,\n" +
                "    payment.amount,\n" +
                "    payment.event_id,\n" +
                "\tpayment_participants.isBetaald as betaald,\n" +
                "    (payment.amount / (( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id))) AS price_per_user,\n" +
                "    ( SELECT users_per_payment.users\n" +
                "           FROM cleverdivide.users_per_payment\n" +
                "          WHERE users_per_payment.id = payment_participants.payment_id) AS total_payees\n" +
                "   FROM cleverdivide.payment\n" +
                "     JOIN cleverdivide.payment_participants ON payment_participants.payment_id = payment.id\n" +
                "     JOIN cleverdivide.event ON payment.event_id = event.id) as payment_info\n" +
                "\t where payee = "+id+" and payer != "+id+"".replaceAll("\t"," ").replaceAll("\n"," ");
        System.out.println(query);
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
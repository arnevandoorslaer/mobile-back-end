package be.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @NotNull(message = "id can't be null")
    private long id;

    @NotNull(message = "participants (betalers) list can't be null")
    @ElementCollection(targetClass=Long.class)
    private List<Long> participants;
    // mensen da moete betalen

    private long payer;
    // iemand dat betaald heeft

    @DecimalMin(value = "0.01", inclusive = true)
    private double amount;

    private long eventId;

    @NotNull(message = "message can't be null")
    @NotEmpty(message = "message can't be empty")
    private String message;

    public Payment() {}

    public Payment(ArrayList<Long> participants, long payer) {
        setParticipants(participants);
        setPayer(payer);
    }

    public Payment(ArrayList<Long> participants, long payer, long eventId) {
        setParticipants(participants);
        setPayer(payer);
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Long> participants) {
        this.participants = participants;
    }

    public long getPayer() {
        return payer;
    }

    public void setPayer(long payer) {
        this.payer = payer;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountPerUser() {
        return this.amount / this.participants.size(); //+1 want payer betaalt ook mee
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

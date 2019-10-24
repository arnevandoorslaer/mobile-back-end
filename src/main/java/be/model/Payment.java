package be.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
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
    @ManyToMany
    @ElementCollection(targetClass=User.class)
    private List<User> participants;
    // mensen da moete betalen

    @OneToOne
    @NotNull(message = "payer can't be null")
    private User payer;
    // iemand dat betaald heeft

    @DecimalMin(value = "0.01", inclusive = true)
    private double amount;

    private long eventId;

    public Payment() {}

    public Payment(ArrayList<User> participants, User payer) {
        setParticipants(participants);
        setPayer(payer);
    }

    public Payment(ArrayList<User> participants, User payer, long eventId) {
        setParticipants(participants);
        setPayer(payer);
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountPerUser() {
        return this.amount / this.participants.size() + 1; //+1 want payer betaalt ook mee
    }
}

package be.model;

import javax.persistence.*;
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
    public Payment() {}

    public Payment(ArrayList<User> participants, User payer) {
        setParticipants(participants);
        setPayer(payer);
    }
    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        if (participants == null) throw new ModelException("participants can't be null");
        if (participants.isEmpty()) throw new ModelException("participants can't be empty");
        this.participants = participants;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        if (payer == null) throw new ModelException("payer can't be null");
        this.payer = payer;
    }
}

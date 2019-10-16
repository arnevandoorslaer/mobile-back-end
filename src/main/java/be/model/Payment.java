package be.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @NotNull(message = "id can't be null")
    private long id;

    @NotNull(message = "participants (betalers) list can't be null")
    private List<User> participants;
    // mensen da moete betalen

    @NotNull(message = "payer can't be null")
    private User payer;
    // iemand dat betaald heeft
}

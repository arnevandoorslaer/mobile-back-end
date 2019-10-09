package be.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @NotNull(message = "id can't be null")
    private long id;
    @NotNull(message = "lastname can't be null")
    @NotEmpty(message = "lastname can't be empty")
    private String lastname;
    @NotNull(message = "firstname can't be null")
    @NotEmpty(message = "firstname can't be empty")
    private String firstname;
    @NotNull(message = "username can't be null")
    @NotEmpty(message = "username can't be empty")
    private String username;
    @NotNull(message = "iban can't be null")
    @NotEmpty(message = "iban can't be empty")
    private String iban;

    public User() {

    }

    public User(String lastname, String firstname, String username, String password) {
        setLastname(lastname);
        setFirstname(firstname);
        setUsername(username);
    }

    public String getLastname() {
        return lastname;
    }

    private void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    private void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(String iban) {
        this.iban = iban;
    }
}

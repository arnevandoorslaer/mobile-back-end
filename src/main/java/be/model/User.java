package be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
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
    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    private String password;

    public User() {
    }

    public User(String lastname, String firstname, String username, String password, String iban) {
        super();
        setLastname(lastname);
        setFirstname(firstname);
        setUsername(username.trim().toLowerCase());
        setPassword(password);
        setIBAN(iban);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isCorrectPassword(String password) {
        return password.equals(this.password);
    }


    /*
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                ", \"lastname\":\"" + lastname + "\"" +
                ", \"firstname\":\"" + firstname + "\"" +
                ", \"username\":\"" + username + "\"" +
                ", \"iban\":\"" + iban + "\"" +
                ", \"password\":\"" + password + "\"" +
                '}';
    }

     */
    @JsonIgnore
    public String getDisplay() {
        return "  {\n" +
                "      \"display\":\"" + firstname + " " + lastname + "\",\n" +
                "      \"value\":\"" + username + "\"\n" +
                "   }";
    }
}

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
    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    private String password;

    public User() {

    }

    public User(String lastname, String firstname, String username, String password) {
        setLastname(lastname);
        setFirstname(firstname);
        setPassword(password);
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

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public boolean checkCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

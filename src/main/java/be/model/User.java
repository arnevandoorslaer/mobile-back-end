package be.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public User() {}

    public User(String lastname, String firstname, String username, String password,String iban) {
        super();
        setLastname(lastname);
        setFirstname(firstname);
        setUsername(username);
        setPasswordHashed(password);
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

    private String hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //create MessageDigest
        MessageDigest crypt = MessageDigest.getInstance("SHA-512");
        //reset
        crypt.reset();
        //update
        byte[] passwordBytes = password.getBytes("UTF-8");
        crypt.update(passwordBytes);
        //digest
        byte[] digest = crypt.digest();
        //convert to String
        BigInteger digestAsBigInteger = new BigInteger(1, digest);
        //return hashed password
        return digestAsBigInteger.toString(16);
    }

    public void setPasswordHashed(String password) {
        try {
            setPassword(hashPassword(password));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public boolean isCorrectPassword(String password) {
        if(password.isEmpty()){
            throw new IllegalArgumentException("Geen paswoord gegeven.");
        }
        String p;
        try {
            p = hashPassword(password);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return getPassword().equals(p);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", username='" + username + '\'' +
                ", iban='" + iban + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

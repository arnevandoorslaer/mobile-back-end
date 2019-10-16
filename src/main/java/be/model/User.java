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

    public User(String lastname, String firstname, String username, String password) {
        super();
        setLastname(lastname);
        setFirstname(firstname);
        setUsername(username);
        setPasswordHashed(password);
    }

    public void setPassword(String password) {
        if (password == null) throw new ModelException("password can't be null");
        if (password.isEmpty()) throw new ModelException("password can't be empty");
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
        if (lastname == null) throw new ModelException("lastname can't be null");
        if (lastname.isEmpty()) throw new ModelException("lastname can't be empty");
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    private void setFirstname(String firstname) {
        if (firstname == null) throw new ModelException("firstname can't be null");
        if (firstname.isEmpty()) throw new ModelException("firstname can't be empty");
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        if (username == null) throw new ModelException("username can't be null");
        if (username.isEmpty()) throw new ModelException("username can't be empty");
        this.username = username;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(String iban) {
        if (iban == null) throw new ModelException("iban can't be null");
        if (iban.isEmpty()) throw new ModelException("iban can't be empty");
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
        if (password.trim().isEmpty()) {
            throw new ModelException("Geen paswoord gegeven.");
        }
        try {
            setPassword(hashPassword(password));
        } catch (Exception e) {
            throw new ModelException(e.getMessage(), e);
        }
    }

    public boolean isCorrectPassword(String password) {
        if(password.isEmpty()){
            throw new ModelException("Geen paswoord gegeven.");
        }
        String p;
        try {
            p = hashPassword(password);
        } catch (Exception e) {
            throw new ModelException(e.getMessage(), e);
        }
        return getPassword().equals(p);
    }

}

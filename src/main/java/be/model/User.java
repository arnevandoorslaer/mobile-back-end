package be.model;

public class User {
    private String lastname;
    private String firstname;
    private String username;
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

package be.db;

import be.model.User;

import java.util.ArrayList;

public class UserDB {
    private ArrayList<User> users = new ArrayList<>();

    public UserDB() {
        loadUsers();
    }

    private void loadUsers() {
        users.add(new User("min", "ad", "admin", "t"));
        users.add(new User("us", "er", "user", "t"));
    }

    public User Authenticate(String username, String password) {
        for (User user : users) {
            if (user.checkCredentials(username, password)) {
                return user;
            }
        }
        return null;
    }
}

package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: create header comment
 */
public class ModelSingleton {

    private static ModelSingleton instance = new ModelSingleton();
    private List<User> users = new ArrayList<>();

    private ModelSingleton( ) {

        //makeModel();
    }

    public static ModelSingleton getInstance() {

        return instance;
    }

    private void makeModel(String userName, String email, String password, Boolean isAdmin) {
        User u = new User(userName,email,password,isAdmin);
        users.add(u);
    }

    public Boolean checkIfUser(String email, String password) {
        for (User i : users) {
            if (i.getEmail().equals(email) && i.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User freshUser) {

        users.add(freshUser);
    }

}

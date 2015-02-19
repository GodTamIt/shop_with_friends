package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: create header comment
 */
public class ModelSingleton {

    private static ModelSingleton instance = new ModelSingleton();
    private ArrayList<User> users = new ArrayList<>();

    private ModelSingleton( ) {
        makeModel();
    }

    public static ModelSingleton getInstance() {
        return instance;
    }

    private void makeModel() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            //User u = new User ("User " + i, rand.nextInt(10));
            //users.add(u);
        }
    }

    public Boolean checkIfUser(String userName, String password) {
        boolean isaUser = false;
        for (User i : users) {
            if (i.getUserName().equals(userName) && i.getPassword().equals(password)) {
                isaUser = true;
            }
        }
        return isaUser;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser(User freshUser) {
        users.add(freshUser);
    }
}

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

    public List<User> getUsers() {
        return users;
    }
}

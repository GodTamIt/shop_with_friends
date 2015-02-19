package models;

import java.io.Serializable;

/**
 * TODO: create header comment
 */
public class User implements Serializable {


    private static final long serialVersionUID = -7060210544600464481L;

    private String userName;
    private String email;
    private String password;
    private Boolean isAdmin;

    public User(String userName, String email, String password, Boolean isAdmin) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

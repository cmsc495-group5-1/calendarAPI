package calendar.api.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class User {
    private String userid;

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private ArrayList<Calendar> calendars;

    User findById(String id) {
        return null;
    }



    User findByUsername(String username) {
        return null;
    }



    boolean authenticate(String password) {
        return false;
    }



        void newUser(String firstName, String lastName, String email) {

    }



    void save() {

    }



    void validate() {
    }



    void saveUser() {
    }



    void applyPasswordChange(String pass, String passConfirm) {

    }
}

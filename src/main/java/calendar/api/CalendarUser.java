package calendar.api;

import lombok.Data;

@Data
public class CalendarUser {

    private String email;

    private String firstName;

    private String lastName;

    private String username

    private ArrayList<Calendar> calendars;

    CalendarUser findById(String id) {
    }



    CalendarUser findByUsername(String username) {

    }



    boolean authenticate(String password) {
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

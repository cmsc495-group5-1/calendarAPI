package calendar.api.dto;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String calendarIds;

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

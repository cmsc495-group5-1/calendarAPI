package calendar.api.dto;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table
public class User implements Serializable {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "calendar_ids")
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

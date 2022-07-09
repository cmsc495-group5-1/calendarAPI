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
@Table(name = "user")
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

    @Column(name = "password")
    private String password;



    public boolean authenticate(String password) {
        return this.password.equals(password);
    }



    public void applyPasswordChange(String username, String pass, String passConfirm) {
        if (pass.equals(passConfirm)){
            setUsername(username);
            setPassword(pass);
        }
    }
}

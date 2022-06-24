package calendar.api.dto;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Calendar {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String CalendarId;

    private String userId;

    private String name;

    private ArrayList<String> eventIds;
}

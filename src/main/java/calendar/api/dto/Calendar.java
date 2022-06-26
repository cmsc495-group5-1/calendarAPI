package calendar.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("calendarId")
    private String calendarId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("name")
    private String name;

    private String eventIds;
}

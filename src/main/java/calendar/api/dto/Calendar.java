package calendar.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "calendar")
public class Calendar implements Serializable {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @JsonProperty("calendarId")
    @Column(name = "calendar_id")
    String calendarId;

    @Column(name = "user_id")
    @JsonProperty("userId")
    String userId;

    @Column(name = "name")
    @JsonProperty("name")
    String name;

    @Column(name = "event_ids")
    @JsonProperty("eventIds")
    String eventIds;
}

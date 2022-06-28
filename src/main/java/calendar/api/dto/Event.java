package calendar.api.dto;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "event")
public class Event implements Serializable{

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "event_id")
    private String eventId;

    @Column(name = "calendar_id")
    private String calendarId;

    @Column(name = "event_Name")
    private String eventName;

    @Column(name = "start_Date_Time")
    private Date startDateTime;

    @Column(name = "end_Date_Time")
    private Date endDateTime;

    @Column(name = "location")
    private String Location;

    @Column(name = "description")
    private String Description;
}


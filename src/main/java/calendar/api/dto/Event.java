package calendar.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Event {

    private long calendarId;

    private String eventName;

    private Date startDateTime;

    private Date endDateTime;

    private String Location;

    private String Description;
}


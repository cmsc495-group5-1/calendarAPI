package calendar.api.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Calendar {

    private User user;

    private String name;

    private ArrayList<Event> events;
}

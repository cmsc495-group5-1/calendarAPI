package calendar.api.controller;

import calendar.api.dto.Event;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {

    // I wasn't sure if I should use calendarId variable for path value? So I just stuck with {id} like the other controllers
    @GetMapping(value = "/event/{id}")
    public Event getEvent(@PathVariable String calendarId) {return null;
    }

    // I threw in the calendarId since this new event will need to be tied to an existing calendar. Is that correct?
    @PostMapping(value = "/api/event/{id}")
    public void createEvent(String... params) {
    }

    // Similar scenario -- I wasn't sure if I should use calendarId in place of {id}
    @PutMapping(value = "/api/event/{id}")
    public void updateEvent(@PathVariable String calendarId, Event event) {
    }

    @DeleteMapping(value = "/api/event/{id})")
    public void deleteEvent(@PathVariable String calendarId) {
    }
}
package calendar.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CalendarController {

    @GetMapping(value = "/api/calendar", produces= MediaType.APPLICATION_JSON_VALUE)
    public Calendar[] getCalendar() {
    }

    @GetMapping(value = "/api/calendar/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Calendar getCalendar(@PathVariable String id) {
    }

    @PostMapping(value = "/api/calendar")
    public void createCalendar(String... params) {
    }

    @PutMapping(value = "/api/calendar/{id}")
    public void updateCalendar(@PathVariable String id) {
    }

    @DeleteMapping(value = "/api/calendar/{id}")
    public void deleteCalendar(@PathVariable String id) {
    }
}


package calendar.api.controller;

import calendar.api.dto.Calendar;
import calendar.api.dto.Event;
import calendar.api.dto.User;
import calendar.api.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class CalendarController {

    @Autowired
    CalendarRepository calendarRepository;

    @GetMapping(value = "/api/calendars", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Calendar> getCalendars() {
        return calendarRepository.findAll();
    }

    @GetMapping(value = "/api/calendars/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Optional<Calendar> getCalendar(@PathVariable String id) {
        return calendarRepository.findById(id);
    }

    @PostMapping(value = "/api/calendars")
    public void createCalendar(String... params) {
    }

    //TESTING ONLY
    @GetMapping(value = "/api/calendars/test")
    public void createTestCalendar() {
        //This is a test TODO: Delete later
        calendarRepository.save(generateUserReturnCalendar());
    }

    @PutMapping(value = "/api/calendars/{id}")
    public void updateCalendar(@PathVariable String id) {
    }

    @DeleteMapping(value = "/api/calendars/{id}")
    public void deleteCalendar(@PathVariable String id) {
    }

    private Calendar generateUserReturnCalendar(){
        var user = new User();
        var calendar = generateTestCalendar();
        user.setEmail("test@yahoo.com");
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername("testuser1");
        calendar.setUserId(user.getUserid());
        ArrayList<String> calendarArraylist = new ArrayList<>();
        calendarArraylist.add(calendar.getCalendarId());
        user.setCalendars(calendarArraylist);
        return calendar;
    }

    private Calendar generateTestCalendar(){
        var calendar = new Calendar();
        var event = generateTestEvent();
        calendar.setName("TestCalendar");
        event.setCalendarId(calendar.getCalendarId());
        ArrayList<String> events = new ArrayList<>();
        events.add(event.getEventId());
        return calendar;
    }

    private Event generateTestEvent(){
        var event = new Event();
        event.setEventName("TestEvent");
        event.setStartDateTime(new Date());
        event.setEndDateTime(new Date());
        event.setLocation("Here&Now");
        event.setDescription("This is a Test");
        return event;
    }
}


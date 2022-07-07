package calendar.api.controller;

import calendar.api.dto.Calendar;
import calendar.api.dto.Event;
import calendar.api.dto.User;
import calendar.api.repository.CalendarRepository;
import calendar.api.repository.EventRepository;
import calendar.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
public class CalendarController {

    //TODO: Delete later
    private static int eventCounter;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    ObjectMapper objectMapper;

    @GetMapping(value = "/api/calendar/all/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Object> getCalendarsAll(@PathVariable String id) {
        var user = userRepository.findById(id);

        var calendarsQueries = getUserCalendars(user);
        var calendars = new ArrayList<>();
        for (String calendarsString : calendarsQueries){
            var newCalendar = calendarsString.replace("," , " ");
            log.info("searching for new calendar" + newCalendar);
            var userCalendar = calendarRepository.findById(newCalendar);
            if (!userCalendar.isEmpty()){
                calendars.add(userCalendar);
                log.info("User calendar found: " + userCalendar.get());
            } else {
                log.info("User calendar not found.");
            }
        }
        return calendars;
    }

    @GetMapping(value = "/api/calendar/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Optional<Calendar> getCalendar(@PathVariable String id) {
        return calendarRepository.findById(id);
    }

    @PostMapping(value = "/api/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCalendar(@RequestBody Calendar calendar) {
        calendarRepository.save(calendar);
    }

    //TESTING ONLY
    //TODO: Delete later
    @GetMapping(value = "/api/calendar/test")
    public Calendar createTestCalendar() {
        //This is a test TODO: Delete later
        return generateUserReturnCalendar();
    }

    @PutMapping(value = "/api/calendar/{id}")
    public void updateCalendar(@PathVariable String id) {
    }

    @DeleteMapping(value = "/api/calendar/{id}")
    public void deleteCalendar(@PathVariable String id) {
    }

    //TODO: Delete later
    private Calendar generateUserReturnCalendar(){
        var user = new User();
        var calendar = generateTestCalendar();
        user.setEmail("test@yahoo.com");
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername("testuser1");
        ArrayList<String> calendarArraylist = new ArrayList<>();
        calendarArraylist.add(calendar.getCalendarId());
        user.setCalendarIds(calendarArraylist.toString());
        userRepository.save(user);
        calendar.setUserId(user.getUserId());
        calendarRepository.save(calendar);
        return calendar;
    }

    //TODO: Delete later
    private Calendar generateTestCalendar(){
        var calendar = new Calendar();
        var event = generateTestEvent();
        var secondEvent = generateTestEvent();
        var thirdEvent = generateTestEvent();
        calendar.setName("TestCalendar");
        event.setCalendarId(calendar.getCalendarId());
        secondEvent.setCalendarId(calendar.getCalendarId());
        thirdEvent.setCalendarId(calendar.getCalendarId());
        eventRepository.save(event);
        eventRepository.save(secondEvent);
        eventRepository.save(thirdEvent);
        ArrayList<String> events = new ArrayList<>();
        events.add(event.getEventId());
        events.add(secondEvent.getEventId());
        events.add(thirdEvent.getEventId());
        calendar.setEventIds(events.toString());
        calendarRepository.save(calendar);
        return calendar;
    }

    //TODO: Delete later
    private Event generateTestEvent(){
        var event = new Event();
        eventCounter = 1;
        event.setEventName("TestEvent " + eventCounter );
        event.setStartDateTime(new Date());
        event.setEndDateTime(new Date());
        event.setLocation("Here&Now");
        event.setDescription("This is a Test");
        eventCounter++;
        return event;
    }

    public String[] getUserCalendars(Optional<User> user){
        return user.get().getCalendarIds().replace(",", "").replace("[","").replace("]", "").split(" ");
    }
}


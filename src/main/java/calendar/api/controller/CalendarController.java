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
import java.util.Arrays;
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

    @GetMapping(value = "/api/calendar/all/{userId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Object> getCalendarsAll(@PathVariable String userId) {
        var user = userRepository.findById(userId);

        var calendarsQueries = getUserCalendars(user);
        var calendars = new ArrayList<>();
        for (String calendarsString : calendarsQueries){
            var newCalendar = calendarsString.replace("," , " ");
            log.info("searching for new calendar");
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
    public Optional<Calendar> getCalendar(@PathVariable String id) throws Exception {
        var calendar = calendarRepository.findById(id);

        // Throw exception for empty calendar
        if (calendar.isEmpty()){
            throw new Exception("Calendar not found.");
        }
        return calendar;
    }

    @PostMapping(value = "/api/calendar/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCalendar(@PathVariable String userId, @RequestBody Calendar calendar) throws Exception {
        var user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new Exception("User not found.");
        }
        log.info("User found " + user.get().getUserId());

        calendar.setUserId(user.get().getUserId());
        calendarRepository.save(calendar);
        log.info("New calendar has been saved. " + calendar);

        var newAddedCalendars = getUserCalendars(user);
        var newCalendars = new String[newAddedCalendars.length+1];
        var count = 0;
        for (Object newAddedCalendar : newAddedCalendars){
            newCalendars[count] = (String) newAddedCalendar;
            count++;
        }
        newCalendars[newCalendars.length -1] = calendar.getCalendarId();
        log.info("Calendar added to user. " + calendar);
        user.get().setCalendarIds(Arrays.toString(newCalendars));
        userRepository.save(user.get());
    }

    //TESTING ONLY
    //TODO: Delete later
    @GetMapping(value = "/api/calendar/test")
    public Calendar createTestCalendar() {
        //This is a test TODO: Delete later
        return generateUserReturnCalendar();
    }

    @PutMapping(value = "/api/calendar/{id}")
    public void updateCalendar(@PathVariable String id, @RequestBody Calendar calendar) throws Exception {
        var calendarUpdate = calendarRepository.findById(id);
        if (calendarUpdate.isEmpty()) {
            throw new Exception("There is no calendar associated with this user.");
        }
        //TODO: Assert calendar is owned by authenticated user

        if (!calendar.getCalendarId().equals(id)){
            throw new Exception("The calendar being updated has a different event Id");
        }
        calendarRepository.save(calendar);
    }

    @DeleteMapping(value = "/api/calendar/{id}")
    public void deleteCalendar(@PathVariable String id) throws Exception {
        var calendar = calendarRepository.findById(id);
        if (!calendar.get().getCalendarId().equals(id)) {
            throw new Exception("This event is not listed within the calendar you have open");
        }

        //TODO: Assert calendar is owned by authenticated user

        log.info("Calendar removed from user: " + id);
        calendarRepository.deleteById(calendar.get().getCalendarId());

        var user = userRepository.findById(calendar.get().getUserId());
        var oldCalendars = getUserCalendars(user);
        var newCalendars = new String[oldCalendars.length];
        var count = 0;
        log.info("Removing old calendar from user calendar Ids.");
        for (String oldCalendar : oldCalendars){
            if (!(oldCalendar.equals(calendar.get().getCalendarId()))){
                newCalendars[count] = oldCalendar;
            }
            count++;
        }
        user.get().setCalendarIds(Arrays.toString(newCalendars));
        userRepository.save(user.get());
    }

    //TODO: Delete later
    private Calendar generateUserReturnCalendar(){
        var user = new User();
        var calendar = generateTestCalendar();
        var calendar2 = generateTestCalendar();
        user.setEmail("test@yahoo.com");
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername("testuser1");
        ArrayList<String> calendarArraylist = new ArrayList<>();
        calendarArraylist.add(calendar.getCalendarId());
        calendarArraylist.add(calendar2.getCalendarId());
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
        Date monthFromNow = new Date();
        monthFromNow.setTime(monthFromNow.getTime() + 30L * 1000 * 60 * 60 * 24);
        var event = new Event();
        eventCounter = 1;
        event.setEventName("TestEvent " + eventCounter );
        event.setStartDateTime(new Date());
        event.setEndDateTime(monthFromNow);
        event.setLocation("Here&Now");
        event.setDescription("This is a Test");
        eventCounter++;
        return event;
    }

    public String[] getUserCalendars(Optional<User> user){
        return user.get().getCalendarIds().replace(",", "")
                .replace("[","").replace("]", "").split(" ");
    }
}


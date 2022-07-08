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

    ObjectMapper objectMapper;

    @GetMapping(value = "/api/calendar/all/{userId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Object> getCalendarsAll(@PathVariable String userId) {
        var user = userRepository.findById(userId);

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
    public Optional<Calendar> getCalendar(@PathVariable String calendarId) throws Exception {
        var calendar = calendarRepository.findById(calendarId);

        // I wasn't sure if the .contains(calendarId) section is necessary? Since we're searching for a specific calendar?
        if (calendar.get().getCalendarId().contains(calendarId)){
            log.info("User calendar found: " + calendar.get());
        } else {
            log.info("No calendars found for this user.");
        }

        // Throw exception for empty calendar
        if (!calendar.isEmpty()){
            throw new Exception("Calendar not found.");
        }
        return calendar;
    }

    @PostMapping(value = "/api/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCalendar(@PathVariable String userId, @RequestBody Calendar calendar) throws Exception {
        var user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new Exception("User not found.");
        }
        log.info("No user found " + user.get().getUserId());

        // I'm not sure if userId is the correct parameter to use here. Would it be calendarId instead?
        calendar.setCalendarId(String.valueOf(user));
        calendarRepository.save(calendar);
        log.info("New calendar has been saved. " + calendar);

        // I also wasn't sure if this section is necessary. Since it's a new calendar with new events?
        // I modeled it off of your PostMapping for EventController.
        var newAddedCalendars = getCalendarsAll(userId);
        var newCalendars = new String[newAddedCalendars.length+1];
        var count = 0;
        for (Object newAddedCalendar : newAddedCalendars){
            newCalendars[count] = (String) newAddedCalendar;
            count++;
        }
        // I think this is somewhat close. I'm a bit unsure how to get the new calendar to save/align to the userId.
        newCalendars[newCalendars.length -1] = calendar.getCalendarId();
        log.info("Calendar added to user. " + Arrays.toString(newAddedCalendars));
    }

    //TESTING ONLY
    //TODO: Delete later
    @GetMapping(value = "/api/calendar/test")
    public Calendar createTestCalendar() {
        //This is a test TODO: Delete later
        return generateUserReturnCalendar();
    }

    @PutMapping(value = "/api/calendar/{id}")
    public void updateCalendar(@PathVariable String calendarId, @RequestBody Calendar calendar) throws Exception {
        var calendarUpdate = calendarRepository.findById(calendarId);
        if (!calendarUpdate.get().isEmpty(0)) {
            throw new Exception("There is no calendar associated with this user.");
        }
        //TODO: Assert calendar is owned by authenticated user
        var oldCalendar = eventRepository.findById(calendarId);
        if (oldCalendar.isEmpty()){
            throw new Exception("Calendar does not exist.");
        }

        if (!calendar.getCalendarId().equals(calendarId)){
            throw new Exception("The calendar being updated has a different event Id");
        }
        calendarRepository.save(calendar);
    }

    @DeleteMapping(value = "/api/calendar/{id}")
    public void deleteCalendar(@PathVariable String calendarId) throws Exception {
        var calendar = calendarRepository.findById(calendarId);
        if (!calendar.get().getCalendarId().contains(calendarId)) {
            throw new Exception("This event is not listed within the calendar you have open");
        }

        //TODO: Assert calendar is owned by authenticated user
        var oldCalendar = calendarRepository.findById(calendarId);
        if (oldCalendar.isEmpty()){
            throw new Exception("Calendar ID not found. Calendar does not exist.");
        }

        if (!oldCalendar.get().getCalendarId().equals(calendarId)){
            throw new Exception("The calendar being replaced has a different Calendar ID.");
        }

        // Functionality for deleting calendar found by Calendar ID + logging of action.
        log.info("Calendar removed from user: " + calendarId);
        eventRepository.deleteById(oldCalendar.get().getCalendarId());
        calendarRepository.save(calendar.get());
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


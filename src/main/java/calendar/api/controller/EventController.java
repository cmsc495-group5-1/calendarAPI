package calendar.api.controller;

import calendar.api.dto.Calendar;
import calendar.api.dto.Event;
import calendar.api.repository.CalendarRepository;
import calendar.api.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class EventController {

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    EventRepository eventRepository;

    ObjectMapper objectMapper;

    @GetMapping(value = "/api/calendar/{id}/event", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Object> getAllEventsForCalendar(@PathVariable String id) {
        var calendar = calendarRepository.findById(id);
        //TODO: Assert calendar is owned by authenticated user
        var eventQueries = getCalendarEvents(calendar);
        var events = new ArrayList<>();
        for (String eventString : eventQueries){
            var newEvent = eventString.replace(",", "");
            log.info("searching for new event " + newEvent);
            var event = eventRepository.findById(newEvent);
            if (!event.isEmpty()) {
                events.add(event);
                log.info("event found" + event.get());
            } else {
                log.info("event not found");
            }
        }
        return events;
    }

    @GetMapping(value = "/api/calendar/{calendarId}/event/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Event> getEventInCalendar(@PathVariable String calendarId, @PathVariable String eventId) throws Exception {
        var calendar = calendarRepository.findById(calendarId);
        if (!calendar.get().getEventIds().contains(eventId)){
            throw new Exception("This event is not listed within the calendar you have open");
        }
        //TODO: Assert calendar is owned by authenticated user
        var event = eventRepository.findById(eventId);
        if (!event.isEmpty()) {
            log.info("event found" + event.get());
        } else {
            log.info("event not found");
        }
        return event;
    }

    // I threw in the calendarId since this new event will need to be tied to an existing calendar. Is that correct?
    @PostMapping(value = "/api/calendar/{id}/event", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createEvent(@PathVariable String id, @RequestBody Event event) throws Exception {
        objectMapper = new ObjectMapper();
        var calendar = calendarRepository.findById(id);
        if (calendar.isEmpty()){
            throw new Exception("Calendar not found");
        }
        log.info("calendar for event found " + calendar.get().getCalendarId());
        event.setCalendarId(id);
        eventRepository.save(event);
        log.info("event has been saved. " + event);
        var calendarEvents = getCalendarEvents(calendar);
        calendarEvents[calendarEvents.length -1] = event.getEventId();
        log.info("event Id added to calendar " + Arrays.toString(calendarEvents));
        calendar.get().setEventIds(Arrays.toString(calendarEvents));
        log.info("New calendar attributes being saved " + calendar);
        calendarRepository.save(calendar.get());
        log.info("New calendar event saved.");
    }

    // Similar scenario -- I wasn't sure if I should use calendarId in place of {id}
    @PutMapping(value = "/api/event/{id}")
    public void updateEvent(@PathVariable String calendarId, Event event) {
    }

    @DeleteMapping(value = "/api/event/{id})")
    public void deleteEvent(@PathVariable String calendarId) {
    }


    private String[] getCalendarEvents(Optional<Calendar> calendar){
        return calendar.get().getEventIds().replace(",", "").
                replace("[","").replace("]", "").split(" ");
    }
}
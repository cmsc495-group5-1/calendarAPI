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

    @GetMapping(value = "/api/calendar/{id}/event", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Object> getAllEventsForCalendar(@PathVariable String id) {
        var calendar = calendarRepository.findById(id);
        //TODO: Assert calendar is owned by authenticated user
        var eventQueries = getCalendarEvents(calendar);
        var events = new ArrayList<>();
        for (String eventString : eventQueries){
            log.info("searching for new event " + eventString);
            var event = eventRepository.findById(eventString);
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
        var calendar = calendarRepository.findById(id);
        if (calendar.isEmpty()){
            throw new Exception("Calendar not found");
        }
        log.info("calendar for event found " + calendar.get().getCalendarId());
        event.setCalendarId(id);
        eventRepository.save(event);
        log.info("event has been saved. " + event);
        var calendarEvents = getCalendarEvents(calendar);
        var newCalendarEvents = new String[calendarEvents.length+1];
        var count = 0;
        for (String calendarEvent : calendarEvents){
            newCalendarEvents[count] = calendarEvent;
            count++;
        }
        newCalendarEvents[newCalendarEvents.length -1] = event.getEventId();
        log.info("event Id added to calendar " + Arrays.toString(calendarEvents));
        calendar.get().setEventIds(Arrays.toString(newCalendarEvents));
        log.info("New calendar attributes being saved " + calendar);
        calendarRepository.save(calendar.get());
        log.info("New calendar event saved.");
    }

    // Similar scenario -- I wasn't sure if I should use calendarId in place of {id}
    @PutMapping(value = "/api/calendar/{calendarId}/event/{eventId}")
    public void updateEvent(@PathVariable String calendarId, @PathVariable String eventId, @RequestBody Event event) throws Exception {
        var calendar = calendarRepository.findById(calendarId);
        if (!calendar.get().getEventIds().contains(eventId)) {
            throw new Exception("This event is not listed within the calendar you have open");
        }
        //TODO: Assert calendar is owned by authenticated user
        var oldEvent = eventRepository.findById(eventId);
        if (oldEvent.isEmpty()){
            throw new Exception("event does not exist");
        }

        if (!event.getEventId().equals(eventId)){
            throw new Exception("The event being replaced has a different event Id");
        }
        eventRepository.save(event);
    }

    @DeleteMapping(value = "/api/calendar/{calendarId}/event/{eventId}")
    public void deleteEvent(@PathVariable String calendarId, @PathVariable String eventId) throws Exception {
        var calendar = calendarRepository.findById(calendarId);
        if (!calendar.get().getEventIds().contains(eventId)) {
            throw new Exception("This event is not listed within the calendar you have open");
        }

        //TODO: Assert calendar is owned by authenticated user
        var oldEvent = eventRepository.findById(eventId);
        if (oldEvent.isEmpty()){
            throw new Exception("event does not exist");
        }

        if (!oldEvent.get().getEventId().equals(eventId)){
            throw new Exception("The event being replaced has a different event Id");
        }
        var calendarEvents = getCalendarEvents(calendar);
        var newCalendarEvents = new ArrayList<String>();
        for (String eventString : calendarEvents){
            log.info("searching for event to remove " + eventString);
            if (!eventString.matches(eventId)) {
                newCalendarEvents.add(eventString);
                log.info("event not found" + eventString);
            } else {
                log.info("event found, removing.");
            }
        }
        calendar.get().setEventIds(newCalendarEvents.toString());
        log.info("events removed " + newCalendarEvents);
        eventRepository.deleteById(oldEvent.get().getEventId());
        calendarRepository.save(calendar.get());
    }


    private String[] getCalendarEvents(Optional<Calendar> calendar){
        return calendar.get().getEventIds().replace(",", "").
                replace("[","").replace("]", "").split(" ");
    }
}
package calendar.api.controller;

import calendar.api.dto.Calendar;
import calendar.api.dto.Event;
import calendar.api.repository.CalendarRepository;
import calendar.api.repository.EventRepository;
import calendar.api.repository.UserRepository;
import calendar.api.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private JobService jobService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    EventRepository eventRepository;

    ArrayList<Event> eventJobQueue;

    //Test Only
    @GetMapping(value = "/api/notifications/run-job", produces = MediaType.APPLICATION_JSON_VALUE)
    public String runOneTimeJob(@RequestParam(value = "name") String name) {

        jobScheduler.enqueue(() -> jobService.execute(name));
        return "Event has been made.";

    }

    //Test Only
    @GetMapping(value = "/api/notifications/schedule-job", produces = MediaType.APPLICATION_JSON_VALUE)
    public String scheduledOneTimeJob(
            @RequestParam(value = "eventName") String name,
            @RequestParam(value = "eventTime") String when) {

        jobScheduler.schedule(
                Instant.now().plus(Duration.parse(when)),
                () -> jobService.execute(name)
        );

        return "Event has been made. You will be notified before you event occurs.";
    }

    @GetMapping(value = "/api/notifications/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Event> getEvents(@PathVariable String id){
        var user = userRepository.findById(id);
        var calendars = user.get().getCalendarIds().replace(",", "").
                replace("[","").replace("]", "").split(" ");
        var newQueue = new ArrayList<Event>();
        for (String calendarId : calendars) {
            var calendar = calendarRepository.findById(calendarId);
            var oldQueue = calendar.get().getEventIds().replace(",", "").
                    replace("[","").replace("]", "").split(" ");

            for (String eventString : oldQueue) {
                var event = eventRepository.findById(eventString);
                if ((event.get().getEndDateTime().after(Date.from(Instant.now())))) {
                    log.info("adding event to queue " + event.get());
                    newQueue.add(event.get());
                }
            }
        }

        return newQueue;
    }


}

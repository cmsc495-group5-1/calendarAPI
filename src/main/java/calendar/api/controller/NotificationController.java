package calendar.api.controller;

import calendar.api.service.JobService;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
public class NotificationController {

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private JobService jobService;

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





}

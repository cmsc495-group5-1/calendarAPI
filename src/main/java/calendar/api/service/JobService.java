package calendar.api.service;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobService {

    @Job(name = "Test Job")
    public void execute() {
        execute("Hello world!");
    }

    @Job(name = "Event")
    public void execute(String input) {
        log.info("The event has begun: {}", input);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("Error while executing event", e);
        } finally {
            log.info("Event has finished...");
        }
    }
}

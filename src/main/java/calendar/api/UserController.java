package calendar.api;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping(value = "/user/{id}")
    public CalendarUser getUser(@PathVariable String id) {
    }

    @PutMapping(value = "/api/user/{id}")
    public void updateUser(@PathVariable String id, CalendarUser user) {
    }



    @DeleteMapping(value = "/api/user/{id)")
    public void delete(@PathVariable String id) {

    }

    @PostMapping(value = "/api/user/{id}")
    public void createUser(@PathVariable String id, String ...params) {
    }


}

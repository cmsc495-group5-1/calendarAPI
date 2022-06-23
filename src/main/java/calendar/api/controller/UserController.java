package calendar.api.controller;

import calendar.api.dto.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping(value = "/user/{id}")
    public User getUser(@PathVariable String id) {
        return null;
    }

    @PutMapping(value = "/api/user/{id}")
    public void updateUser(@PathVariable String id, User user) {
    }



    @DeleteMapping(value = "/api/user/{id}")
    public void delete(@PathVariable String id) {

    }

    @PostMapping(value = "/api/user/{id}")
    public void createUser(@PathVariable String id, String ...params) {
    }


}

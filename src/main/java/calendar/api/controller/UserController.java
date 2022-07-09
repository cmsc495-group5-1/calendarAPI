package calendar.api.controller;

import calendar.api.dto.User;
import calendar.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/api/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable String id) throws Exception {
        var user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new Exception("User could not be found");
        }
        return user.get();
    }

    @PutMapping(value = "/api/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable String id, @RequestBody User user) throws Exception {
        var matchingUser = userRepository.findById(id);
        if (matchingUser.isEmpty()){
            throw new Exception("User could not be found");
        }

        userRepository.save(user);
        log.info("User attributes have been updated " + user);
    }



    @DeleteMapping(value = "/api/user/{id}")
    public void delete(@PathVariable String id) throws Exception {
        var user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new Exception("User could not be found");
        }

        userRepository.deleteById(user.get().getUserId());
        log.info("User has been deleted " + user.get());
    }

    @PostMapping(value = "/api/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) throws Exception {
        if (user == null){
            throw new Exception("No parameters were passed to create the user");
        }

        userRepository.save(user);
        log.info("User was Created " + user);
        return user;
    }


}

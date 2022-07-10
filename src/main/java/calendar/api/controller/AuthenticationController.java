package calendar.api.controller;

import calendar.api.dto.User;
import calendar.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.dashboard.server.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/api/auth/login")
    public User login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {
        var user = userRepository.findByUsername(username);
        if (user == null){
            return new User();
        }

        if (user.authenticate(password, user.getPassword())){
            return user;
        } else {
            return new User();
        }
    }

    @PostMapping(value = "/api/auth/create")
    public User create(@RequestParam(value = "username") String username,
                      @RequestParam(value = "password") String password,
                       @RequestParam(value = "passwordConfirmation") String passwordConfirmation) {
        var user = userRepository.findByUsername(username);
        if (user != null){
            log.info("User already exists");
            log.info("Provide a different user name to proceed");
            return new User();
        }

        var newUser = new User();
        newUser.applyPasswordChange(username, password, passwordConfirmation);
        return newUser;
    }
}

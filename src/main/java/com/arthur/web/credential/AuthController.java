package com.arthur.web.credential;

import com.arthur.web.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/auth"})
public class AuthController {
    private AuthRepository repository;

    AuthController(AuthRepository repository) {
        this.repository = repository;
    }
    @GetMapping
    public Auth register(Auth auth) {
        User newUser = new User();
        auth.setUser(newUser);
        return repository.save(auth);
    }
}

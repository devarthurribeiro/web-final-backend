package com.arthur.web.credential;

import com.arthur.web.credential.jwt.JwtTokenProvider;
import com.arthur.web.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.http.ResponseEntity.ok;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/auth"})
public class AuthController {
    private AuthRepository repository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    AuthController(AuthRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/register")
    public Auth register(@RequestBody AuthenticationRequest auth) {
        User newUser = new User();

        return this.repository.save(Auth.builder()
                .username(auth.getUsername())
                .password(this.passwordEncoder.encode(auth.getPassword()))
                .roles(Arrays.asList( "CLIENT"))
                .user(newUser)
                .build()
        );
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            Auth auth = getAuth(username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            model.put("id", auth.getId());
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    private Auth getAuth(String username) {
        return repository.findByUsername(username).map( record -> record).orElseThrow(ResourceNotFoundException::new);
    }
}

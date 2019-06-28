package com.arthur.web.user;

import com.arthur.web.credential.Auth;
import com.arthur.web.product.ProductRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/users"})
public class UserController {
    private UserRepository repository;
    private ProductRepository productRepository;

    UserController(UserRepository userRepository, ProductRepository productRepository) {
        this.repository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List findAll(){
        return repository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity findById(@PathVariable long id){
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = {"/favorites"})
    public ResponseEntity favoritesLoggedUser(@AuthenticationPrincipal Auth auth){
        return repository.findById(auth.getId())
                .map(record -> ResponseEntity.ok().body(record.getFavorites()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User create(@RequestBody User user){
        return repository.save(user);
    }

    @PutMapping(value="/{id}")
    public User update(@PathVariable("id") long id,
                                 @RequestBody User user) {
        return repository.save(user);
    }

    @PutMapping(value="/favorite/{id}")
    public ResponseEntity addFavorite(@PathVariable("id") long id, @AuthenticationPrincipal Auth auth) {
        User user = getUser(auth.getId());
        return productRepository.findById(id)
                .map(record -> {
                    user.getFavorites().add(record);
                    User updated = repository.save(user);
                    return ResponseEntity.ok().body(updated.getFavorites());
                }).orElse(ResponseEntity.notFound().build());
    }

    private User getUser(long id) {
        return repository.findById(id).map( record -> record).orElseThrow(ResourceNotFoundException::new);
    }

}

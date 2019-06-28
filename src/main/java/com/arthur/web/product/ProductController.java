package com.arthur.web.product;

import com.arthur.web.credential.Auth;
import com.arthur.web.user.User;
import com.arthur.web.user.UserRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/products"})
public class ProductController {
    private ProductRepository repository;
    private UserRepository userRepository;

    ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.repository = productRepository;
        this.userRepository = userRepository;
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

    @PostMapping
    public Product create(@RequestBody Product product, @AuthenticationPrincipal Auth auth){
        User user = getUser(auth.getId());
        product.setUser(user);
        return repository.save(product);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity update(@PathVariable("id") long id,
                                 @RequestBody Product product) {
        return repository.findById(id)
                .map(record -> {
                    record.setName(product.getName());
                    record.setDescription(product.getDescription());
                    record.setPrice(product.getPrice());
                    record.setImage(product.getImage());
                    record.setActive(product.isActive());
                    Product updated = repository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity<?> delete(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    private User getUser(long id) {
        return userRepository.findById(id).map( record -> record).orElseThrow(ResourceNotFoundException::new);
    }
}

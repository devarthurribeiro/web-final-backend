package com.arthur.web.user;

import com.arthur.web.credential.Auth;
import com.arthur.web.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "favorites")
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String phone;
    private String course;
    @JsonIgnore
    private String password;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "favorites", joinColumns = { @JoinColumn(name =
            "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "produto_id") })
    @JsonIgnore
    private Set<Product> favorites;
    @OneToOne
    @JoinColumn(name="user_id")
    private Auth auth;
}

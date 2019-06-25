package com.arthur.web.user;

import com.arthur.web.credential.Auth;
import com.arthur.web.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "favorites", joinColumns = { @JoinColumn(name =
            "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "produto_id") })
    private List<Product> favorites = new ArrayList<Product>();
    @OneToOne
    @JoinColumn(name="user_id")
    private Auth auth;
}

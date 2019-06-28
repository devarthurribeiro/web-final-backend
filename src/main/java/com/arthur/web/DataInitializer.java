package com.arthur.web;

import com.arthur.web.category.Category;
import com.arthur.web.category.CategoryRepository;
import com.arthur.web.credential.Auth;
import com.arthur.web.credential.AuthRepository;
import com.arthur.web.product.Product;
import com.arthur.web.product.ProductRepository;
import com.arthur.web.user.User;
import com.arthur.web.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicilizando dados!");

        Category category1 = new Category(null, "Doces");
        categoryRepository.save(category1);

        User newUser = new User();

        newUser.setName("Arthur Ribeiro");
        newUser.setEmail("devarthurribeiro@gmail.com");
        newUser.setCourse("TADS");
        newUser.setPhone("84991230249");

        Auth a = this.authRepository.save(Auth.builder().
                username("admin").
                user(newUser).
                password(this.passwordEncoder.encode("admin"))
                .roles(Arrays.asList( "CLIENT"))
                .build()
        );



        Product product1 = new Product(
                new Long(0),
                "Brigadeiro",
                "Caseiro",
                "https://static.baratocoletivo.com.br/_cps//static/teamb/2017/0119/cp/oferta_14848610518472_5184.jpg",
                1.0,
                true,
                category1,
                newUser
        );

        Product product2 = new Product(
                new Long(0),
                "Coxinha",
                "Delicioso recheio de frango com catupiry e amor.",
                "https://static.baratocoletivo.com.br/2018/0516/oferta_15264946709315_OFERTA.jpg",
                1.0,
                true,
                category1,
                newUser
        );

        Product p = productRepository.save(product1);
        productRepository.save(product2);


        userRepository.save(newUser);
    }
}

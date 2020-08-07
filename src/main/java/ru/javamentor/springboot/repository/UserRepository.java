package ru.javamentor.springboot.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javamentor.springboot.model.User;


import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByName(String name);

    Optional<User>  findByNameAndPassword(String name, String password);
}
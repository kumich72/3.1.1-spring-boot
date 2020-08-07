package ru.javamentor.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role , Long> {
    List<Role> findByUser(User user);
    @Query("SELECT r.name FROM Role r GROUP BY r.name")
    List<String> findRoles();
}

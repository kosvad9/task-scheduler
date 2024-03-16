package com.kosvad9.taskscheduler.repository;

import com.kosvad9.taskscheduler.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);

    @EntityGraph(attributePaths = "tasks")
    Optional<User> findUserById(Long id);
}

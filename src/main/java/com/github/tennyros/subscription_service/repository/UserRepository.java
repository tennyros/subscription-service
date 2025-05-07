package com.github.tennyros.subscription_service.repository;

import com.github.tennyros.subscription_service.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"subscriptions"})
    Optional<User> findWithSubscriptionsById(Long id);

    boolean existsByEmail(String email);
}

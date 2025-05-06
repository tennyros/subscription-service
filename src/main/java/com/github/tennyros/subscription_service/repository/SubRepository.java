package com.github.tennyros.subscription_service.repository;

import com.github.tennyros.subscription_service.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRepository extends JpaRepository<Subscription, Long> {
}

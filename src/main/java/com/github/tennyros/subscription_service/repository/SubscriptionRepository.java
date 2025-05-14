package com.github.tennyros.subscription_service.repository;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("""
            SELECT new com.github.tennyros.subscription_service.dto.response.TopSubscriptions(
                s.serviceName, COUNT(s))
            FROM Subscription s GROUP BY s.serviceName
            ORDER BY COUNT(s) DESC LIMIT 3
    """)
    List<TopSubscriptions> findTop3PopularSubscriptions();

    @Modifying
    @Query("DELETE FROM Subscription s WHERE s.id = :subId AND s.user.id = :userId")
    int deleteByIdAndUserId(@Param("subId") Long subId, @Param("userId") Long userId);

    List<Subscription> findByUserId(Long userId);
}

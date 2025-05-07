package com.github.tennyros.subscription_service.repository;

import com.github.tennyros.subscription_service.repository.projection.TopSubscriptionsProjection;
import com.github.tennyros.subscription_service.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRepository extends JpaRepository<Subscription, Long> {

    @Query("""
            SELECT s.serviceName as serviceName, COUNT(s) as count
            FROM Subscription s GROUP BY s.serviceName
            ORDER BY count DESC LIMIT 3
            """)
    List<TopSubscriptionsProjection> findTop3PopularSubscriptions();

    @Modifying
    @Query("DELETE FROM Subscription s WHERE s.id = :subId AND s.user.id = :userId")
    int deleteByIdAndUserId(@Param("subId") Long subId, @Param("userId") Long userId);

    List<Subscription> findByUserId(Long userId);
}

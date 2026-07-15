package com.serkan.peri.repositorty.consumer;

import com.serkan.peri.entity.consumer.QuoteRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuoteRequestRepository extends JpaRepository<QuoteRequest, UUID> {
    List<QuoteRequest> findAllByOrderByCreatedAtDesc();
    long countByReadFalse();
}

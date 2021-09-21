package com.sarakhman.onlineStore.repository;

import com.sarakhman.onlineStore.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
}

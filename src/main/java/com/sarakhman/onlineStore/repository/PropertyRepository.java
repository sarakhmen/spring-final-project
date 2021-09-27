package com.sarakhman.onlineStore.repository;

import com.sarakhman.onlineStore.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    @Query("SELECT DISTINCT p.propertyName FROM Property p")
    List<String> findDistinctPropertyNames();

    @Query("SELECT DISTINCT p.propertyValue FROM Property p WHERE p.propertyName = ?1")
    List<String> findDistinctPropertyValuesByPropertyName(String propertyName);
}

package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PropertyService {

    private PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public Map<String, Set<String>> findPropertyGroups(){
        Map<String, Set<String>> propertyGroups = new HashMap<>();
        List<String> propertyNames = propertyRepository.findDistinctPropertyNames();
        for(String name : propertyNames){
            Set<String> values = new HashSet<>(propertyRepository.findDistinctPropertyValuesByPropertyName(name));
            propertyGroups.put(name, values);
        }
        return propertyGroups;
    }
}

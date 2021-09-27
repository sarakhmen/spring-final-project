package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Property;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/testDataPropertyService.sql"})
public class PropertyServiceTest {
    @Autowired
    PropertyService propertyService;

    @Test
    public void shouldReturnCorrectPropertyGroups(){
        Map<String, Set<String>> propertyGroups = propertyService.findPropertyGroups();
        Assert.assertEquals(2, propertyGroups.size());
        Assert.assertTrue(propertyGroups.containsKey("Model"));
        Assert.assertTrue(propertyGroups.containsKey("Size"));
        Assert.assertEquals(3, propertyGroups.get("Model").size());
        Assert.assertEquals(2, propertyGroups.get("Size").size());
    }
}

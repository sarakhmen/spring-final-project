package com.sarakhman.onlineStore.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/testDataUserService.sql"})
public class MyUserDetailsServiceTest {
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Test
    public void shouldReturnCorrectUserDetails_WhenEmailExists(){
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("artur.sarahman@gmail.com");
        Assert.assertEquals("artur.sarahman@gmail.com", userDetails.getUsername());
    }
}

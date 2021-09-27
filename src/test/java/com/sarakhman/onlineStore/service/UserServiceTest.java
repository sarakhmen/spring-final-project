package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/testDataUserService.sql"})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void shouldReturnCorrectUser_WhenSaveAndFindByEmail() {
        User expected = User.builder().userName("Artur").email("artur@gmail.com").password("qwerty")
                .active(true).build();
        userService.saveUser(expected);
        Optional<User> actual = userService.findUserByEmail(expected.getEmail());
        Assert.assertNotNull(actual.get());
        Assert.assertEquals(expected.getUserName(), actual.get().getUserName());
    }

    @Test
    public void shouldToggleUserActiveStatus_WhenUsedBanHandler(){
        User user = userService.findUserById(1);
        boolean activeBefore = user.getActive();
        user = userService.banHandler(1);
        boolean activeAfter = user.getActive();
        Assert.assertNotEquals(activeBefore, activeAfter);

        activeBefore = !activeBefore;
        user = userService.banHandler(1);
        activeAfter = user.getActive();
        Assert.assertNotEquals(activeBefore, activeAfter);
    }

    @Test
    public void shouldReturnThreeUsers_WhenFindAllUsers(){
        Page<User> userPage = userService.findAllUsers(PageRequest.of(0, 10));
        Assert.assertEquals(3, userPage.getTotalElements());
    }
}

package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
public class UserServiceImplTest
{

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        List<User> myList = userService.findAll();
        for (User u : myList)
        {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findUserById()
    {
        assertEquals("test admin", userService.findUserById(4).getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        assertEquals(1, userService.findByNameContaining("barnbarn").size());
    }

    @Test
    public void findAll()
    {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void delete()
    {
        userService.delete(4);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void findByName()
    {
        assertEquals ("test barnbarn", userService.findByName("test barnbarn").getUsername());
    }

    @Test
    public void save()
    {
        User u = new User("kristin", "61616", "kristin@hey.com");
        userService.save(u);
        assertEquals(6, userService.findAll().size());
    }

    @Test
    public void update()
    {
        User uu = new User("kristin2", "16161", "k@hey.com");
        userService.update(uu, 4);
        assertEquals("kristin2", userService.findUserById(4).getUsername());
        assertEquals("16161", userService.findUserById(4).getPassword());
    }

    @Test
    public void deleteAll()
    {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}
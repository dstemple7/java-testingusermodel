package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");
        // admin, data, user
        User u1 = new User("test admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(1);
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));
        // data, user
        userList.add(u1);
        User u2 = new User("test cinnamon",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.setUserid(2);
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails()
            .add(new Useremail(u2,
                "cinnamon@mymail.local"));
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails()
            .add(new Useremail(u2,
                "bunny@email.local"));
        // user
        userList.add(u2);
        User u3 = new User("test barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");
        u3.setUserid(3);
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.getUseremails()
            .add(new Useremail(u3,
                "barnbarn@email.local"));
        userList.add(u3);
        User u4 = new User("test puttat",
            "password",
            "puttat@school.lambda");
        u4.setUserid(4);
        u4.getRoles().add(new UserRoles(u4, r2));
        userList.add(u4);
        User u5 = new User("test misskitty",
            "password",
            "misskitty@school.lambda");
        u5.setUserid(5);
        u5.getRoles().add(new UserRoles(u5, r2));
        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listAllUsers() throws Exception
    {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(er, tr);
    }

    @Test
    public void getUserById() throws Exception
    {
        String apiUrl = "/users/user/1";
        Mockito.when(userService.findUserById(1)).thenReturn(userList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(1));

        assertEquals(er, tr);
    }

    @Test
    public void getUserByName() throws Exception
    {
        String apiUrl = "/users/user/name/test%20puttat";
        Mockito.when(userService.findByName("test puttat")).thenReturn(userList.get(3));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(3));

        assertEquals(er, tr);
    }

    @Test
    public void getUserLikeName()throws Exception
    {
        List<User> newList = new ArrayList<>();
        newList.add(userList.get(1));

        String apiUrl = "/users/user/name/like/cinn";
        Mockito.when(userService.findByNameContaining("cinn")).thenReturn(newList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(newList);

        assertEquals(er, tr);
    }

    @Test
    public void addNewUser()throws Exception
    {
        String apiUrl = "/users/user";
        ObjectMapper mapper = new ObjectMapper();

        User nu = new User("ben", "66666", "ben@hell.com");
        nu.setUserid(666);
        String u = mapper.writeValueAsString(nu);

        Mockito.when(userService.save(any(User.class))).thenReturn(nu);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(u);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser()throws Exception
    {
        String apiUrl = "/users/user/4";
        ObjectMapper mapper = new ObjectMapper();

        User uu = new User("bob", "20120", "hi@bob.com");
        String u = mapper.writeValueAsString(uu);

        Mockito.when(userService.update(uu, 69)).thenReturn(uu);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(u);

        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser()throws Exception
    {
        String apiUrl = "/users/user/3";
        ObjectMapper mapper = new ObjectMapper();

        User uu = new User("satan", "61616", "eternity@hell.com");
        String u = mapper.writeValueAsString(uu);

        userList.get(3).setUsername("satan");

        Mockito.when(userService.update(uu, 3)).thenReturn(userList.get(3));

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(u);

        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void deleteUserById()throws Exception
    {
        String apiUrl = "/users/user/2";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
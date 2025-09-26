package com.javafx.librarymanagement;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UserTest {

    @Test
    public void testUserCreation() {
        User u = new User(1, "Sakshi", "sakshi@example.com");

        Assert.assertEquals(u.getId(), 1);
        Assert.assertEquals(u.getName(), "Sakshi");
        Assert.assertEquals(u.getEmail(), "sakshi@example.com");
    }

    @Test
    public void testUserWithEmptyValues() {
        User u = new User(99, "", "");
        Assert.assertEquals(u.getName(), "");
        Assert.assertEquals(u.getEmail(), "");
    }
}

package com.javafx.librarymanagement;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BookTest {

    @Test
    public void testBookCreationAndAvailability() {
        Book b = new Book(1, "Wings of Fire", "APJ Abdul Kalam", 1999, true);

        Assert.assertEquals(b.getId(), 1);
        Assert.assertEquals(b.getTitle(), "Wings of Fire");
        Assert.assertEquals(b.getAuthor(), "APJ Abdul Kalam");
        Assert.assertTrue(b.isAvailable());

        b.setAvailable(false);
        Assert.assertFalse(b.isAvailable());
    }

    @Test
    public void testBookWithEmptyFields() {
        Book b = new Book(99, "", "", 0, true);
        Assert.assertEquals(b.getTitle(), "");
        Assert.assertEquals(b.getAuthor(), "");
        Assert.assertEquals(b.getYear(), 0);
    }
}

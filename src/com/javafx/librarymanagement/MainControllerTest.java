package com.javafx.librarymanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainControllerTest {

    private List<Book> books;
    private List<User> users;
    private List<Loan> loans;

    @BeforeMethod
    public void setUp() {
        books = new ArrayList<>();
        books.add(new Book(1, "Wings of Fire", "APJ Abdul Kalam", 1999, true));
        books.add(new Book(2, "Rich Dad Poor Dad", "Kiyosaki", 2000, true));

        users = new ArrayList<>();
        users.add(new User(1, "Sakshi", "sakshi@mail.com"));
        users.add(new User(2, "Mahammad", "mahammad@mail.com"));

        loans = new ArrayList<>();
    }

    @Test
    public void testBorrowBook() {
        Book book = books.get(0);
        User user = users.get(0);

        Loan loan = new Loan(1, book, user, LocalDate.now(), LocalDate.now().plusDays(7));
        loans.add(loan);
        book.setAvailable(false);

        Assert.assertFalse(book.isAvailable());
        Assert.assertEquals(loans.size(), 1);
    }

    @Test
    public void testReturnBook() {
        Book book = books.get(0);
        User user = users.get(1);

        Loan loan = new Loan(1, book, user, LocalDate.now(), LocalDate.now().plusDays(7));
        loans.add(loan);
        book.setAvailable(false);

        // return flow
        loan.setReturnDate(LocalDate.now());
        book.setAvailable(true);

        Assert.assertTrue(book.isAvailable());
        Assert.assertNotNull(loan.getReturnDate());
    }
}

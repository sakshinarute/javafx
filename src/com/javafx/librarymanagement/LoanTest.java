package com.javafx.librarymanagement;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class LoanTest {

    @Test
    public void testLoanCreationAndReturn() {
        Book book = new Book(1, "Java Basics", "Author", 2020, false);
        User user = new User(1, "Tester", "test@mail.com");

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);

        Loan loan = new Loan(1, book, user, borrowDate, dueDate);

        Assert.assertEquals(loan.getBook().getTitle(), "Java Basics");
        Assert.assertEquals(loan.getUser().getName(), "Tester");
        Assert.assertEquals(loan.getDueDate(), dueDate);

        LocalDate returnDate = borrowDate.plusDays(7);
        loan.setReturnDate(returnDate);

        Assert.assertEquals(loan.getReturnDate(), returnDate);
    }

    @Test
    public void testLoanWithoutBookOrUser() {
        Loan loan = new Loan(99, null, null, null, null);
        Assert.assertNull(loan.getBook());
        Assert.assertNull(loan.getUser());
    }
}


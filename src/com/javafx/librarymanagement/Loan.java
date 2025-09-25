package com.javafx.librarymanagement;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Loan {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final ObjectProperty<Book> book = new SimpleObjectProperty<>(this, "book");
    private final ObjectProperty<User> user = new SimpleObjectProperty<>(this, "user");
    private final ObjectProperty<LocalDate> borrowDate = new SimpleObjectProperty<>(this, "borrowDate");
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>(this, "dueDate");
    private final ObjectProperty<LocalDate> returnDate = new SimpleObjectProperty<>(this, "returnDate");

    public Loan() {}

    // existing 5-arg constructor - keep for compatibility
    public Loan(int id, Book book, User user, LocalDate borrowDate, LocalDate dueDate) {
        this(id, book, user, borrowDate, dueDate, null);
    }

    // new 6-arg constructor (id, book, user, borrowDate, dueDate, returnDate)
    public Loan(int id, Book book, User user, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.id.set(id);
        this.book.set(book);
        this.user.set(user);
        this.borrowDate.set(borrowDate);
        this.dueDate.set(dueDate);
        this.returnDate.set(returnDate);
    }

    // id
    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    // book
    public Book getBook() { return book.get(); }
    public void setBook(Book b) { book.set(b); }
    public ObjectProperty<Book> bookProperty() { return book; }

    // user
    public User getUser() { return user.get(); }
    public void setUser(User u) { user.set(u); }
    public ObjectProperty<User> userProperty() { return user; }

    // borrowDate
    public LocalDate getBorrowDate() { return borrowDate.get(); }
    public void setBorrowDate(LocalDate d) { borrowDate.set(d); }
    public ObjectProperty<LocalDate> borrowDateProperty() { return borrowDate; }

    // dueDate
    public LocalDate getDueDate() { return dueDate.get(); }
    public void setDueDate(LocalDate d) { dueDate.set(d); }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }

    // returnDate
    public LocalDate getReturnDate() { return returnDate.get(); }
    public void setReturnDate(LocalDate d) { returnDate.set(d); }
    public ObjectProperty<LocalDate> returnDateProperty() { return returnDate; }
}
package com.javafx.librarymanagement;

import javafx.beans.property.*;

public class Book {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty title = new SimpleStringProperty(this, "title", "");
    private final StringProperty author = new SimpleStringProperty(this, "author", "");
    private final IntegerProperty year = new SimpleIntegerProperty(this, "year", 0);
    private final BooleanProperty available = new SimpleBooleanProperty(this, "available", true);

    public Book() {}

    public Book(int id, String title, String author, int year, boolean available) {
        this.id.set(id);
        this.title.set(title);
        this.author.set(author);
        this.year.set(year);
        this.available.set(available);
    }

    // id
    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    // title
    public String getTitle() { return title.get(); }
    public void setTitle(String v) { title.set(v); }
    public StringProperty titleProperty() { return title; }

    // author
    public String getAuthor() { return author.get(); }
    public void setAuthor(String v) { author.set(v); }
    public StringProperty authorProperty() { return author; }

    // year
    public int getYear() { return year.get(); }
    public void setYear(int v) { year.set(v); }
    public IntegerProperty yearProperty() { return year; }

    // available
    public boolean isAvailable() { return available.get(); }
    public void setAvailable(boolean v) { available.set(v); }
    public BooleanProperty availableProperty() { return available; }
}

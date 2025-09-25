package com.javafx.librarymanagement;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name", "");
    private final StringProperty email = new SimpleStringProperty(this, "email", "");

    public User() {}

    public User(int id, String name, String email) {
        this.id.set(id);
        this.name.set(name);
        this.email.set(email);
    }

    // id
    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    // name
    public String getName() { return name.get(); }
    public void setName(String v) { name.set(v); }
    public StringProperty nameProperty() { return name; }

    // email
    public String getEmail() { return email.get(); }
    public void setEmail(String v) { email.set(v); }
    public StringProperty emailProperty() { return email; }
}

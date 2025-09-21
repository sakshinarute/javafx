package com.javafx.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();

    public Person(String name, String email) {
        this.name.set(name);
        this.email.set(email);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
}

package com.javafx.views;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DemoController {

    @FXML private TreeView<String> treeView;
    @FXML private TableView<Person> tableView;
    @FXML private TableColumn<Person, String> colName;
    @FXML private TableColumn<Person, String> colEmail;
    @FXML private ListView<String> listView;
    @FXML private Label statusLabel;

    // Observable property for binding
    private StringProperty statusText = new SimpleStringProperty("Ready");

    @FXML
    public void initialize() {
        // Bind statusLabel text to property (Unidirectional Binding)
        statusLabel.textProperty().bind(statusText);

        setupTreeView();
        setupTableView();
        setupListView();

        // Event bubbling example: clicking anywhere in table updates status
        tableView.setOnMouseClicked(e -> 
            statusText.set("Table clicked: " + e.getButton())
        );
    }

    @SuppressWarnings("unchecked")
	private void setupTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Departments");
        rootItem.setExpanded(true);

        TreeItem<String> it1 = new TreeItem<>("Engineering");
        TreeItem<String> it2 = new TreeItem<>("HR");
        TreeItem<String> it3 = new TreeItem<>("Finance");

        rootItem.getChildren().addAll(it1, it2, it3);
        treeView.setRoot(rootItem);

        // Event handling for selection
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) statusText.set("Selected: " + newVal.getValue());
        });
    }

    private void setupTableView() {
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        tableView.setItems(FXCollections.observableArrayList(
            new Person("Alice", "alice@mail.com"),
            new Person("Bob", "bob@mail.com"),
            new Person("Charlie", "charlie@mail.com")
        ));
    }

    private void setupListView() {
        listView.setItems(FXCollections.observableArrayList("Item A", "Item B", "Item C"));

        // Event handling
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) statusText.set("List selected: " + newVal);
        });
    }

    // Bidirectional Binding Example
    public void bindStatusBidirectional(StringProperty externalProperty) {
        statusText.bindBidirectional(externalProperty);
    }
}

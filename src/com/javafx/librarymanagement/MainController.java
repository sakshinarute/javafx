package com.javafx.librarymanagement;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MainController {

    // --------------------- FXML UI ---------------------
    @FXML private BorderPane rootPane;

    // Books
    @FXML private TableView<Book> tableBooks;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, Boolean> colAvailable;

    @FXML private TextField tfTitle;
    @FXML private TextField tfAuthor;
    @FXML private TextField tfYear;
    @FXML private CheckBox cbAvailable;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnLoad;
    @FXML private Button btnSave;

    @FXML private TextField tfSearch;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;

    @FXML private MenuItem menuLoad;
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuExit;

    // Users
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUserName;
    @FXML private TableColumn<User, String> colUserEmail;

    @FXML private TextField tfUserName;
    @FXML private TextField tfUserEmail;
    @FXML private Button btnUserAdd;
    @FXML private Button btnUserUpdate;
    @FXML private Button btnUserDelete;

    // Borrow / Loans
    @FXML private ComboBox<User> cbUsersBorrow;
    @FXML private Button btnBorrow;

    @FXML private TableView<Loan> tableLoans;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, String> colLoanBook;
    @FXML private TableColumn<Loan, String> colLoanUser;
    @FXML private TableColumn<Loan, String> colBorrowDate;
    @FXML private TableColumn<Loan, String> colDueDate;
    @FXML private TableColumn<Loan, String> colReturnDate;
    @FXML private Button btnReturn;

    // --------------------- Data ---------------------
    private final ObservableList<Book> books = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Loan> loans = FXCollections.observableArrayList();
    private FilteredList<Book> filtered;

    // Form properties
    private final StringProperty formTitle = new SimpleStringProperty("");
    private final StringProperty formAuthor = new SimpleStringProperty("");
    private final StringProperty formYear = new SimpleStringProperty("");
    private final BooleanProperty formAvailable2 = new SimpleBooleanProperty(true);

    // --------------------- Initialize ---------------------
    @FXML
    public void initialize() {
        // Books table
        colId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        colTitle.setCellValueFactory(cell -> cell.getValue().titleProperty());
        colAuthor.setCellValueFactory(cell -> cell.getValue().authorProperty());
        colYear.setCellValueFactory(cell -> cell.getValue().yearProperty().asObject());
        colAvailable.setCellValueFactory(cell -> cell.getValue().availableProperty());
        colAvailable.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean available, boolean empty) {
                super.updateItem(available, empty);
                setText(empty ? null : (available ? "Yes" : "No"));
            }
        });

        // Search/filter
        filtered = new FilteredList<>(books, b -> true);
        tfSearch.textProperty().addListener((o, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            filtered.setPredicate(book -> {
                if (q.isEmpty()) return true;
                return String.valueOf(book.getId()).contains(q)
                        || book.getTitle().toLowerCase().contains(q)
                        || book.getAuthor().toLowerCase().contains(q)
                        || String.valueOf(book.getYear()).contains(q);
            });
        });
        SortedList<Book> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableBooks.comparatorProperty());
        tableBooks.setItems(sorted);

        // Form bindings
        tfTitle.textProperty().bindBidirectional(formTitle);
        tfAuthor.textProperty().bindBidirectional(formAuthor);
        tfYear.textProperty().bindBidirectional(formYear);
        cbAvailable.selectedProperty().bindBidirectional(formAvailable2);

        btnUpdate.disableProperty().bind(tableBooks.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(tableBooks.getSelectionModel().selectedItemProperty().isNull());

        tableBooks.getSelectionModel().selectedItemProperty().addListener((obs, oldS, sel) -> {
            if (sel != null) {
                formTitle.set(sel.getTitle());
                formAuthor.set(sel.getAuthor());
                formYear.set(String.valueOf(sel.getYear()));
                formAvailable2.set(sel.isAvailable());
            } else clearForm();
        });

        tableBooks.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getClickCount() == 2) {
                Book sel = tableBooks.getSelectionModel().getSelectedItem();
                if (sel != null) { sel.setAvailable(!sel.isAvailable()); saveBooksAuto(); tableBooks.refresh(); }
            }
        });

        // Users table
        colUserId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colUserName.setCellValueFactory(c -> c.getValue().nameProperty());
        colUserEmail.setCellValueFactory(c -> c.getValue().emailProperty());
        tableUsers.setItems(users);

        btnUserUpdate.disableProperty().bind(tableUsers.getSelectionModel().selectedItemProperty().isNull());
        btnUserDelete.disableProperty().bind(tableUsers.getSelectionModel().selectedItemProperty().isNull());

        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldU, sel) -> {
            if (sel != null) {
                tfUserName.setText(sel.getName());
                tfUserEmail.setText(sel.getEmail());
            } else { tfUserName.clear(); tfUserEmail.clear(); }
        });

        btnUserAdd.setOnAction(e -> onUserAdd());
        btnUserUpdate.setOnAction(e -> onUserUpdate());
        btnUserDelete.setOnAction(e -> onUserDelete());

        cbUsersBorrow.setItems(users);
        cbUsersBorrow.setConverter(new StringConverter<>() {
            @Override public String toString(User user) { return user == null ? "" : user.getName(); }
            @Override public User fromString(String string) { return null; }
        });
        cbUsersBorrow.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(User item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getName()); }
        });

        // Loans table
        colLoanId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colLoanBook.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getBook() != null ? c.getValue().getBook().getTitle() : ""));
        colLoanUser.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUser() != null ? c.getValue().getUser().getName() : ""));
        colBorrowDate.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getBorrowDate() != null ? c.getValue().getBorrowDate().toString() : ""));
        colDueDate.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDueDate() != null ? c.getValue().getDueDate().toString() : ""));
        colReturnDate.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getReturnDate() != null ? c.getValue().getReturnDate().toString() : ""));
        tableLoans.setItems(loans);
        btnReturn.disableProperty().bind(tableLoans.getSelectionModel().selectedItemProperty().isNull());

        // Buttons wiring
        btnAdd.setOnAction(e -> onAdd());
        btnUpdate.setOnAction(e -> onUpdate());
        btnDelete.setOnAction(e -> onDelete());
        btnLoad.setOnAction(e -> loadAllInBackground());
        btnSave.setOnAction(e -> saveAllInBackground());
        if (menuLoad != null) menuLoad.setOnAction(e -> loadAllInBackground());
        if (menuSave != null) menuSave.setOnAction(e -> saveAllInBackground());
        if (menuExit != null) menuExit.setOnAction(e -> Platform.exit());

        btnBorrow.setOnAction(e -> onBorrow());
        btnReturn.setOnAction(e -> onReturn());

        // initial status
        statusLabel.setText("Ready");
        progressBar.setProgress(0);

        // load all data
        loadAllInBackground();
    }

    // --------------------- Books CRUD with Auto-Save ---------------------
    private void onAdd() {
        if (!validateForm()) return;
        int nextId = books.stream().map(Book::getId).max(Comparator.naturalOrder()).orElse(0) + 1;
        int y = parseYear(formYear.get());
        Book b = new Book(nextId, formTitle.get().trim(), formAuthor.get().trim(), y, formAvailable2.get());
        books.add(b);
        statusLabel.setText("Added book: " + b.getTitle());
        clearForm();
        saveBooksAuto();
    }

    private void onUpdate() {
        Book sel = tableBooks.getSelectionModel().getSelectedItem();
        if (sel == null || !validateForm()) return;
        sel.setTitle(formTitle.get().trim());
        sel.setAuthor(formAuthor.get().trim());
        sel.setYear(parseYear(formYear.get()));
        sel.setAvailable(formAvailable2.get());
        tableBooks.refresh();
        statusLabel.setText("Updated book ID " + sel.getId());
        saveBooksAuto();
    }

    private void onDelete() {
        Book sel = tableBooks.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirm Delete"); a.setHeaderText("Delete book?");
        a.setContentText(sel.getTitle() + " by " + sel.getAuthor());
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            books.remove(sel);
            statusLabel.setText("Deleted book ID " + sel.getId());
            clearForm();
            saveBooksAuto();
        }
    }

    private boolean validateForm() {
        if (formTitle.get().trim().isEmpty()) { showAlert("Validation", "Title is required"); return false; }
        if (formAuthor.get().trim().isEmpty()) { showAlert("Validation", "Author is required"); return false; }
        if (!formYear.get().trim().isEmpty()) {
            try { Integer.parseInt(formYear.get().trim()); } catch (NumberFormatException e) { showAlert("Validation", "Year must be a number"); return false; }
        }
        return true;
    }

    private void saveBooksAuto() {
        try { DataStore.saveBooks(List.copyOf(books)); }
        catch (Exception e) { statusLabel.setText("Books auto-save failed: " + e.getMessage()); }
    }

    // --------------------- Users CRUD with Auto-Save ---------------------
    private void onUserAdd() {
        String name = tfUserName.getText().trim(); String email = tfUserEmail.getText().trim();
        if (name.isEmpty()) { showAlert("Validation", "Name is required"); return; }
        int nextId = users.stream().map(User::getId).max(Comparator.naturalOrder()).orElse(0) + 1;
        User u = new User(nextId, name, email);
        users.add(u); statusLabel.setText("Added user: " + u.getName());
        tfUserName.clear(); tfUserEmail.clear();
        saveUsersAuto();
    }

    private void onUserUpdate() {
        User sel = tableUsers.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        String name = tfUserName.getText().trim(); String email = tfUserEmail.getText().trim();
        if (name.isEmpty()) { showAlert("Validation", "Name is required"); return; }
        sel.setName(name); sel.setEmail(email); tableUsers.refresh();
        statusLabel.setText("Updated user ID " + sel.getId());
        saveUsersAuto();
    }

    private void onUserDelete() {
        User sel = tableUsers.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION); a.setTitle("Confirm Delete"); a.setHeaderText("Delete user?");
        a.setContentText(sel.getName() + " (" + sel.getEmail() + ")");
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            users.remove(sel); statusLabel.setText("Deleted user ID " + sel.getId());
            tfUserName.clear(); tfUserEmail.clear();
            saveUsersAuto();
        }
    }

    private void saveUsersAuto() {
        try { DataStore.saveUsers(List.copyOf(users)); }
        catch (Exception e) { statusLabel.setText("Users auto-save failed: " + e.getMessage()); }
    }

    // --------------------- Borrow/Return with Auto-Save Loans ---------------------
    private void onBorrow() {
        Book book = tableBooks.getSelectionModel().getSelectedItem();
        User user = cbUsersBorrow.getSelectionModel().getSelectedItem();
        if (book == null) { showAlert("Borrow", "Select a book to borrow"); return; }
        if (user == null) { showAlert("Borrow", "Select a user who borrows"); return; }
        if (!book.isAvailable()) { showAlert("Borrow", "This book is currently not available"); return; }

        int nextLoanId = loans.stream().map(Loan::getId).max(Comparator.naturalOrder()).orElse(0) + 1;
        LocalDate now = LocalDate.now(); LocalDate due = now.plusWeeks(2);
        Loan loan = new Loan(nextLoanId, book, user, now, due);
        loans.add(loan); book.setAvailable(false);
        tableBooks.refresh(); tableLoans.refresh();
        statusLabel.setText("Book borrowed: " + book.getTitle() + " -> " + user.getName());
        saveLoansAuto(); saveBooksAuto();
    }

    private void onReturn() {
        Loan loan = tableLoans.getSelectionModel().getSelectedItem();
        if (loan == null) return;
        if (loan.getReturnDate() != null) { showAlert("Return", "This loan is already returned."); return; }
        loan.setReturnDate(LocalDate.now());
        if (loan.getBook() != null) loan.getBook().setAvailable(true);
        tableLoans.refresh(); tableBooks.refresh();
        statusLabel.setText("Returned: " + loan.getBook().getTitle());
        saveLoansAuto(); saveBooksAuto();
    }

    private void saveLoansAuto() {
        try { DataStore.saveLoans(List.copyOf(loans)); }
        catch (Exception e) { statusLabel.setText("Loans auto-save failed: " + e.getMessage()); }
    }

    // --------------------- Helpers ---------------------
    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.WARNING); a.setTitle(title); a.setHeaderText(null); a.setContentText(message); a.showAndWait();
    }

    private int parseYear(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }

    private void clearForm() {
        formTitle.set(""); formAuthor.set(""); formYear.set(""); formAvailable2.set(true);
        tableBooks.getSelectionModel().clearSelection();
    }

    // -----------------------------
    // Load / Save for Books+Users+Loans (background tasks)
    // -----------------------------
    private static class LoadBundle {
        final List<Book> books;
        final List<User> users;
        final List<Loan> loans;
        LoadBundle(List<Book> b, List<User> u, List<Loan> l) { this.books = b; this.users = u; this.loans = l; }
    }

    private void loadAllInBackground() {
        Task<LoadBundle> task = new Task<>() {
            @Override
            protected LoadBundle call() throws Exception {
                updateMessage("Loading...");
                List<Book> loadedBooks = DataStore.loadBooks();
                List<User> loadedUsers = DataStore.loadUsers();
                List<Loan> loadedLoans = DataStore.loadLoans(loadedBooks, loadedUsers);

                int n = Math.max(1, loadedBooks.size() + loadedUsers.size() + loadedLoans.size());
                for (int i = 0; i < n; i++) {
                    Thread.sleep(20);
                    updateProgress(i + 1, n);
                }
                updateMessage("Load complete");
                return new LoadBundle(loadedBooks, loadedUsers, loadedLoans);
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            LoadBundle bundle = task.getValue();
            books.setAll(bundle.books);
            users.setAll(bundle.users);
            loans.setAll(bundle.loans);

            // ensure sample data if empty (backwards compatibility with previous demo)
            if (books.isEmpty()) {
                books.addAll(
                        new Book(1, "Effective Java", "Joshua Bloch", 2018, true),
                        new Book(2, "Clean Code", "Robert C. Martin", 2008, true)
                );
            }
            if (users.isEmpty()) {
                users.addAll(
                        new User(1, "Alice", "alice@example.com"),
                        new User(2, "Bob", "bob@example.com")
                );
            }

            // If any loan is active (returnDate == null), mark that book unavailable
            for (Loan l : loans) {
                if (l.getReturnDate() == null && l.getBook() != null) {
                    l.getBook().setAvailable(false);
                }
            }

            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Loaded " + books.size() + " books, " + users.size() + " users, " + loans.size() + " loans");
            progressBar.setProgress(0);
        });

        task.setOnFailed(e -> {
            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Load failed: " + task.getException().getMessage());
            progressBar.setProgress(0);
        });

        new Thread(task, "Load-All-Task").start();
    }

    private void saveAllInBackground() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Saving...");
                List<Book> snapshotBooks = List.copyOf(books);
                List<User> snapshotUsers = List.copyOf(users);
                List<Loan> snapshotLoans = List.copyOf(loans);
                int n = Math.max(1, snapshotBooks.size() + snapshotUsers.size() + snapshotLoans.size());
                for (int i = 0; i < n; i++) {
                    Thread.sleep(15);
                    updateProgress(i + 1, n);
                }
                DataStore.saveBooks(snapshotBooks);
                DataStore.saveUsers(snapshotUsers);
                DataStore.saveLoans(snapshotLoans);
                updateMessage("Save complete");
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Saved " + books.size() + " books, " + users.size() + " users, " + loans.size() + " loans");
            progressBar.setProgress(0);
        });

        task.setOnFailed(e -> {
            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Save failed: " + task.getException().getMessage());
            progressBar.setProgress(0);
        });

        new Thread(task, "Save-All-Task").start();
    }
}

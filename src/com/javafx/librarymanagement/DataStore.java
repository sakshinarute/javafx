package com.javafx.librarymanagement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class DataStore {
    private static final String BOOKS_FILE = "books.csv";
    private static final String USERS_FILE = "users.csv";
    private static final String LOANS_FILE = "loans.csv";

    // -------- BOOKS ----------
    public static List<Book> loadBooks() throws IOException {
        File file = new File(BOOKS_FILE);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        List<Book> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    int year = parts[3].trim().isEmpty() ? 0 : Integer.parseInt(parts[3].trim());
                    boolean available = Boolean.parseBoolean(parts[4].trim());
                    list.add(new Book(id, title, author, year, available));
                } catch (Exception ignored) {}
            }
        }
        return list;
    }

    public static void saveBooks(List<Book> books) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BOOKS_FILE), StandardCharsets.UTF_8))) {
            bw.write("id,title,author,year,available");
            bw.newLine();
            for (Book b : books) {
                bw.write(String.format("%d,%s,%s,%d,%s",
                        b.getId(), safe(b.getTitle()), safe(b.getAuthor()), b.getYear(), Boolean.toString(b.isAvailable())));
                bw.newLine();
            }
        }
    }

    // -------- USERS ----------
    public static List<User> loadUsers() throws IOException {
        File file = new File(USERS_FILE);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        List<User> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    list.add(new User(id, name, email));
                } catch (Exception ignored) {}
            }
        }
        return list;
    }

    public static void saveUsers(List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(USERS_FILE), StandardCharsets.UTF_8))) {
            bw.write("id,name,email");
            bw.newLine();
            for (User u : users) {
                bw.write(String.format("%d,%s,%s", u.getId(), safe(u.getName()), safe(u.getEmail())));
                bw.newLine();
            }
        }
    }

    // -------- LOANS ----------
    public static List<Loan> loadLoans(List<Book> books, List<User> users) throws IOException {
        File file = new File(LOANS_FILE);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        List<Loan> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int bookId = Integer.parseInt(parts[1].trim());
                    int userId = Integer.parseInt(parts[2].trim());
                    LocalDate borrowDate = parts[3].trim().isEmpty() ? null : LocalDate.parse(parts[3].trim());
                    LocalDate dueDate = parts[4].trim().isEmpty() ? null : LocalDate.parse(parts[4].trim());
                    LocalDate returnDate = parts[5].trim().isEmpty() ? null : LocalDate.parse(parts[5].trim());

                    Book book = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
                    User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
                    if (book != null && user != null) {
                        Loan loan = new Loan(id, book, user, borrowDate, dueDate);
                        loan.setReturnDate(returnDate);
                        list.add(loan);
                    }
                } catch (Exception ignored) {}
            }
        }
        return list;
    }

    public static void saveLoans(List<Loan> loans) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LOANS_FILE), StandardCharsets.UTF_8))) {
            bw.write("id,bookId,userId,borrowDate,dueDate,returnDate");
            bw.newLine();
            for (Loan l : loans) {
                bw.write(String.format("%d,%d,%d,%s,%s,%s",
                        l.getId(),
                        l.getBook() != null ? l.getBook().getId() : 0,
                        l.getUser() != null ? l.getUser().getId() : 0,
                        l.getBorrowDate() != null ? l.getBorrowDate() : "",
                        l.getDueDate() != null ? l.getDueDate() : "",
                        l.getReturnDate() != null ? l.getReturnDate() : ""));
                bw.newLine();
            }
        }
    }

    // Replace newlines in strings
    private static String safe(String s) {
        if (s == null) return "";
        return s.replace("\n", " ").replace("\r", " ");
    }
}

package com.javafx.minidashboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DashboardApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Mini Dashboard");
        stage.setScene(scene);
        stage.show();

        // ---- Menu Items ----
        MenuBar menuBar = (MenuBar) scene.lookup(".menu-bar");
        if (menuBar != null) {
            Menu fileMenu = menuBar.getMenus().get(0); // File
            MenuItem exitItem = fileMenu.getItems().get(1); // Exit
            exitItem.setOnAction(e -> Platform.exit());

            Menu helpMenu = menuBar.getMenus().get(1); // Help
            MenuItem aboutItem = helpMenu.getItems().get(0); // About
            aboutItem.setOnAction(e -> showAlert("About", "Mini Dashboard v1.0"));
        }

        // ---- Side Menu Buttons ----
        Button btnHome = (Button) scene.lookup("#btnHome");
        Button btnSettings = (Button) scene.lookup("#btnSettings");
        Button btnLogout = (Button) scene.lookup("#btnLogout");

        if (btnHome != null) {
            btnHome.setOnAction(e -> showAlert("Home", "Home clicked!"));
            btnHome.setOnMouseEntered(e -> btnHome.setStyle("-fx-opacity: 0.8;"));
            btnHome.setOnMouseExited(e -> btnHome.setStyle("-fx-opacity: 1;"));
        }

        if (btnSettings != null) {
            btnSettings.setOnAction(e -> showAlert("Settings", "Settings clicked!"));
            btnSettings.setOnMouseEntered(e -> btnSettings.setStyle("-fx-opacity: 0.8;"));
            btnSettings.setOnMouseExited(e -> btnSettings.setStyle("-fx-opacity: 1;"));
        }

        if (btnLogout != null) {
            btnLogout.setOnAction(e -> Platform.exit());
            btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-opacity: 0.8;"));
            btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-opacity: 1;"));
        }

        // ---- Login Button & Input Fields ----
        Button loginBtn = (Button) scene.lookup(".login-button");
        TextField usernameField = (TextField) scene.lookup("#usernameField");
        PasswordField passwordField = (PasswordField) scene.lookup("#passwordField");

        if (loginBtn != null) {
            loginBtn.setOnAction(e -> {
                String user = usernameField != null ? usernameField.getText() : "";
                String pass = passwordField != null ? passwordField.getText() : "";
                System.out.println("Login clicked: " + user + " / " + pass);
            });

            loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-opacity: 0.8;"));
            loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-opacity: 1;"));
        }
    }

    // ---- Helper to show alert dialogs ----
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

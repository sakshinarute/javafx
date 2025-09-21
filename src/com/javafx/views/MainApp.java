package com.javafx.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("demo.fxml"));
        Scene scene = new Scene(loader.load(), 700, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("JavaFX Views Demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


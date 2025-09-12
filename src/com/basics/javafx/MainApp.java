package com.basics.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML from the same package
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));

        Scene scene = new Scene(root, 400, 200);  
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX + Scene Builder");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

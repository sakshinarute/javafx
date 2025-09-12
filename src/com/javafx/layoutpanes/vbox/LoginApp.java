package com.javafx.layoutpanes.vbox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
       
        Parent root = FXMLLoader.load(getClass().getResource("VBoxExample.fxml"));

       
        Scene scene = new Scene(root);

        
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); 
    }
}


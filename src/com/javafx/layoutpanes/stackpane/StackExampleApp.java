package com.javafx.layoutpanes.stackpane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StackExampleApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("StackExample.fxml")));
		stage.setScene(scene);
		stage.setTitle("Stack Pane Example");
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}

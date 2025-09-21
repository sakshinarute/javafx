package com.javafx.multithreading;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DemoController {

    @FXML private Button btnTask;
    @FXML private Button btnService;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        // Run Task button
        btnTask.setOnAction(e -> runTask());

        // Run Service button
        btnService.setOnAction(e -> runService());
    }

    private void runTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(500); // simulate work
                    updateProgress(i, 10);
                    updateMessage("Processing... " + i * 10 + "%");
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    private void runService() {
        Service<Void> service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        for (int i = 1; i <= 10; i++) {
                            Thread.sleep(500);
                            updateProgress(i, 10);
                            updateMessage("Service Running... " + i * 10 + "%");
                        }
                        return null;
                    }
                };
            }
        };

        progressBar.progressProperty().bind(service.progressProperty());
        statusLabel.textProperty().bind(service.messageProperty());

        service.start();
    }
}

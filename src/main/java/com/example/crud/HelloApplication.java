package com.example.crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("lubi2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 790, 527);
        scene.getStylesheets().add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
        stage.setTitle("ventana");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.example.pingpongjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PingPongGame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PingPongGame.class.getResource("ping-pong-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Puig Pongllar IA vs Human!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
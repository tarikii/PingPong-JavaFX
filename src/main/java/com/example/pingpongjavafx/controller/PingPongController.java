package com.example.pingpongjavafx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PingPongController implements Initializable {

    @FXML
    private Button startButton;
    @FXML
    private Rectangle ball;
    @FXML
    private Rectangle paddleLeft;
    @FXML
    private Rectangle paddleRight;
    @FXML
    private Label scoreLeft;
    @FXML
    private Label scoreRight;
    @FXML
    private Label gameLevel;

    private Timeline timeline;
    private double ballSpeedX = 2;
    private double ballSpeedY = 2;
    private double ballSpeed = 1.5;
    private boolean gameRunning = false;
    private int playerScore = 0;
    private int IAScore = 0;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> moveBall()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        resetBall();
        moveLeftPaddle();
        moveRightPaddle();
    }

    public void moveBall() {
        if (!gameRunning) {
            return;
        }

        // Move the ball
        ball.setLayoutX(ball.getLayoutX() + ballSpeedX);
        ball.setLayoutY(ball.getLayoutY() + ballSpeedY);

        // Check for collisions with the paddles
        if (ball.getBoundsInParent().intersects(paddleRight.getBoundsInParent())) {
            ballSpeedX = -Math.abs(ballSpeedX);
        }
        if (ball.getBoundsInParent().intersects(paddleLeft.getBoundsInParent())) {
            ballSpeedX = Math.abs(ballSpeedX);
        }


        if (ball.getLayoutX() <= 0) {
            resetBall();
            IAScore++;
            scoreRight.setText(Integer.toString(IAScore));
        }
        else if (ball.getLayoutX() + ball.getWidth() >= 600){
            resetBall();
            playerScore++;
            if (playerScore % 5 == 0) {
                ballSpeed += 3;
                String gameLevelText = gameLevel.getText();
                String levelNumberString = gameLevelText.replace("LEVEL ", "");
                int gameLevelValue = Integer.parseInt(levelNumberString);
                gameLevel.setText("LEVEL " + (gameLevelValue + 1));
            }
            scoreLeft.setText(Integer.toString(playerScore));
            ball.setLayoutX(ball.getLayoutX() + ballSpeedX * ballSpeed);
            ball.setLayoutY(ball.getLayoutY() + ballSpeedY * ballSpeed);
        }

        if (ball.getLayoutY() <= 0 || ball.getLayoutY() + ball.getHeight() >= 400) {
            ballSpeedY = -ballSpeedY;
        }
    }

    public void startGame() {
        startButton.setOnAction(event -> {
            initialize(null, null);
            timeline.play();
        });
    }


    private void resetBall() {
        ball.setLayoutX(300 - ball.getWidth() / 2);
        ball.setLayoutY(200 - ball.getHeight() / 2);
        ball.setX(ballSpeedX);
        ball.setY(ballSpeedY);
        gameRunning = true;
    }

    public void moveLeftPaddle() {
        Node paddleLeftNode = paddleLeft;
        paddleLeftNode.setOnMouseDragged(event -> {
            double y = event.getSceneY();
            if (y > 0 && y + paddleLeft.getHeight() < 400) {
                paddleLeft.setLayoutY(y);
            }
        });
    }

    public void moveRightPaddle() {
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), event -> {
            double y = paddleRight.getLayoutY();

            String gameLevelText = gameLevel.getText();
            String levelNumberString = gameLevelText.replace("LEVEL ", "");
            int gameLevelValue = Integer.parseInt(levelNumberString);

            if (ball.getLayoutY() > y) {
                y += Math.random() * 1.5 * gameLevelValue;
            } else if (ball.getLayoutY() < y) {
                y -= Math.random() * 1.5 * gameLevelValue;
            }

            y = Math.max(0, Math.min(y, 400 - paddleRight.getHeight()));
            paddleRight.setLayoutY(y);
        }));
    }
}
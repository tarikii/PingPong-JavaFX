package com.example.pingpongjavafx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
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

    Media score = new Media(new File("src/main/resources/sounds/score.mp3").toURI().toString());
    Media computerHit = new Media(new File("src/main/resources/sounds/computer_hit.mp3").toURI().toString());
    Media playerHit = new Media(new File("src/main/resources/sounds/player_hit.mp3").toURI().toString());
    MediaPlayer mediaPlayerScore = new MediaPlayer(score);
    MediaPlayer mediaPlayerComputerHit = new MediaPlayer(computerHit);
    MediaPlayer mediaPlayerPlayerHit = new MediaPlayer(playerHit);



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

        checkGameOver();

        // Check for collisions with the paddles
        if (ball.getBoundsInParent().intersects(paddleRight.getBoundsInParent())) {
            ballSpeedX = -Math.abs(ballSpeedX);
            mediaPlayerComputerHit.play();
            mediaPlayerComputerHit.seek(Duration.ZERO);
        }
        if (ball.getBoundsInParent().intersects(paddleLeft.getBoundsInParent())) {
            ballSpeedX = Math.abs(ballSpeedX);
            mediaPlayerPlayerHit.play();
            mediaPlayerPlayerHit.seek(Duration.ZERO);
        }


        if (ball.getLayoutX() <= 0) {
            resetBall();
            IAScore++;
            scoreRight.setText(Integer.toString(IAScore));
            mediaPlayerScore.play();
            mediaPlayerScore.seek(Duration.ZERO);
        }
        else if (ball.getLayoutX() + ball.getWidth() >= 600){
            resetBall();
            playerScore++;
            if (playerScore % 2 == 0) {
                ballSpeedX *= 1.1;
                ballSpeedY *= 1.2;

                if(paddleLeft.getHeight() > 30){
                    paddleLeft.setHeight(paddleLeft.getHeight()-15);
                }

                if(paddleRight.getHeight() > 30){
                    paddleRight.setHeight(paddleRight.getHeight()-15);
                }

                System.out.println(paddleRight.getHeight());
                System.out.println(paddleLeft.getHeight());

                String gameLevelText = gameLevel.getText();
                String levelNumberString = gameLevelText.replace("LEVEL ", "");
                int gameLevelValue = Integer.parseInt(levelNumberString);
                gameLevel.setText("LEVEL " + (gameLevelValue + 1));
            }
            mediaPlayerScore.play();
            mediaPlayerScore.seek(Duration.ZERO);
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
            startButton.setVisible(false);
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

    public void checkGameOver() {
        String gameLevelText = gameLevel.getText();
        String levelNumberString = gameLevelText.replace("LEVEL ", "");
        int gameLevelValue = Integer.parseInt(levelNumberString);

        if (gameLevelValue == 10) {
            timeline.stop();
            gameRunning = false;
            resetGame();
        }
    }

    @FXML
    private void showGameOverWindow(String resultGame) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resultGame);
        alert.setHeaderText("GAME OVER!");
        alert.setContentText("You won the game! Congratulations!");
        alert.show();
    }

    public void resetGame() {
        if (playerScore != IAScore) {
            if (playerScore > IAScore) {
                showGameOverWindow("The Human wins!");
            } else {
                showGameOverWindow("Skynet wins!");
            }
        }

        ballSpeedX = 2;
        ballSpeedY = 2;
        ballSpeed = 1.5;
        playerScore = 0;
        IAScore = 0;
        scoreLeft.setText("0");
        scoreRight.setText("0");
        gameLevel.setText("LEVEL 1");
        paddleLeft.setHeight(100);
        paddleRight.setHeight(100);
        paddleLeft.setLayoutY(200 - paddleLeft.getHeight() / 2);
        paddleRight.setLayoutY(200 - paddleRight.getHeight() / 2);
        startButton.setVisible(true);
    }
}
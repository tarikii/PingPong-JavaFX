package com.example.pingpongjavafx.model;
import javafx.scene.shape.Rectangle;


public class Ball extends Rectangle {
    private double speedX;
    private double speedY;
    public Ball(double x, double y) {
        super(x, y, 15, 15);
        setWidth(15);
        setHeight(15);
    }
    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void move() {
        setX(getX() + speedX);
        setY(getY() + speedY);
    }
}

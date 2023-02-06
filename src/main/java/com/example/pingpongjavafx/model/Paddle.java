package com.example.pingpongjavafx.model;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {
    private double speed;

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void move(double direction) {
        setY(getY() + speed * direction);
    }
}

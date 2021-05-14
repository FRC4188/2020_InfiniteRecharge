package frc.robot.utils;

public class Coworking {
    private static Coworking instance;
    public synchronized static Coworking getInstance() {
        if (instance == null) instance = new Coworking();
        return instance;
    }

    public double intakeSpeed;
    public double intakeSet;
}
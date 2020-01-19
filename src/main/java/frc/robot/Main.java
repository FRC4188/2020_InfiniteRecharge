package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * Do NOT add any static variables to this class, or any initialization at all.
 */
public final class Main {

    private Main() {
    }

    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }

}

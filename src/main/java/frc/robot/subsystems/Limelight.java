package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating limelight function.
 */
public class Limelight extends SubsystemBase {

    // constants
    private static final double CAMERA_HEIGHT = 240; // pixels
    private static final double CAMERA_WIDTH = 320; // pixels
    private static final double CAMERA_FOV_HOR = Math.toRadians(59.6); // rads
    private static final double CAMERA_FOV_VER = Math.toRadians(49.7); // rads
    private static final double FEET_TO_METERS = 0.3048;
    private static final double RAD_TO_DEGREES = 180 / Math.PI;
    private static final double FORMULA_RATIO = 55.029;
    private static final double PORT_HEIGHT = 8.1875; // meters
    private static final double TAPE_HEIGHT = 2.5 / 2.0; // between bottom and top, feet
    private static final double SHOOTER_HEIGHT = 43.0 / 12.0; // feet
    private static final double HEIGHT_DIFF = PORT_HEIGHT - SHOOTER_HEIGHT; // feet
    private static final double CAMERA_ANGLE = 30; // degrees
    private static final double MULTIPLIER = 0.88; // offset for distance reader

    // state vars
    private NetworkTable limelightTable = null;
    private double dist;
    private double angDist;

    /**
     * Enum to control LED mode.
     */
    public enum LedMode {
        DEFAULT(0), OFF(1), BLINK(2), ON(3);

        private final int value;
        LedMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }

    /**
     * Enum to control camera mode.
     */
    public enum CameraMode {
        VISION(0), CAMERA(1);

        private final int value;
        CameraMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    /**
     * Constructor for Limelight.
     */
    public Limelight() {
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
        setDist();
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Formula RPM", formulaRpm());
        SmartDashboard.putNumber("Direct line distance", dist);
        SmartDashboard.putNumber("Horizontal distance", angDist);
    }

    /**
     * Sets the LED mode of the camera.
     * @param mode - the LED mode to set the camera to
     */
    public void setLightMode(LedMode mode) {
        limelightTable.getEntry("ledMode").setNumber(mode.getValue());
    }

    /**
     * Sets the camera mode of the camera.
     * @param mode - the camera mode to set the camera to
     */
    public void setCameraMode(CameraMode mode) {
        limelightTable.getEntry("camMode").setNumber(mode.getValue());
    }

    /**
     * Returns true if the camera sees a target.
     */
    public boolean hasTarget() {
        return limelightTable.getEntry("tv").getBoolean(false);
    }

    /**
     * Returns the vertical angle from the center of the camera to the target.
     */
    public double getVerticalAngle() {
        return limelightTable.getEntry("ty").getDouble(0.0);
    }

    /**
     * Returns the horizontal angle from the center of the camera to the target.
     */
    public double getHorizontalAngle() {
        return limelightTable.getEntry("tx").getDouble(0.0);
    }

    /**
     * Returns distance in meters from object of height s (feet). Uses s = r(theta).
     */
    public double getDistance(double objectHeight) {
        double boxHeight = limelightTable.getEntry("tvert").getDouble(0.0); // pixels
        if (boxHeight == 0) return 0;
        double output =
                objectHeight / (2 * Math.tan(boxHeight * CAMERA_FOV_VER / (2 * CAMERA_HEIGHT)));
        return output / MULTIPLIER; // from front of bot
    }

    /**
     * Returns rpm to spin shooter to based on vision target formula.
     */
    public double formulaRpm() {
        return (FORMULA_RATIO * angDist) + 4349.71;
    }

    /**
     * Start tracking the vision targets.
     */
    public void trackTarget() {
        setLightMode(LedMode.ON);
        setCameraMode(CameraMode.VISION);
    }

    /**
     * Use LimeLight as camera.
     */
    public void useAsCamera() {
        setLightMode(LedMode.OFF);
        setCameraMode(CameraMode.CAMERA);
    }

    /**
     * Sets distance fields.
     */
    public void setDist() {
        dist = getDistance(TAPE_HEIGHT);
        angDist = dist * Math.tan((getVerticalAngle() + CAMERA_ANGLE) / RAD_TO_DEGREES);
    }

}

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
    private static final double CLOSE_FORMULA_RATIO = 1952;
    private static final double MID_FORMULA_RATIO = -903;
    private static final double FAR_FORMULA_RATIO = 65.4;
    private static final double SUPER_FAR_FORMULA_RATIO = 80.9;
    private static final double PORT_HEIGHT = 8.1875; // feet
    private static final double TAPE_HEIGHT = 2.5 / 2.0; // between bottom and top, feet
    private static final double SHOOTER_HEIGHT = 3.0 + (0.5 / 12.0); // feet
    private static final double HEIGHT_DIFF = PORT_HEIGHT - SHOOTER_HEIGHT; // feet
    private static final double CAMERA_ANGLE = 11.663; // degrees
    private static final double DIRECT_TO_FLAT_DISTANCE =
            1 / Math.cos(Math.toRadians(CAMERA_ANGLE));

    // state vars
    private NetworkTable limelightTable = null;
    private Pipeline pipeline = Pipeline.CLOSE;

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
     * Enum to control Limelight's pipeline.
     */
    public enum Pipeline {
        CLOSE(0), ZOOM(1), OFF(2);

        private final int value;

        Pipeline(int value) {
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
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Formula RPM", formulaRpm());
        SmartDashboard.putNumber("Limelight distance reading", getDistance());
        SmartDashboard.putNumber("Vertical Angle", getVerticalAngle());
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
     * Sets the pipeline of the camera.
     * @param pl - the camera mode to set the camera to
     */
    public void setPipeline(Pipeline pl) {
        limelightTable.getEntry("pipeline").setNumber(pl.getValue());
        pipeline = pl;
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
        double r = limelightTable.getEntry("ty").getDouble(0.0);
        return r + (0.164*r + 0.102) + CAMERA_ANGLE;
    }

    /**
     * Returns the horizontal angle from the center of the camera to the target.
     */
    public double getHorizontalAngle() {
        return limelightTable.getEntry("tx").getDouble(0.0);
    }

    /**
     * Returns horizontal distance in feet from the target.
     */
    public double getDistance() {
        double dist = HEIGHT_DIFF/(Math.tan(Math.toRadians(getVerticalAngle()))) + 0.2;
        return dist; //* DIRECT_TO_FLAT_DISTANCE;
    }

    /**
     * Returns rpm to spin shooter to based on vision target formula.
     */
    public double formulaRpm() {
        /*if (getDistance() <= 10) {
            return (CLOSE_FORMULA_RATIO * getDistance() - 18190);
        } else if (getDistance() > 10 && getDistance() <= 13) {
            return (MID_FORMULA_RATIO * getDistance() + 17326);
        } else if (getDistance() > 13 && getDistance() <= 35) {
            return (FAR_FORMULA_RATIO * getDistance()) + 2320;
        } else if (getDistance() > 35) {
            return (SUPER_FAR_FORMULA_RATIO * getDistance() + 2290);
        } else {
            return 0;
        }*/
        double d = getDistance();
        double rpm = 8200 + -500*d + 11.0*d*d;
        return rpm;
    }

    /**
     * Zooms in on target.
     */
    public void zoomTarget() {
        setLightMode(LedMode.ON);
        setCameraMode(CameraMode.VISION);
        setPipeline(Pipeline.ZOOM);
    }

    /**
     * Start tracking the vision targets.
     */
    public void trackTarget() {
        setLightMode(LedMode.ON);
        setCameraMode(CameraMode.VISION);
        setPipeline(Pipeline.CLOSE);
    }

    /**
     * Use LimeLight as camera.
     */
    public void useAsCamera() {
        setLightMode(LedMode.OFF);
        setCameraMode(CameraMode.CAMERA);
        setPipeline(Pipeline.CLOSE);
    }

    /**
     * Returns the pipeline the camera is running.
     */
    public Pipeline getPipeline() {
        return pipeline;
    }

}

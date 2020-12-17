package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
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
    private static final double PORT_HEIGHT = 8.1875; // feet
    private static final double TAPE_HEIGHT = 2.5 / 2.0; // between bottom and top, feet
    private static final double SHOOTER_HEIGHT = 3.0 + (0.5 / 12.0); // feet
    private static final double HEIGHT_DIFF = PORT_HEIGHT - SHOOTER_HEIGHT; // feet
    private static final double CAMERA_ANGLE = 11.663; // degrees
    private static final double DIRECT_TO_FLAT_DISTANCE =
            1 / Math.cos(Math.toRadians(CAMERA_ANGLE));
    public static final double PORT_SIDE_LENGTH = 2.5 / (2 * Math.sin(Math.toRadians(60))); //Side length of the port in feet.
    public static final double THREE_POINT_DEPTH = 2 + (5.25/12.0); //Depth of the 3 point goal inside the 2 point goal in feet.
    public static final double OFFSET_LIMIT = Math.atan(PORT_SIDE_LENGTH / (THREE_POINT_DEPTH * 2)) + 2; //Limit for the skew against the 3-point goal where any farther would make the ball hit the wall.


    // state vars
    private NetworkTable limelightTable = null;
    private Pipeline pipeline = Pipeline.CLOSE;

    private double adjust;
    private double setRPM;

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
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight-csp");
        SmartDashboard.putNumber("Set shooter speed", 0.0);        
        Notifier shuffle = new Notifier(() -> updateShuffleboard());        
        shuffle.startPeriodic(0.1);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Formula RPM", formulaRpm());
        SmartDashboard.putNumber("Limelight distance reading", getDistance());
        SmartDashboard.putNumber("Vertical Angle", getVerticalAngle());
        SmartDashboard.putBoolean("Is Aimed", getIsAimed());
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
    public double hasTarget() {
        return limelightTable.getEntry("tv").getDouble(0.0);
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
        adjust = SmartDashboard.getNumber("Turret Aim adjust", -2.0);
        return limelightTable.getEntry("tx").getDouble(-adjust);
    }

    public double getSkew() {
        double rawVal =  limelightTable.getEntry("ts").getDouble(0.0);

        rawVal *= -2;
        rawVal = (rawVal > 90) ? (90-rawVal) : rawVal;

        return rawVal;
    }

    public double getOffset() {
        double a = THREE_POINT_DEPTH;
        double b = getDistance();
        double c = getSkew();
    
    
        double offset =  Math.toDegrees(Math.asin((a * Math.sin(Math.toRadians(180-c))) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) - (2 * a * b * Math.cos(Math.toRadians(180 - c))))));
        double check = c - offset;
        if (check <= OFFSET_LIMIT && check >= -OFFSET_LIMIT) return offset;
        else return 0.0;
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
        //setRPM = (Math.pow(1.28449e-5, (getDistance() - 8.69353))) + (2.97767 * Math.pow((getDistance()+4.28663), 2)) + (-124.827 * (getDistance()-32.6752)) - 9.2825e7;
        setRPM = (Math.pow(2.50852, ((0.135494 * getDistance()) + 3.20058)) + (7.7212e15 * Math.pow(getDistance(), -13.0483)) + 3209.97);
        
        if (setRPM > 6000) {
            setRPM = 6000;
        } else if (setRPM < 0) {
            setRPM = 0;
        }

        return setRPM;
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
    public boolean getIsAimed() {
        return (getHorizontalAngle() >= -1.5 && getHorizontalAngle() <= 1.5) && hasTarget() == 1.0;

    }
    /**
     * Returns the pipeline the camera is running.
     */
    public Pipeline getPipeline() {
        return pipeline;
    }

}

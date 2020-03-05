package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating turret function.
 */
public class Turret extends SubsystemBase {

    // device initialization
    private final CANSparkMax turretMotor = new CANSparkMax(23, MotorType.kBrushless);
    private final CANEncoder turretEncoder = new CANEncoder(turretMotor);
    private final CANPIDController pid = new CANPIDController(turretMotor);

    // constants
    private static final double MAX_VELOCITY = 11000; // rpm
    private static final double MAX_ACCELERATION = 22000; // rpm / sec
    private static final double kP = 4e-5;
    private static final double kI = 1e-6;
    private static final double kD = 0;
    private static final double kF = 1.0 / MAX_VELOCITY;
    private static final double kI_ZONE = 0;
    private static final double GEAR_RATIO = 300; // angular velocity will be divided by this amount
    private static final double ENCODER_TO_DEGREES = 360.0 / GEAR_RATIO; // degrees
    private static final double RAMP_RATE = 0.5; // seconds
    private static final double MAX_ANG = 370;
    private static final double MIN_ANG = -10;

    private boolean isTracking;

    /**
     * Constructs new Turret object and configures devices.
     */
    public Turret() {
        //resetEncoders();
        setRampRate();
        controllerInit();
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
    private void updateShuffleboard() {
        SmartDashboard.putNumber("Turret pos (deg)", getPosition());
        SmartDashboard.putNumber("Turret temp", turretMotor.getMotorTemperature());
        SmartDashboard.putNumber("Turret raw vel", turretEncoder.getVelocity());
        SmartDashboard.putBoolean("Auto Aim", isTracking);
    }

    /**
     * Configures gains for Spark closed loop controller.
     */
    private void controllerInit() {
        pid.setP(kP);
        pid.setI(kI);
        pid.setD(kD);
        pid.setIZone(kI_ZONE);
        pid.setFF(kF);
        pid.setOutputRange(-1.0, 1.0);
        pid.setSmartMotionMaxVelocity(MAX_VELOCITY, 0);
        pid.setSmartMotionMaxAccel(MAX_ACCELERATION, 0);
    }

    /**
     * Sets turret motor to given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        turretMotor.set(percent);
    }

    /**
     * Turns turret to angle in degrees.
     */
    public void setAngle(double angle) {
        angle /= ENCODER_TO_DEGREES;
        pid.setReference(angle, ControlType.kSmartMotion);
    }

    /**
     * Configures motor ramp rates.
     */
    public void setRampRate() {
        turretMotor.setClosedLoopRampRate(0);
        turretMotor.setOpenLoopRampRate(RAMP_RATE);
    }

    /**
     * Resets turret encoder value to 0.
     */
    public void resetEncoders() {
        turretEncoder.setPosition(0);
    }

    /**
     * Returns turret encoder position in degrees.
     */
    public double getPosition() {
        return turretEncoder.getPosition() * ENCODER_TO_DEGREES;
    }

    /**
     * Returns turret encoder velocity in degrees per second.
     */
    public double getVelocity() {
        return turretEncoder.getVelocity() * ENCODER_TO_DEGREES / 60.0;
    }

    /**
     * Returns max position of turret in degrees.
     */
    public double getMaxPosition() {
        return MAX_ANG;
    }

    /**
     * Returns min position of turret in degrees.
     */
    public double getMinPosition() {
        return MIN_ANG;
    }

    /**
     * Returns temperature of motor based off CANSpark ID.
     */
    public double getMotorTemperature() {
        return turretMotor.getMotorTemperature();
    }

    /**
     * Sets the isTracking variable (for SmartDashboard purposes).
     */
    public void setTracking(boolean track) {
        isTracking = track;
    }
}
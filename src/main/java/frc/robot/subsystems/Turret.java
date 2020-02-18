package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating turret function.
 */
public class Turret extends SubsystemBase {

    // device initialization
    private final CANSparkMax turretMotor = new CANSparkMax(31, MotorType.kBrushless);
    private final CANEncoder turretEncoder = new CANEncoder(turretMotor);
    private CANPIDController pidC = turretMotor.getPIDController();

    // constants
    private static final double GEAR_RATIO = 300; // angular velocity will be divided by this amount
    private static final double ENCODER_TO_DEGREES = 360.0 / GEAR_RATIO; // degrees
    private static final double RAMP_RATE = 0.2; // seconds
    private final double MAX_VELOCITY = 11000; // rpm
    private final double kP = 5e-5;
    private final double kI = 1e-5;
    private final double kD = 0.03;
    private final double kF = 0;
    private final double kI_ZONE = 0;
    private final int SLOT_ID = 0;
    private final double  MAX_OUT = 1.0; // percent out
    private final double MIN_ANG = 0;
    private final double MAX_ANG = 360;

    /**
     * Constructs new Turret object and configures devices.
     */
    public Turret() {
        resetEncoders();
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
     * Configures gains for Spark closed loop controller. 
     */
    private void controllerInit() {
        pidC.setP(kP);
        pidC.setI(kI);
        pidC.setD(kD);
        pidC.setFF(kF);
        pidC.setIZone(kI_ZONE);
        pidC.setOutputRange(-MAX_OUT, MAX_OUT);
        pidC.setSmartMotionMaxVelocity(MAX_VELOCITY, SLOT_ID);
        pidC.setSmartMotionMinOutputVelocity(0, SLOT_ID);
    }

    /**
     * Writes values to Shuffleboard.
     */
    private void updateShuffleboard() {
        SmartDashboard.putNumber("Turret pos (deg)", getPosition());
        SmartDashboard.putNumber("Turret temp", turretMotor.getMotorTemperature());
    }

    /**
     * Sets turret motor to given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        turretMotor.set(percent);
    }

    /**
     * Configures motor ramp rates.
     */
    public void setRampRate() {
        turretMotor.setClosedLoopRampRate(RAMP_RATE);
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
     * Drives turret motor to given angle in degrees (counterclockwise from 0 at the front of the robot). 
     */
    public void turretToAngle(double angle, double tolerance) {
        // convert from radians to rotations (Spark units)
        angle /= ENCODER_TO_DEGREES;
        tolerance /= ENCODER_TO_DEGREES;
        pidC.setSmartMotionAllowedClosedLoopError(tolerance, SLOT_ID);
        pidC.setReference(angle, ControlType.kSmartMotion);
    }

    public boolean getSpin() {
        if (getPosition() <= MIN_ANG || getPosition() >= MAX_ANG) return true;
        return false;
    }

}

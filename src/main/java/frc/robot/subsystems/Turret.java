package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating turret function.
 */
public class Turret extends SubsystemBase {

    // device initialization
    private final CANSparkMax turretMotor = new CANSparkMax(23, MotorType.kBrushless);
    private final CANEncoder turretEncoder = new CANEncoder(turretMotor);
    private final ProfiledPIDController pid = new ProfiledPIDController(0.021, 0.0, 0.0, new Constraints(MAX_VELOCITY, MAX_ACCELERATION)); //at > 12.3 V

    // constants
    private static final double MAX_VELOCITY = 1.5; // rpm
    private static final double MAX_ACCELERATION = 0.75; // rpm / s ec
    private static final double GEAR_RATIO = 40.0 * (120.0 / 16.0); // angular velocity will be divided by this amount
    private static final double ENCODER_TO_DEGREES = 360.0 / GEAR_RATIO; // degrees
    private static final double RAMP_RATE = 0.5; // seconds
    private static final double MAX_ANG = 255;
    private static final double MIN_ANG = -32;

    private double reduction = 1.0;

    private boolean MANUAL = false;

    /**
     * Constructs new Turret object and configures devices.
     */
    public Turret() {
        resetEncoders();
        setRampRate();
        turretMotor.setIdleMode(IdleMode.kBrake);
        Notifier shuffle = new Notifier(() -> updateShuffleboard());        
        //shuffle.startPeriodic(0.1);

        SmartDashboard.putNumber("Set Turret Angle", 0.0);
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
    private void updateShuffleboard() {
        SmartDashboard.putNumber("Turret pos (deg)", getPosition());
        SmartDashboard.putNumber("Turret temp", turretMotor.getMotorTemperature());
        //SmartDashboard.putNumber("Turret raw vel", turretEncoder.getVelocity());
    }

    public void updatePID() {
        //pid.setPID(
        //SmartDashboard.getNumber("Turret P", 0.0), 
        //SmartDashboard.getNumber("Turret I", 0.0), 
        //SmartDashboard.getNumber("Turret D", 0.0)
        //);
    }

    /**
     * Sets turret motor to given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        if (getPosition() < MIN_ANG){
            if (percent > 0.0) turretMotor.set(percent);
            else turretMotor.set(0.0);
        } else if (getPosition() > MAX_ANG) {
            if (percent < 0.0) turretMotor.set(percent);
            else turretMotor.set(0.0);
        } else {
            turretMotor.set(percent * reduction);
        }
    }

    public void track(double measurement) {
        set(pid.calculate(measurement, 0.0));
    }

    /**
     * Turns turret to angle in degrees.
     */
    public void setAngle(double angle) {
        set(pid.calculate(getPosition(), angle));
    }

    public void trackTarget(double measure) {
        set(pid.calculate(measure, 0));
    }

    /**
     * Configures motor ramp rates.
     */
    public void setRampRate() {
        turretMotor.setClosedLoopRampRate(0);
        turretMotor.setOpenLoopRampRate(RAMP_RATE);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
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

    public boolean getManual() {
        return MANUAL;
    }

    public void setManual(boolean manual) {
        MANUAL = manual;
    }

    public void toggleManual() {
        MANUAL = !MANUAL;
    }

    /**
     * Returns turret motor temperature in Celsius.
     */
    public double getTemp() {
        return turretMotor.getMotorTemperature();
    }

}

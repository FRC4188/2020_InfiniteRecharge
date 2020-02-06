package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase{

    // device initialization
    private CANSparkMax turretMotor = new CANSparkMax(0, MotorType.kBrushless);
    private CANEncoder turretEncoder = new CANEncoder(turretMotor);
    private CANPIDController pidC = turretMotor.getPIDController();

    // constants
    private final double INITIAL_ANGLE = 0; // degrees; 0 is defined as facing in the forward direction
    private final double GEAR_RATIO = 300; // angular velocity will be divided by this amount
    private final double ENCODER_TO_DEGREES = 360 / (42 * GEAR_RATIO); // degrees
    private final double RAD_TO_DEGREES = 180 / Math.PI;
    private final double RAMP_RATE = 0.5; // seconds
    private final double FLAT_RATE = 0.035; // percent out
    private final double MAX_VELOCITY = 10000; // rpm
    private final double MAX_ACCELERATION = 2000; // this may need to be adjusted
    private final double kP = 5e-5; // PID will most definitely need to be adjusted
    private final double kI = 0;
    private final double kD = 0;
    private final double kF = 0;
    private final double kI_ZONE = 0;
    private final int    SLOT_ID = 0;
    public final double  MAX_OUT = 1.0; // percent out

    public Turret(){
        controllerInit();
        resetEncoders();
        configMotors();
    }

    public void configMotors(){
        turretMotor.setClosedLoopRampRate(0.5);
        turretMotor.setOpenLoopRampRate(0.5);
    }

    @Override
    public void periodic(){
        updateShuffleboard();
    }

    /** Configures gains for Spark closed loop controller. */
    private void controllerInit() {
        pidC.setP(kP);
        pidC.setI(kI);
        pidC.setD(kD);
        pidC.setIZone(kI_ZONE);
        pidC.setFF(kF);
        pidC.setOutputRange(-MAX_OUT, MAX_OUT);
        pidC.setSmartMotionMaxVelocity(MAX_VELOCITY, SLOT_ID);
        pidC.setSmartMotionMaxAccel(MAX_ACCELERATION, SLOT_ID);
        pidC.setSmartMotionMinOutputVelocity(0, SLOT_ID);
    }

    /** Prints necessary info to the dashboard. */
    private void updateShuffleboard(){
        SmartDashboard.putNumber("Turret pos (deg)", getPosition());
        SmartDashboard.putNumber("Turret temp", turretMotor.getMotorTemperature());
    }

    /** Resets turret encoder value to 0. */
    public void resetEncoders() {
        double init = INITIAL_ANGLE / ENCODER_TO_DEGREES;
        turretEncoder.setPosition(init);
    }

    /** Returns turret encoder position in degrees. */
    public double getPosition() {
        return (turretEncoder.getPosition() * ENCODER_TO_DEGREES);
    }

    /** Sets turret motor to given percentage using velocity controller. */
    public void set(double percent) {
        double setpoint = percent * MAX_VELOCITY;
        pidC.setReference(setpoint, ControlType.kVelocity);
    }

    /** Drives turret motor to given angle in degrees (counterclockwise from 0 at the front of the robot). */
    public void turretToAngle(double angle, double tolerance) {
        // convert from radians to rotations (Spark units)
        angle /= ENCODER_TO_DEGREES;
        tolerance /= ENCODER_TO_DEGREES;
        pidC.setSmartMotionAllowedClosedLoopError(tolerance, SLOT_ID);
        pidC.setReference(angle, ControlType.kSmartMotion);
    }
}
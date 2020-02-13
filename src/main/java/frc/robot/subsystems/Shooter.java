package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating shooter function.
 */
public class Shooter extends SubsystemBase {

    // device initialization
    //private WPI_TalonFX shooterMotor = new WPI_TalonFX(27);
    //private WPI_TalonFX shooterSlave = new WPI_TalonFX(26);
    private WPI_TalonFX shooterMotor;
    private WPI_TalonFX shooterSlave;

    // constants
    private static final double MAX_VELOCITY = 20000.0; // talon units per 100ms
    private static final double kP = 0.2;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final double kF = 1023 / MAX_VELOCITY;
    private static final int    SLOT_ID = 0;
    private static final int    TIMEOUT = 10; // ms
    private static final double ENCODER_TICKS_PER_REV = 2048;
    private static final double MAX_RPM = MAX_VELOCITY / ENCODER_TICKS_PER_REV;
    private double speed;

    /**
     * Constructs new Shooter object and configures devices.
     */
    public Shooter() {

        // setup encoders
        shooterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
        shooterSlave.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);

        // configuration
        setCoast();
        controllerInit();
        configMotors();
        SmartDashboard.putNumber("Set shooter speed", 0.0);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
    }

    public void configMotors(){
        shooterMotor.configClosedloopRamp(1);
        shooterMotor.configOpenloopRamp(1);
        shooterSlave.configClosedloopRamp(1);
        shooterSlave.configOpenloopRamp(1);
    }

    /**
     * Writes values to Shuffleboard.
     */
    private void updateShuffleboard() {
        SmartDashboard.putNumber("ShooterMotor velocity", getVelocity());
        SmartDashboard.putNumber("ShooterSlave velocity", shooterSlave.getSelectedSensorVelocity() * 600 / ENCODER_TICKS_PER_REV);
        SmartDashboard.putNumber("ShooterMotor temp", shooterMotor.getTemperature());
        SmartDashboard.putNumber("ShooterSlave temp", shooterSlave.getTemperature());
        speed = SmartDashboard.getNumber("Set shooter speed", 0.0);
    }

    /** 
     * Configures gains for SRX closed loop controller.
     */
    private void controllerInit() {
        shooterMotor.config_kP(SLOT_ID, kP, TIMEOUT);
        shooterMotor.config_kI(SLOT_ID, kI, TIMEOUT);
        shooterMotor.config_kD(SLOT_ID, kD, TIMEOUT);
        shooterMotor.config_kF(SLOT_ID, kF, TIMEOUT);
        shooterSlave.config_kP(SLOT_ID, kP, TIMEOUT);
        shooterSlave.config_kI(SLOT_ID, kI, TIMEOUT);
        shooterSlave.config_kD(SLOT_ID, kD, TIMEOUT);
        shooterSlave.config_kF(SLOT_ID, kF, TIMEOUT);
    }

    /**
     * Sets shooter motors to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        shooterMotor.set(percent);
        shooterSlave.set(percent);
    }

    /**
     * Sets shooter motors to a given velocity in rpm.
     */
    public void setVelocity(double velocity) {
        velocity = (velocity * ENCODER_TICKS_PER_REV) / 600; // native is talon units per 100ms
        shooterMotor.set(ControlMode.Velocity, velocity);
        shooterSlave.set(ControlMode.Velocity, -velocity);
    }

    /**
     * Sets shooter motors to a given velocity in rpm.
     */
    public void setVelocity() {
        double sped = (speed * ENCODER_TICKS_PER_REV) / 600; // native is talon units per 100ms
        shooterMotor.set(ControlMode.Velocity, sped);
        shooterSlave.set(ControlMode.Velocity, -sped);
    }

    /**
     * Sets shooter motors to brake mode.
     */
    public void setBrake() {
        shooterMotor.setNeutralMode(NeutralMode.Brake);
        shooterSlave.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Sets shooter motors to coast mode.
     */
    public void setCoast() {
        shooterMotor.setNeutralMode(NeutralMode.Coast);
        shooterSlave.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Gets shooter motor velocity in rpm.
     */
    public double getVelocity() {
        return shooterMotor.getSelectedSensorVelocity() * 600 / ENCODER_TICKS_PER_REV;
    }

    /**
     * Returns the maximum shooter velocity.
     */
    public double getMaxVelocity(){
        return MAX_RPM;
    }
}
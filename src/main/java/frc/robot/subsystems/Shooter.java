package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating shooter function.
 */
public class Shooter extends SubsystemBase {

    // device initialization
    private WPI_TalonFX leftShooter = new WPI_TalonFX(21);
    private WPI_TalonFX rightShooter = new WPI_TalonFX(22);

    // constants
    private static final double kP = 0.35;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final double MAX_VELOCITY = 21300.0;
    private static final double kF = 1023 / MAX_VELOCITY;
    private static final double ENCODER_TICKS_PER_REV = 2048;
    private static final double RAMP_RATE = 1.0; // seconds

    private double reduction = 1;

    /**
     * Constructs new Shooter object and configures devices.
     */
    public Shooter() {

        SmartDashboard.putNumber("Set Shooter Velocity", 0.0); 
        // inversion
        leftShooter.setInverted(true);

        // setup encoders
        leftShooter.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
        rightShooter.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
        rightShooter.setSensorPhase(true);

        // configuration
        setCoast();
        controllerInit();
        setRampRate();

        Notifier shuffle = new Notifier(() -> updateShuffleboard());        
        //shuffle.startPeriodic(0.1);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        //updateShuffleboard();
    }

    /**
     * Writes values to Shuffleboard.
     */
    private void updateShuffleboard() {
        if (getLeftVelocity() == getRightVelocity()) {
            SmartDashboard.putBoolean("Shooter motors equal speeds", true);
            SmartDashboard.putNumber("Shooter RPM", getRightVelocity());
        } else {
            SmartDashboard.putBoolean("Shooter motors equal speeds", false);
            SmartDashboard.putNumber("Left shooter rpm", getLeftVelocity());
            SmartDashboard.putNumber("Right shooter rpm", getRightVelocity());
        }
        
    }

    /**
     * Configures gains for SRX closed loop controller.
     */
    private void controllerInit() {
        leftShooter.config_kP(0, kP, 10);
        leftShooter.config_kI(0, kI, 10);
        leftShooter.config_kD(0, kD, 10);
        leftShooter.config_kF(0, kF, 10);
        rightShooter.config_kP(0, kP, 10);
        rightShooter.config_kI(0, kI, 10);
        rightShooter.config_kD(0, kD, 10);
        rightShooter.config_kF(0, kF, 10);
    }

    /**
     * Sets shooter motors to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        leftShooter.set(percent);
        rightShooter.set(percent);
    }

    /**
     * Sets shooter motors to a given velocity in rpm.
     */
    public void setVelocity(double velocity) {
        velocity *= ((ENCODER_TICKS_PER_REV) / 600) *reduction;
        leftShooter.set(ControlMode.Velocity, velocity);
        rightShooter.set(ControlMode.Velocity, velocity);
    }

    /**
     * Sets shooter motors to brake mode.
     */
    public void setBrake() {
        leftShooter.setNeutralMode(NeutralMode.Coast);
        rightShooter.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Sets shooter motors to coast mode.
     */
    public void setCoast() {
        leftShooter.setNeutralMode(NeutralMode.Coast);
        rightShooter.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Configures shooter motor ramp rates.
     */
    public void setRampRate() {
        leftShooter.configClosedloopRamp(RAMP_RATE);
        leftShooter.configOpenloopRamp(RAMP_RATE);
        rightShooter.configClosedloopRamp(RAMP_RATE);
        rightShooter.configOpenloopRamp(RAMP_RATE);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    /**
     * Gets left shooter motor velocity in rpm.
     */
    public double getLeftVelocity() {
        return (leftShooter.getSelectedSensorVelocity() * 600) / ENCODER_TICKS_PER_REV;
    }

    /**
     * Gets right shooter motor velocity in rpm.
     */
    public double getRightVelocity() {
        return (rightShooter.getSelectedSensorVelocity() * 600) / ENCODER_TICKS_PER_REV;
    }

    /**
     * Returns left shooter motor temperature in Celcius.
     */
    public double getLeftTemp() {
        return leftShooter.getTemperature();
    }

    /**
     * Returns right shooter motor temperature in Celcius.
     */
    public double getRightTemp() {
        return rightShooter.getTemperature();
    }

}

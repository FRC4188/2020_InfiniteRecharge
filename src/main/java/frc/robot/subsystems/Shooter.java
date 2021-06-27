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
    private static final double kP = 1.0;
    private static final double kI = 0.0;
    private static final double kD = 600.0;
    private static final double MAX_VELOCITY = 21300.0;
    private static final double kF = 0.75 * 1023 / 16220;
    private static final double ENCODER_TICKS_PER_REV = 2048;
    private static final double RAMP_RATE = 0.7; // seconds

    private double reduction = 1;

    /**
     * Constructs new Shooter object and configures devices.
     */
    public Shooter() {

        
        // inversion
        leftShooter.setInverted(true);

        leftShooter.follow(rightShooter);

        // setup encoders
        leftShooter.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
        rightShooter.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
        rightShooter.setSensorPhase(true);

        // configuration
        setCoast();
        controllerInit();
        setRampRate();

        Notifier shuffle = new Notifier(() -> updateShuffleboard());        
        shuffle.startPeriodic(0.1);

        SmartDashboard.putNumber("Set S P", kP);
        SmartDashboard.putNumber("Set S I", kI);
        SmartDashboard.putNumber("Set S D", kD);
        SmartDashboard.putNumber("Set S Velocity", 0.0);
        SmartDashboard.putNumber("Set S Voltage", 0.0);

        SmartDashboard.putNumber("Quantity", 0.0);

        SmartDashboard.putNumber("Formula RPM", 0.0);

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
        SmartDashboard.putNumber("Shooter RPM", getRightVelocity());
        SmartDashboard.putNumber("Left Shooter Temp", getLeftTemp());
        SmartDashboard.putNumber("Right Shooter Temp", getRightTemp());
        
    }

    public void setPIDs(double kkP, double kkI, double kkD) {
        leftShooter.config_kP(0, kkP, 10);
        leftShooter.config_kI(0, kkI, 10);
        leftShooter.config_kD(0, kkD, 10);
        rightShooter.config_kP(0, kkP, 10);
        rightShooter.config_kI(0, kkI, 10);
        rightShooter.config_kD(0, kkD, 10);
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
        leftShooter.setNeutralMode(NeutralMode.Brake);
        rightShooter.setNeutralMode(NeutralMode.Brake);
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

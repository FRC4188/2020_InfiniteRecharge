package frc.robot.utils.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Constants;

/**
 * Class extending TalonFX handling encoder conversions and motor configuration.
 * Simplifies and cleans code.
 */
public class CSPFalcon extends WPI_TalonFX{

    /**
     * Constructs a CSPFalcon object to control a Falcon500 motor.
     * @param canID The configured canID number of the motor.
     */
    public CSPFalcon (int canID) {
        super(canID);
        this.configFactoryDefault();
        this.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    }

    /**
     * Set the PIDF gains for encoder-based closed-control.
     * @param kP Proportional gain; sets power based on current error.
     * @param kI Integral gain; sets power based on the sum of error over time.
     * @param kD Derivative gain; sets power based on current rate of change in error.
     * @param kF Feedforward gain; adds some amount of power to the motor to account for resistance.
     */
    public void setPIDF(double kP, double kI, double kD, double kF) {
        this.config_kF(0, kF);
        setPID(kP, kI, kD);
    }

    /**
     * Set the PID gains for encoder-based closed-control.
     * @param kP Proportional gain; sets power based on current error.
     * @param kI Integral gain; sets power based on the sum of error over time.
     * @param kD Derivative gain; sets power based on current rate of change in error.
     */
    public void setPID(double kP, double kI, double kD) {
        this.config_kI(0, kI);
        setPD(kP, kD);
    }

    /**
     * Set the PD gains for encoder-based closed-control.
     * @param kP Proportional gain; sets power based on current error.
     * @param kD Derivative gain; sets power based on current rate of change in error.
     */
    public void setPD(double kP, double kD) {
        this.config_kD(0, kD);
        setP(kP);
    }

    /**
     * Set the P gain for encoder-based closed-control.
     * @param kP Proportional gain; sets power based on current error.
     */
    public void setP(double kP) {
        this.config_kP(0, kP);
    }

    /**
     * Set the constraints for profiled motion (set position with limits).
     * @param maxVelocity Maximum velocity in RPM.
     * @param maxAcceleration Maximum acceleration in delta RPM.
     */
    public void setConstraints(double maxVelocity, double maxAcceleration) {
        this.configMotionCruiseVelocity((int) Math.round(RPMToCounts(maxVelocity)));
        this.configMotionAcceleration((int) Math.round(RPMToCounts(maxAcceleration)));
    }

    /**
     * Set the power sent to the falcon
     * @param power Number in range [-1.0, 1.0] representing falcon power.
     */
    public void set(double power) {
        this.set(ControlMode.PercentOutput, power);
    }

    /**
     * Set the velocity controlled by a closed-control loop.
     * @param velocity Number representing desired velocity in RPM.
     */
    public void setVelocity(double velocity) {
        this.set(ControlMode.Velocity, RPMToCounts(velocity));
    }

    /**
     * Set the position controlled by a closed-control loop.
     * @param position Number representing desired position in degrees.
     */
    public void setPosition(double position) {
        this.set(ControlMode.Position, degreesToCounts(position));
    }

    /**
     * Sets the position of the motor confined to the set constraints.
     * @param position Position to run to in degrees.
     */
    public void setProfiledPosition(double position) {
        this.set(ControlMode.MotionMagic, degreesToCounts(position));
    }

    /**
     * Returns the position of the motor.
     * @return Position in degrees.
     */
    public double getPosition() {
        return countsToDegrees(this.getSelectedSensorPosition());
    }

    /**
     * Returns the velocity of the motor.
     * @return Velocity in RPM.
     */
    public double getVelocity() {
        return countsToRPM(this.getSelectedSensorVelocity());
    }

    private double RPMToCounts(double rpm) {
        return rpm * (Constants.robot.FALCON_CPR / 600.0);
    }

    private double countsToRPM(double counts) {
        return (counts * 600.0) / Constants.robot.FALCON_CPR;
    }

    private double degreesToCounts(double degrees) {
        return (degrees / 360) * Constants.robot.FALCON_CPR;
    }

    private double countsToDegrees(double counts) {
        return (counts / Constants.robot.FALCON_CPR) * 360;
    }
}
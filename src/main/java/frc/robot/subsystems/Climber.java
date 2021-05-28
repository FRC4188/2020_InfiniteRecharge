package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating climber function.
 */
public class Climber extends SubsystemBase {

    // motor init
    private WPI_TalonFX climberLeftMotor = new WPI_TalonFX(32);
    private WPI_TalonFX climberRightMotor = new WPI_TalonFX(31);

    // pneumatics
    private Solenoid climberSolenoid = new Solenoid(2);// needs to change
    boolean isBrakeEngaged;

    // constants
    private static final double GEAR_RATIO = (58.0 / 11.0) * (20.0 / 60.0); // needs to be verified.
    private static final double ENCODER_TICS_PER_REV = 2048.0;
    private static final double ENCODER_TO_REV = 1.0 / (GEAR_RATIO * ENCODER_TICS_PER_REV);
    private static final double MAX_VELOCITY = 20000.0;
    private static final double kP = 0.4; // porportion (will be * error (= at - want))
    private static final double kD = 0.0; // change in error over time will be negative * kD
    private static final double kI = 0.0; // integral sums up error (see )
    private static final double kF = 1023 / MAX_VELOCITY;
    private static final double RAMP_RATE = .2; // seconds
    private static final int TIMEOUT = 10; // ms
    private static final int CRUISE_ACCEL = 15000;
    private static final int CRUISE_VEL = 15000;
    private static final double MAX_POSITION = 0;
    private static final double MIN_POSITION = -200000;

    /**
     * Constructs a new Climber object and configures devices.
     */
    public Climber() {

        // setup encoders and inversions
        climberLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
        climberRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
        climberLeftMotor.setInverted(true);
        climberLeftMotor.setNeutralMode(NeutralMode.Brake);
        climberRightMotor.setNeutralMode(NeutralMode.Brake);

        // init
        controllerInit();
        setRampRate();

        // reset devices
        resetEncoders();

        engagePneuBrake(true);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        //updateShuffleboard();
    }

    /**
     * Writes values to the Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Left climber height", getLeftPosition());
        SmartDashboard.putNumber("Right climber height", getRightPosition());
        SmartDashboard.putNumber("Left climber velocity", getLeftVelocity());
        SmartDashboard.putNumber("Right climber velocity", getRightVelocity());
        SmartDashboard.putBoolean("Climber brake", !climberSolenoid.get());
        SmartDashboard.putNumber("diff", Math.abs(getRightPosition() - getMinPosition()));
    }

    /**
     * Config Pid loop stuff. Have Locke explain.
     */
    public void controllerInit() {
        climberLeftMotor.config_kI(0, kI, TIMEOUT);
        climberLeftMotor.config_kD(0, kD, TIMEOUT);
        climberLeftMotor.config_kP(0, kP, TIMEOUT);
        climberLeftMotor.config_kF(0, kF, TIMEOUT);
        climberRightMotor.config_kI(0, kI, TIMEOUT);
        climberRightMotor.config_kD(0, kD, TIMEOUT);
        climberRightMotor.config_kP(0, kP, TIMEOUT);
        climberRightMotor.config_kF(0, kF, TIMEOUT);
    }

    /**
     * Sets both climber motors to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        climberLeftMotor.set(percent);
        climberRightMotor.set(percent);
    }

    /**
     * Sets left climber motor to a given percentage [-1.0, 1.0].
     */
    public void setLeft(double percent) {
        climberLeftMotor.set(percent);
    }

    /**
     * Sets left climber motor to a given percentage [-1.0, 1.0].
     */
    public void setRight(double percent) {
        climberRightMotor.set(percent);
    }

    /**
     * Sets both motors to run at a given percentage [-1.0, 1.0] of max velocity.
     */
    public void setVelocity(double percent) {
        double velocity = (percent * MAX_VELOCITY) / (ENCODER_TO_REV * 600);
        climberLeftMotor.set(ControlMode.Velocity, velocity);
        climberRightMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Sets left motor to run at a given percentage [-1.0, 1.0] of max velocity.
     */
    public void setLeftVelocity(double percent) {
        double velocity = (percent * MAX_VELOCITY) / (ENCODER_TO_REV * 600);
        climberLeftMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Sets right motor to run at a given percentage [-1.0, 1.0] of max velocity.
     */
    public void setRightVelocity(double percent) {
        double velocity = (percent * MAX_VELOCITY) / (ENCODER_TO_REV * 600);
        climberRightMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Fires the break pistons to stop the climber.
     */
    public void engagePneuBrake(boolean output) {
        climberSolenoid.set(output);
    }

    /**
     * Sets Climber motors to brake mode.
     */
    public void setBrake() {
        climberLeftMotor.setNeutralMode(NeutralMode.Brake);
        climberRightMotor.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Sets climber ramp rates.
     */
    public void setRampRate() {
        climberLeftMotor.configOpenloopRamp(RAMP_RATE);
        climberLeftMotor.configClosedloopRamp(0);
        climberRightMotor.configOpenloopRamp(RAMP_RATE);
        climberRightMotor.configClosedloopRamp(0);
    }

    /**
     * Resets encoder values to 0 for both sides of Climber.
     */
    public void resetEncoders() {
        climberLeftMotor.setSelectedSensorPosition(0);
        climberRightMotor.setSelectedSensorPosition(0);
    }

    /**
     * Returns left encoder position in feet.
     */
    public double getLeftPosition() {
        return climberLeftMotor.getSelectedSensorPosition();
    }

    /**
     * Returns right encoder position in feet.
     */
    public double getRightPosition() {
        return climberRightMotor.getSelectedSensorPosition(); // * ENCODER_TO_REV;
    }

    /**
     * Returns the left climber velocity in rpm.
     */
    public double getLeftVelocity() {
        return climberLeftMotor.getSelectedSensorVelocity()
                * ENCODER_TO_REV * 600;// sensor units for rpm
    }

    /**
     * Returns the right climber velocity in rpm.
     */
    public double getRightVelocity() {
        return climberRightMotor.getSelectedSensorVelocity()
                * ENCODER_TO_REV * 600;// sensor units for rpm
    }

    /**
     * Returns the maximum position of the climber in raw encoder ticks.
     */
    public double getMaxPosition() {
        return MAX_POSITION;
    }

    /**
     * Returns the minimum position of the climber in raw encoder ticks.
     */
    public double getMinPosition() {
        return MIN_POSITION;
    }

    /**
     * Returns left climber motor temperature in Celcius.
     */
    public double getLeftTemp() {
        return climberLeftMotor.getTemperature();
    }

    /**
     * Returns right climber motor temperature in Celcius.
     */
    public double getRightTemp() {
        return climberRightMotor.getTemperature();
    }

    public boolean getClimberBrake() {
        return !climberSolenoid.get();
    }

}
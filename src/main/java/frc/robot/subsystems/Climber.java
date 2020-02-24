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
    private static final double MIN_POSITION = -197500;

    /**
     * Constructs a new Climber object and configures devices.
     */
    public Climber() {

        // setup encoders
        climberLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
        climberRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
        controllerInit();
        setBrake();

        climberLeftMotor.setInverted(true);

        // reset devices
        resetEncoders();

    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
    }

    /**
     * Writes values to the Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Left climber height", getLeftPosition());
        SmartDashboard.putNumber("Right climber height", getRightPosition());
        SmartDashboard.putNumber("Left climber velocity", getLeftVelocity());
        SmartDashboard.putNumber("Right climber velocity", getRightVelocity());
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
     * Sets both of the motors to run at a percentage of the max speed (-1.0, 1.0).
     */
    public void setSpeedPercentage(double percentage) {
        climberLeftMotor.set(percentage);
        climberRightMotor.set(percentage);
    }

    /**
     * Sets the left motor to run at a percentage of the max speed (-1.0, 1.0).
     */
    public void setLeftPercentage(double percentage) {
        climberLeftMotor.set(percentage);
    }

    /**
     * Sets the right motor to run at a percentage of the max speed (-1.0, 1.0).
     */
    public void setRightPercentage(double percentage) {
        climberRightMotor.set(percentage);
    }

    /**
     * set the motors to run at the given velocity in rpm.
     */
    public void setVelocity(double velocity) {
        velocity = velocity / (ENCODER_TO_REV * 600);
        climberLeftMotor.set(ControlMode.Velocity, velocity);
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

    public double getMaxPosition() {
        return MAX_POSITION;
    }

    public double getMinPosition() {
        return MIN_POSITION;
    }

    /** Returns temperature of motor based off Falcon ID. */
    public double getMotorTemperature(int index) {
        WPI_TalonFX[] falcons = new WPI_TalonFX[] { climberRightMotor, climberLeftMotor, };
        index -= 1;
        double temp = -1.0;
            try {
        temp = falcons[index - 30].getTemperature();
            } catch (ArrayIndexOutOfBoundsException e) {
          System.err.println("Error: index " + index + " not in array of climb falcons.");
            }
        return temp;
    }

}
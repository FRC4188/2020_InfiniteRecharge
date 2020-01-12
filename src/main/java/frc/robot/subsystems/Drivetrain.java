package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    // device initialization
    private WPI_TalonSRX leftMotor = new WPI_TalonSRX(5);
    private WPI_TalonSRX leftSlave = new WPI_TalonSRX(6);
    private WPI_TalonSRX rightMotor = new WPI_TalonSRX(8);
    private WPI_TalonSRX rightSlave = new WPI_TalonSRX(7);
    private DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
    private ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    // constants
    private static final double kP = 0.025;
    private static final double kI = 0;
    private static final double kD = 0;
    private static final int    SLOT_ID = 0;
    private static final double MAX_OUT = 1.0;
    private static final int    TIMEOUT = 10; // ms
    private static final double ENCODER_TO_FEET = 18.46 / 265.75;

    // state vars
    private boolean leftInverted;
    private boolean rightInverted;

    /**
     * Constructs new Drivetrain object and configures devices.
     */
    public Drivetrain() {

        // slave control
        leftSlave.follow(leftMotor);
        rightSlave.follow(rightMotor);

        // setup encoders
        leftMotor.configSelectedFeedbackSensor(
                FeedbackDevice.CTRE_MagEncoder_Relative, SLOT_ID, TIMEOUT);
        rightMotor.configSelectedFeedbackSensor(
                FeedbackDevice.CTRE_MagEncoder_Relative, SLOT_ID, TIMEOUT);

        // reset devices
        resetEncoders();
        resetGyro();

        // configuration
        setBrake();
        leftInverted = false;
        rightInverted = false;
        setInverted(true);
        controllerInit();

    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Left Position", getLeftPosition());
        SmartDashboard.putNumber("Right Position", getRightPosition());
    }

    /**
     * Configures gains for Spark closed loop controllers.
     */
    private void controllerInit() {
        leftMotor.configNominalOutputForward(0, TIMEOUT);
        leftMotor.configNominalOutputReverse(0, TIMEOUT);
        leftMotor.configPeakOutputForward(MAX_OUT, TIMEOUT);
        leftMotor.configPeakOutputReverse(-MAX_OUT, TIMEOUT);
        leftMotor.config_kP(SLOT_ID, kP, TIMEOUT);
        leftMotor.config_kI(SLOT_ID, kI, TIMEOUT);
        leftMotor.config_kD(SLOT_ID, kD, TIMEOUT);
        rightMotor.configNominalOutputForward(0, TIMEOUT);
        rightMotor.configNominalOutputReverse(0, TIMEOUT);
        rightMotor.configPeakOutputForward(MAX_OUT, TIMEOUT);
        rightMotor.configPeakOutputReverse(-MAX_OUT, TIMEOUT);
        rightMotor.config_kP(SLOT_ID, kP, TIMEOUT);
        rightMotor.config_kI(SLOT_ID, kI, TIMEOUT);
        rightMotor.config_kD(SLOT_ID, kD, TIMEOUT);
    }

    /**
     * Controls the drivetrain using an arcade model.
     */
    public void arcade(double xSpeed, double zRotation) {
        drive.arcadeDrive(xSpeed, zRotation, false);
    }

    /**
     * Controls the drivetrain using a tank model.
     */
    public void tank(double leftSpeed, double rightSpeed) {
        drive.tankDrive(leftSpeed, rightSpeed, false);
    }

    /**
     * Sets left side of drivetrain to a given speed [-1.0, 1.0].
     */
    public void setLeft(double speed) {
        leftMotor.set(speed);
    }

    /**
     * Sets right side of drivetrain to a given speed [-1.0, 1.0].
     */
    public void setRight(double speed) {
        rightMotor.set(speed);
    }

    /**
     * Sets left side of drivetrain to a given velocity in talon units.
     */
    public void setLeftVelocity(double velocity) {
        leftMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Sets right side of drivetrain to a given velocity in talon units.
     */
    public void setRightVelocity(double velocity) {
        rightMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Sets left side of drivetrain to a given voltage.
     */
    public void setLeftVoltage(double v) {
        leftMotor.setVoltage(v);
    }

    /**
     * Sets right side of drivetrain to a given voltage.
     */
    public void setRightVoltage(double v) {
        rightMotor.setVoltage(v);
    }

    /**
     * Inverts drivetrain. True inverts each side from the
     * current state set in the Drivetrain constructor.
     */
    public void setInverted(boolean isInverted) {
        setLeftInverted(isInverted);
        setRightInverted(isInverted);
    }

    /**
     * Inverts the left side of the drivetrain. True inverts it
     * from the current state set in the Drivetrain constructor.
     */
    public void setLeftInverted(boolean isInverted) {
        if (leftInverted) isInverted = !isInverted;
        leftMotor.setInverted(isInverted);
        leftSlave.setInverted(isInverted);
    }

    /**
     * Inverts the right side of the drivetrain. True inverts it
     * from the current state set in the Drivetrain constructor.
     */
    public void setRightInverted(boolean isInverted) {
        if (rightInverted) isInverted = !isInverted;
        rightMotor.setInverted(isInverted);
        rightSlave.setInverted(isInverted);
    }

    /**
     * Sets drivetrain to brake mode.
     */
    public void setBrake() {
        leftMotor.setNeutralMode(NeutralMode.Brake);
        leftSlave.setNeutralMode(NeutralMode.Brake);
        rightMotor.setNeutralMode(NeutralMode.Brake);
        rightSlave.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Sets drivetrain to coast mode.
     */
    public void setCoast() {
        leftMotor.setNeutralMode(NeutralMode.Coast);
        leftSlave.setNeutralMode(NeutralMode.Coast);
        rightMotor.setNeutralMode(NeutralMode.Coast);
        rightSlave.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Resets encoder values to 0 for both sides of drivetrain.
     */
    public void resetEncoders() {
        leftMotor.setSelectedSensorPosition(0);
        rightMotor.setSelectedSensorPosition(0);
    }

    /**
     * Resets gyro angle to zero.
     */
    public void resetGyro() {
        gyro.reset();
    }

    /**
     * Returns left encoder position in feet.
     */
    public double getLeftPosition() {
        return leftMotor.getSelectedSensorPosition() * ENCODER_TO_FEET;
    }

    /**
     * Returns right encoder position in feet.
     */
    public double getRightPosition() {
        return rightMotor.getSelectedSensorPosition() * ENCODER_TO_FEET;
    }

    /**
     * Returns left encoder position in native talon units.
     */
    public double getRawLeftPosition() {
        return leftMotor.getSelectedSensorPosition();
    }

    /**
     * Returns left encoder position in native talon units.
     */
    public double getRawRightPosition() {
        return rightMotor.getSelectedSensorPosition();
    }

    /**
     * Returns left encoder velocity in feet per second.
     */
    public double getLeftVelocity() {
        return leftMotor.getSelectedSensorVelocity() * ENCODER_TO_FEET * 10; // native talon/100ms
    }

    /**
     * Returns right encoder velocity in feet per second.
     */
    public double getRightVelocity() {
        return rightMotor.getSelectedSensorVelocity() * ENCODER_TO_FEET * 10; // native talon/100ms
    }

    /**
     * Returns left encoder velocity in talon units / 100ms.
     */
    public double getLeftRawVelocity() {
        return leftMotor.getSelectedSensorVelocity();
    }

    /**
     * Returns right encoder velocity in talon units / 100ms.
     */
    public double getRightRawVelocity() {
        return rightMotor.getSelectedSensorVelocity();
    }

    /**
     * Returns gyro angle in degrees.
     */
    public double getGyroAngle() {
        return gyro.getAngle();
    }

    /**
     * Returns gyro rate in degrees per sec.
     */
    public double getGyroRate() {
        return gyro.getRate();
    }

}
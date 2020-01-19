package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating drivetrain function.
 */
public class Drivetrain extends SubsystemBase {

    // device initialization
    private WPI_TalonSRX leftMotor = new WPI_TalonSRX(5);
    private WPI_TalonSRX leftSlave = new WPI_TalonSRX(6);
    private WPI_TalonSRX rightMotor = new WPI_TalonSRX(8);
    private WPI_TalonSRX rightSlave = new WPI_TalonSRX(7);
    private DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
    private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    private DifferentialDriveOdometry odometry;

    // constants
    private static final double ENCODER_TO_METERS = 18.46 / 265.75;

    // state vars
    private boolean leftInverted = true;
    private boolean rightInverted = false;
    private boolean gyroInverted = false;

    /**
     * Constructs new Drivetrain object and configures devices.
     */
    public Drivetrain() {

        // slave control
        leftSlave.follow(leftMotor);
        rightSlave.follow(rightMotor);

        // setup encoders
        leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        // reset devices
        resetEncoders();
        resetGyro();

        // configuration
        setBrake();
        setInverted(false);

        // initialize odometry
        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getGyroAngle()));

    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
        updateOdometry();
    }

    /**
     * Writes values to Shuffleboard.
     */
    private void updateShuffleboard() {
        SmartDashboard.putNumber("Left Position", getLeftPosition());
        SmartDashboard.putNumber("Right Position", getRightPosition());
    }

    /**
     * Tracks robot pose.
     */
    private void updateOdometry() {
        odometry.update(Rotation2d.fromDegrees(getGyroAngle()), getLeftPosition(),
                getRightPosition());
    }

    /**
     * Controls the drivetrain using an arcade model.
     */
    public void arcade(double xSpeed, double zRotation) {
        drive.arcadeDrive(xSpeed, zRotation, false);
    }

    /**
     * Controls the left and right sides of the drivetrain directly with voltages.
     */
    public void tankVolts(double leftVolts, double rightVolts) {
        leftMotor.set(leftVolts);
        rightMotor.set(rightVolts);
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
     * Returns left encoder position in meters.
     */
    public double getLeftPosition() {
        return leftMotor.getSelectedSensorPosition() * ENCODER_TO_METERS;
    }

    /**
     * Returns right encoder position in meters.
     */
    public double getRightPosition() {
        return rightMotor.getSelectedSensorPosition() * ENCODER_TO_METERS;
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
     * Returns left encoder velocity in meters per second.
     */
    public double getLeftVelocity() {
        return leftMotor.getSelectedSensorVelocity() * ENCODER_TO_METERS * 10; // native per 100ms
    }

    /**
     * Returns right encoder velocity in meters per second.
     */
    public double getRightVelocity() {
        return rightMotor.getSelectedSensorVelocity() * ENCODER_TO_METERS * 10; // native per 100ms
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
     * Returns the current wheel speeds of the robot.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    /**
     * Returns the currently estimated pose of the robot.
     */
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    /**
     * Returns gyro angle in degrees.
     */
    public double getGyroAngle() {
        return Math.IEEEremainder(gyro.getAngle(), 360) * (gyroInverted ? -1.0 : 1.0);
    }

    /**
     * Returns gyro rate in degrees per sec.
     */
    public double getGyroRate() {
        return gyro.getRate();
    }

}
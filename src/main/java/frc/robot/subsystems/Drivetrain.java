package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    // device initialization
    private CANSparkMax leftMotor = new CANSparkMax(1, MotorType.kBrushless);
    private CANSparkMax leftSlave1 = new CANSparkMax(2, MotorType.kBrushless);
    private CANSparkMax leftSlave2 = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax rightMotor = new CANSparkMax(7, MotorType.kBrushless);
    private CANSparkMax rightSlave1 = new CANSparkMax(5, MotorType.kBrushless);
    private CANSparkMax rightSlave2 = new CANSparkMax(6, MotorType.kBrushless);
    private DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
    private CANEncoder leftEncoder = leftMotor.getEncoder();
    private CANEncoder rightEncoder = rightMotor.getEncoder();
    private AHRS ahrs = new AHRS();
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
        leftSlave1.follow(leftMotor);
        leftSlave2.follow(leftMotor);
        rightSlave1.follow(rightMotor);
        rightSlave2.follow(rightMotor);

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
        leftSlave1.setInverted(isInverted);
        leftSlave2.setInverted(isInverted);
    }

    /**
     * Inverts the right side of the drivetrain. True inverts it
     * from the current state set in the Drivetrain constructor.
     */
    public void setRightInverted(boolean isInverted) {
        if (rightInverted) isInverted = !isInverted;
        rightMotor.setInverted(isInverted);
        rightSlave1.setInverted(isInverted);
        rightSlave2.setInverted(isInverted);
    }

    /**
     * Sets drivetrain to brake mode.
     */
    public void setBrake() {
        leftMotor.setIdleMode(IdleMode.kBrake);
        leftSlave1.setIdleMode(IdleMode.kBrake);
        leftSlave2.setIdleMode(IdleMode.kBrake);
        rightMotor.setIdleMode(IdleMode.kBrake);
        rightSlave1.setIdleMode(IdleMode.kBrake);
        rightSlave2.setIdleMode(IdleMode.kBrake);
    }

    /**
     * Sets drivetrain to coast mode.
     */
    public void setCoast() {
        leftMotor.setIdleMode(IdleMode.kCoast);
        leftSlave1.setIdleMode(IdleMode.kCoast);
        leftSlave2.setIdleMode(IdleMode.kCoast);
        rightMotor.setIdleMode(IdleMode.kCoast);
        rightSlave1.setIdleMode(IdleMode.kCoast);
        rightSlave2.setIdleMode(IdleMode.kCoast);
    }

    /**
     * Resets encoder values to 0 for both sides of drivetrain.
     */
    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    /**
     * Resets gyro angle to zero.
     */
    public void resetGyro() {
        ahrs.reset();
    }

    /**
     * Returns left encoder position in meters.
     */
    public double getLeftPosition() {
        return leftEncoder.getPosition() * ENCODER_TO_METERS;
    }

    /**
     * Returns right encoder position in meters.
     */
    public double getRightPosition() {
        return rightEncoder.getPosition() * ENCODER_TO_METERS;
    }

    /**
     * Returns left encoder position in native talon units.
     */
    public double getLeftRawPosition() {
        return leftEncoder.getPosition();
    }

    /**
     * Returns left encoder position in native talon units.
     */
    public double getRawRightRawPosition() {
        return rightEncoder.getPosition();
    }

    /**
     * Returns left encoder velocity in meters per second.
     */
    public double getLeftVelocity() {
        return leftEncoder.getVelocity() * ENCODER_TO_METERS * (1.0 / 60); // native is rpm
    }

    /**
     * Returns right encoder velocity in meters per second.
     */
    public double getRightVelocity() {
        return rightEncoder.getVelocity() * ENCODER_TO_METERS * (1.0 / 60); // native is rpm
    }

    /**
     * Returns left encoder velocity in rpm.
     */
    public double getLeftRawVelocity() {
        return leftEncoder.getVelocity();
    }

    /**
     * Returns right encoder velocity in rpm.
     */
    public double getRightRawVelocity() {
        return rightEncoder.getVelocity();
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
        return Math.IEEEremainder(ahrs.getYaw(), 360) * (gyroInverted ? -1.0 : 1.0);
    }

    /**
     * Returns gyro rate in degrees per sec.
     */
    public double getGyroRate() {
        return ahrs.getRate();
    }

}
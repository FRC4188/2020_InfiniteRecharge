package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating drivetrain function.
 */
public class Drivetrain extends SubsystemBase {

    // device initialization
    private final WPI_TalonFX leftMotor = new WPI_TalonFX(1);
    private final WPI_TalonFX leftSlave = new WPI_TalonFX(2);
    private final WPI_TalonFX rightMotor = new WPI_TalonFX(3);
    private final WPI_TalonFX rightSlave = new WPI_TalonFX(4);
    private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    private final Solenoid cooler = new Solenoid(3);

    // constants
    private static final double kS = 1.34; // volts
    private static final double kV = 2.12; // volt seconds / meter
    private static final double kA = 0.363; // volt seconds squared / meter
    private static final double kP = 3.5;
    private static final double AUTO_MAX_VEL = 1.75; // meters / second
    private static final double AUTO_MAX_ACCEL = 3; // meters / second squared
    private static final double AUTO_MAX_VOLTAGE = 10; // volts
    private static final double ARCADE_MAX_VEL = 3; // meters / second
    private static final double ARCADE_MAX_ROT = 2 * Math.PI; // rads / second
    private static final double TRACKWIDTH = 0.58; // meters
    private static final double WHEEL_DIAMETER = 0.1524; // meters
    private static final double GEAR_RATIO = (58.0 / 11.0) * (32.0 / 18.0);
    private static final double TICKS_PER_REV = 2048; // encoder units
    private static final double ENCODER_TO_METERS =
            (Math.PI * WHEEL_DIAMETER) / (GEAR_RATIO * TICKS_PER_REV);
    private double throtle = 1.0;

    // controls
    private final DifferentialDriveOdometry odometry;
    private final PIDController leftPid = new PIDController(kP, 0, 0);
    private final PIDController rightPid = new PIDController(kP, 0, 0);
    private final DifferentialDriveKinematics kinematics =
            new DifferentialDriveKinematics(TRACKWIDTH);
    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(kS, kV, kA);
    private final DifferentialDriveVoltageConstraint voltageConstraint =
            new DifferentialDriveVoltageConstraint(feedforward, kinematics, AUTO_MAX_VOLTAGE);
    private final TrajectoryConfig trajectoryConfig =
            new TrajectoryConfig(AUTO_MAX_VEL, AUTO_MAX_ACCEL)
            .setKinematics(kinematics)
            .addConstraint(voltageConstraint);


    // state vars
    private boolean leftInverted = true;
    private boolean rightInverted = false;
    private boolean gyroInverted = true; // ccw positive

    /**
     * Constructs new Drivetrain object and configures devices.
     */
    public Drivetrain() {

        // slave control
        leftSlave.follow(leftMotor);
        rightSlave.follow(rightMotor);

        // setup encoders
        leftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        rightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        // reset devices
        resetEncoders();
        resetGyro();
        gyro.calibrate();

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
        SmartDashboard.putNumber("Left Velocity", getLeftVelocity());
        SmartDashboard.putNumber("Right Position", getRightPosition());
        SmartDashboard.putNumber("Right Velocity", getRightVelocity());
        SmartDashboard.putNumber("Gyro Angle", getGyroAngle());
        SmartDashboard.putString("Odometry", getPose().toString());
    }

    /**
     * Tracks robot pose.
     */
    private void updateOdometry() {
        odometry.update(Rotation2d.fromDegrees(getGyroAngle()), getLeftPosition(),
                getRightPosition());
    }

    /**
     * Controls the left and right sides of the drivetrain directly with voltages.
     */
    public void tankVolts(double leftVolts, double rightVolts) {
        leftMotor.setVoltage(leftVolts);
        rightMotor.setVoltage(rightVolts);
    }

    /**
     * Sets desired wheel speeds using closed loop control.
     */
    public void setSpeeds(DifferentialDriveWheelSpeeds speeds) {
        double leftVel = speeds.leftMetersPerSecond;
        double rightVel = speeds.rightMetersPerSecond;
        SmartDashboard.putNumber("Left Vel Setpoint", leftVel);
        SmartDashboard.putNumber("Right Vel Setpoint", rightVel);
        double leftFeedforward = feedforward.calculate(leftVel);
        double rightFeedforward = feedforward.calculate(rightVel);
        double leftOutput = leftPid.calculate(getLeftVelocity(), leftVel)
                + leftFeedforward;
        double rightOutput = rightPid.calculate(getRightVelocity(), rightVel)
                + rightFeedforward;
        tankVolts(leftOutput, rightOutput);
    }

    /**
     * Controls the drivetrain using a closed loop arcade model, with inputs [-1, 1].
     * @param xSpeed - Forward percent output.
     * @param zRotation - Rotational percent output - ccw positive.
     */
    public void arcade(double xSpeed, double zRotation) {
        double xSpeedMetersPerSec = xSpeed * ARCADE_MAX_VEL * throtle;
        double zRotRadPerSec = zRotation * ARCADE_MAX_ROT;
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(xSpeedMetersPerSec, 0.0, zRotRadPerSec);
        DifferentialDriveWheelSpeeds speeds = kinematics.toWheelSpeeds(chassisSpeeds);
        setSpeeds(speeds);
    }

    /**
     * Controls the drivetrain using an open loop arcade model, with inputs [-1, 1].
     * @param xSpeed - Forward percent output.
     * @param zRotation - Rotational percent output - cw positive.
     */
    public void arcadeOpenLoop(double xSpeed, double zRotation) {
        double leftOutput = xSpeed + zRotation;
        double rightOutput = xSpeed - zRotation;
        if (Math.abs(leftOutput) > 1.0 || Math.abs(rightOutput) > 1.0) {
            if (Math.abs(rightOutput) > Math.abs(leftOutput)) {
                leftOutput = (leftOutput / rightOutput) * Math.signum(leftOutput);
                rightOutput = 1.0 * Math.signum(rightOutput);
            } else  {
                rightOutput = (rightOutput / leftOutput) * Math.signum(rightOutput);
                leftOutput = 1.0 * Math.signum(leftOutput);
            }
        }
        tankVolts(leftOutput * 12, rightOutput * 12);
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
     * Resets necessary sensors and odometry for drivetrain.
     */
    public void reset(Pose2d initialPose) {
        resetEncoders();
        resetGyro();
        resetOdometry(initialPose);
    }

    /**
     * Resets encoder values to 0 for both sides of drivetrain.
     */
    private void resetEncoders() {
        leftMotor.setSelectedSensorPosition(0);
        rightMotor.setSelectedSensorPosition(0);
    }

    /**
     * Resets gyro angle to zero.
     */
    private void resetGyro() {
        gyro.reset();
    }

    /**
     * Resets pose to zero.
     */
    private void resetOdometry(Pose2d initialPose) {
        odometry.resetPosition(initialPose, Rotation2d.fromDegrees(getGyroAngle()));
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
     * Returns PIDController used to control left side of drivetrain.
     */
    public PIDController getLeftPid() {
        return leftPid;
    }

    /**
     * Returns PIDController used to control left side of drivetrain.
     */
    public PIDController getRightPid() {
        return rightPid;
    }

    /**
     * Returns drive kinematics object.
     */
    public DifferentialDriveKinematics getKinematics() {
        return kinematics;
    }

    /**
     * Returns feedforward object.
     */
    public SimpleMotorFeedforward getFeedforward() {
        return feedforward;
    }

    /**
     * Returns trajectory config that respects drivetrain constraints.
     */
    public TrajectoryConfig getTrajectoryConfig() {
        return trajectoryConfig;
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

    /**
     * Sets the cooler solenoid on or off.
     * @param output - The value to set cooler solenoid to.
     */
    public void setCooler(boolean output) {
        cooler.set(output);
    }

    /** 
     * Returns temperature of motor based off Falcon ID. 
     */
    public double getMotorTemperature(int index) {
        WPI_TalonFX[] falcons = new WPI_TalonFX[]{
            leftMotor,
            leftSlave,
            rightMotor,
            rightSlave,
        };
        double temp = -1.0;
        try {
            temp = falcons[index].getTemperature();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: index " + index + " not in array of drive falcons.");
        }
        return temp;
    }

    public void setThrotle(double idk) {
        throtle = idk;
    }
}

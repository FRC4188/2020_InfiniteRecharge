package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.SlewRateLimiter;
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
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Coworking;

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
    private static final double kS = 0.233; // volts
    private static final double kV = 2.12; // volt seconds / meter
    private static final double kA = 0.167; // volt seconds squared / meter
    private static final double kP = 3.0;//3.5;
    private static final double AUTO_MAX_VEL = 5; // meters / second
    private static final double AUTO_MAX_ACCEL = 1.5; // meters / second squared
    private static final double AUTO_MAX_CENTRIP = 1.5; // meters / second squared
    private static final double AUTO_MAX_VOLTAGE = 12; // volts
    private static final double ARCADE_MAX_VEL = 3; // meters / second
    private static final double ARCADE_MAX_ROT = 2 * Math.PI; // rads / second
    private static final double TRACKWIDTH = 0.565; // meters
    private static final double WHEEL_DIAMETER = /*0.14*/ 0.1524; // meters
    private static final double GEAR_RATIO = (58.0 / 11.0) * (32.0 / 18.0);
    private static final double TICKS_PER_REV = 2048; // encoder units
    private static final double ENCODER_TO_METERS =
            (Math.PI * WHEEL_DIAMETER) / (GEAR_RATIO * TICKS_PER_REV);

    // controls
    private final DifferentialDriveOdometry odometry;
    private final PIDController leftPid = new PIDController(kP, 0, 0);
    private final PIDController rightPid = new PIDController(kP, 0, 0);
    private final DifferentialDriveKinematics kinematics =
            new DifferentialDriveKinematics(TRACKWIDTH);
    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(kS, kV, kA);
    private final DifferentialDriveVoltageConstraint voltageConstraint =
            new DifferentialDriveVoltageConstraint(feedforward, kinematics, AUTO_MAX_VOLTAGE);
    private final CentripetalAccelerationConstraint centripAccelConstraint =
            new CentripetalAccelerationConstraint(AUTO_MAX_CENTRIP);
    private final TrajectoryConfig trajectoryConfig =
            new TrajectoryConfig(AUTO_MAX_VEL, AUTO_MAX_ACCEL)
            .setKinematics(kinematics)
            .addConstraint(voltageConstraint)
            .addConstraint(centripAccelConstraint);

    private SlewRateLimiter speedLimiter = new SlewRateLimiter(1.0);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(1.0);
        
    // state vars
    private boolean leftInverted = true;
    private boolean rightInverted = false;
    private boolean gyroInverted = true; // ccw positive

    private double reduction = 1;

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

        // configuration
        setBrake();
        setInverted(false);

        // initialize odometry
        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getGyroAngle()));

        Notifier shuffle = new Notifier(() -> updateShuffleboard());
        shuffle.startPeriodic(0.1);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        //updateShuffleboard();
        updateOdometry();
    }

    /**
     * Writes values to Shuffleboard.
     */
    private void updateShuffleboard() {
        //SmartDashboard.putNumber("Gyro Angle", getGyroAngle());
        SmartDashboard.putString("Odometry", getPose().toString());
        SmartDashboard.putNumber("Drivetrain Velocity", (getLeftVelocity() + getRightVelocity()) / 2);
        //SmartDashboard.putData("Calibrate Gyro", new InstantCommand(gyro::calibrate));
        //SmartDashboard.putData("Zero Gyro", new InstantCommand(gyro::reset));
        //SmartDashboard.putNumber("Left Velocity ", getLeftVelocity());
        //SmartDashboard.putNumber("Right Velocity", getRightVelocity());
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


    boolean lastSet;
    LinearFilter filter = LinearFilter.movingAverage(50);
    /**
     * Sets desired wheel speeds using closed loop control.
     */
    public void setSpeeds(DifferentialDriveWheelSpeeds speeds) {
        boolean reduce = false;
        boolean isSet = Coworking.getInstance().intakeSet != 0.0;
        if (isSet) {
            double speed = filter.calculate(Coworking.getInstance().intakeSpeed);
            reduce = Math.abs(speed) < 950.0;
        } else if (lastSet) {
            filter = LinearFilter.movingAverage(50);
        }

        lastSet = isSet;

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

        if (!reduce) tankVolts(leftOutput, rightOutput);
        else tankVolts(leftOutput * 0.8, rightOutput * 0.8);
    }

    public void setSpeeds(double left, double right) {
        setSpeeds(new DifferentialDriveWheelSpeeds(left, right));
    }

    /**
     * Controls the drivetrain using a closed loop arcade model, with inputs [-1, 1].
     * @param xSpeed - Forward percent output.
     * @param zRotation - Rotational percent output - ccw positive.
     */
    public void arcade(double xSpeed, double zRotation, boolean fineControl) {
        // modify output based on fine control boolean
        xSpeed = (fineControl) ? xSpeed * 0.75 : xSpeed;
        zRotation = (fineControl) ? zRotation * 0.75 : zRotation;
                
                
        xSpeed = speedLimiter.calculate(xSpeed);
        zRotation = rotLimiter.calculate(zRotation);
        
        double xSpeedMetersPerSec = xSpeed * ARCADE_MAX_VEL * reduction;
        double zRotRadPerSec = zRotation * ARCADE_MAX_ROT * reduction;
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
     * Sets the cooler solenoid on or off.
     */
    public void setCooler(boolean output) {
        cooler.set(output);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
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
     * Returns left master motor temperature in Celcius.
     */
    public double getLeftMasterTemp() {
        return leftMotor.getTemperature();
    }

    /**
     * Returns right master motor temperature in Celcius.
     */
    public double getRightMasterTemp() {
        return rightMotor.getTemperature();
    }

    /**
     * Returns left slave motor temperature in Celcius.
     */
    public double getLeftSlaveTemp() {
        return leftSlave.getTemperature();
    }

    /**
     * Returns right slave motor temperature in Celcius.
     */
    public double getRightSlaveTemp() {
        return rightSlave.getTemperature();
    }

}

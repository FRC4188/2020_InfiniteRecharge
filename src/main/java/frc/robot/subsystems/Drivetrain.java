package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
    private CANPIDController leftPidC = leftMotor.getPIDController();
    private CANPIDController rightPidC = rightMotor.getPIDController();
    private AHRS ahrs = new AHRS();

    // constants
    private static final double MAX_VELOCITY = 2000; // rpm
    private static final double MAX_ACCELERATION = 1500; // rpm
    private static final double kP = 5e-5;
    private static final double kI = 1e-6;
    private static final double kD = 0;
    private static final double kV = 0;
    private static final double kI_ZONE = 0;
    private static final int    SLOT_ID = 0;
    private static final double MAX_OUT = 1.0;
    private static final double NEO_ENCODER_TO_FEET = 18.46 / 265.75;

    // state vars
    private boolean leftInverted;
    private boolean rightInverted;

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
        leftInverted = true;
        leftInverted = false;
        setInverted(false);
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
        leftPidC.setP(kP);
        leftPidC.setI(kI);
        leftPidC.setD(kD);
        leftPidC.setIZone(kI_ZONE);
        leftPidC.setFF(kV);
        leftPidC.setOutputRange(-MAX_OUT, MAX_OUT);
        leftPidC.setSmartMotionMaxVelocity(MAX_VELOCITY, SLOT_ID);
        leftPidC.setSmartMotionMaxAccel(MAX_ACCELERATION, SLOT_ID);
        rightPidC.setP(kP);
        rightPidC.setI(kI);
        rightPidC.setD(kD);
        rightPidC.setIZone(kI_ZONE);
        rightPidC.setFF(kV);
        rightPidC.setOutputRange(-MAX_OUT, MAX_OUT);
        rightPidC.setSmartMotionMaxVelocity(MAX_VELOCITY, SLOT_ID);
        rightPidC.setSmartMotionMaxAccel(MAX_ACCELERATION, SLOT_ID);
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
     * Sets left side of drivetrain to a given velocity in rpm.
     */
    public void setLeftVelocity(double velocity) {
        leftPidC.setReference(velocity, ControlType.kVelocity);
    }

    /**
     * Sets right side of drivetrain to a given velocity in rpm.
     */
    public void setRightVelocity(double velocity) {
        rightPidC.setReference(velocity, ControlType.kVelocity);
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
     * Returns left encoder position in feet.
     */
    public double getLeftPosition() {
        return leftEncoder.getPosition() * NEO_ENCODER_TO_FEET;
    }

    /**
     * Returns right encoder position in feet.
     */
    public double getRightPosition() {
        return rightEncoder.getPosition() * NEO_ENCODER_TO_FEET;
    }

    /**
     * Returns left encoder position in native talon units.
     */
    public double getRawLeftPosition() {
        return leftEncoder.getPosition();
    }

    /**
     * Returns left encoder position in native talon units.
     */
    public double getRawRightPosition() {
        return rightEncoder.getPosition();
    }

    /**
     * Returns left encoder velocity in feet per second.
     */
    public double getLeftVelocity() {
        return leftEncoder.getVelocity() * NEO_ENCODER_TO_FEET * (1.0 / 60); // native is rpm
    }

    /**
     * Returns right encoder velocity in feet per second.
     */
    public double getRightVelocity() {
        return rightEncoder.getVelocity() * NEO_ENCODER_TO_FEET * (1.0 / 60); // native is rpm
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
     * Returns gyro angle in degrees.
     */
    public double getGyroAngle() {
        return ahrs.getYaw();
    }

    /**
     * Returns gyro rate in degrees per sec.
     */
    public double getGyroRate() {
        return ahrs.getRate();
    }

}
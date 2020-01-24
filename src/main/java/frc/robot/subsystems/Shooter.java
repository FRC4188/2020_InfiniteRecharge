package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating shooter function.
 */
public class Shooter extends SubsystemBase {

    // device initialization
    private WPI_TalonFX shooterMotor = new WPI_TalonFX(11);
    private WPI_TalonFX shooterSlave = new WPI_TalonFX(12);

    // constants
    private static final double MAX_VELOCITY = 20000.0; // talon units per 100ms
    private static final double kP = 0.2;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final double kF = 1023 / MAX_VELOCITY;
    private static final int    SLOT_ID = 0;
    private static final int    TIMEOUT = 10; // ms
    private static final double ENCODER_TICKS_PER_REV = 2048;

    /**
     * Constructs new Shooter object and configures devices.
     */
    public Shooter() {

        // slave control
        shooterSlave.follow(shooterMotor);
        shooterSlave.setInverted(true);

        // setup encoders
        shooterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);

        // configuration
        setCoast();
        controllerInit();

    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
    }

    /**
     * Writes values to Shuffleboard.
     */
    private void updateShuffleboard() {
    }

    /** 
     * Configures gains for SRX closed loop controller.
     */
    private void controllerInit() {
        shooterMotor.config_kP(SLOT_ID, kP, TIMEOUT);
        shooterMotor.config_kI(SLOT_ID, kI, TIMEOUT);
        shooterMotor.config_kD(SLOT_ID, kD, TIMEOUT);
        shooterMotor.config_kF(SLOT_ID, kF, TIMEOUT);
    }

    /**
     * Sets shooter motors to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        shooterMotor.set(percent);
    }

    /**
     * Sets shooter motors to a given velocity in rpm.
     */
    public void setVelocity(double velocity) {
        velocity = (velocity * ENCODER_TICKS_PER_REV) / 600; // native is talon units per 100ms
        shooterMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Sets shooter motors to brake mode.
     */
    public void setBrake() {
        shooterMotor.setNeutralMode(NeutralMode.Brake);
        shooterSlave.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Sets shooter motors to coast mode.
     */
    public void setCoast() {
        shooterMotor.setNeutralMode(NeutralMode.Coast);
        shooterSlave.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Gets shooter motor velocity in rpm.
     */
    public double getVelocity() {
        return shooterMotor.getSelectedSensorVelocity() * 600 * ENCODER_TICKS_PER_REV;
    }

}
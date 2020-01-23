package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating shooter function.
 */
public class Shooter extends SubsystemBase {

    // device initialization
    private WPI_TalonFX shooterMotor = new WPI_TalonFX(11);
    private WPI_TalonFX shooterSlave = new WPI_TalonFX(12);

    // constants
    private static final double ENCODER_TICKS_PER_REV = 2048;

    /**
     * Constructs new Shooter object and configures devices.
     */
    public Shooter() {

        // slave control
        shooterSlave.follow(shooterMotor);

        // setup encoders
        shooterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);

        // configuration
        setCoast();

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
     * Sets shooter motors to a given speed [-1.0, 1.0].
     */
    public void set(double speed) {
        shooterMotor.set(speed);
    }

    /**
     * Sets shooter motors to a given voltage.
     */
    public void setVoltage(double v) {
        shooterMotor.setVoltage(v);
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
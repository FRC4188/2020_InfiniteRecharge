package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.BrownoutProtection;

/**
 * Class encapsulating magazine function.
 */
public class Magazine extends SubsystemBase {

    // device initialization
    private final CANSparkMax magMotor = new CANSparkMax(24, MotorType.kBrushless);
    private final CANEncoder magEncoder = new CANEncoder(magMotor);

    // Brownout Protection initialization
    private BrownoutProtection bop = new BrownoutProtection();

    // constants
    private static final double RAMP_RATE = 0.2; // seconds

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        setRampRate();
        bop.run();
    }

    public Magazine(BrownoutProtection bop) {
        this();
        this.bop = bop;
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
        bop.run();
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("Magazine Encoder Position", magEncoder.getPosition());
        SmartDashboard.putNumber("M24 temp", magMotor.getMotorTemperature());
    }

    /**
     * Sets belt motor to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        magMotor.set(percent * bop.getMagazinePower());
    }

    /**
     * Configures magazine motor ramp rates.
     */
    public void setRampRate() {
        magMotor.setClosedLoopRampRate(RAMP_RATE);
        magMotor.setOpenLoopRampRate(RAMP_RATE);
    }

    /**
     * Returns magazine motor temperature in Celcius.
     */
    public double getTemp() {
        return magMotor.getMotorTemperature();
    }

}

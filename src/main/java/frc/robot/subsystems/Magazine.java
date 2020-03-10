package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating magazine function.
 */
public class Magazine extends SubsystemBase {

    // device initialization
    private final CANSparkMax magMotor = new CANSparkMax(24, MotorType.kBrushless);
    private final CANEncoder magEncoder = new CANEncoder(magMotor);
    private final DigitalInput botBeam = new DigitalInput(0);
    private final DigitalInput topBeam = new DigitalInput(1);

    // constants
    private static final double RAMP_RATE = 0.05; // seconds

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        setRampRate();
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
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("M24 temp", magMotor.getMotorTemperature());
        SmartDashboard.putBoolean("Beam Breaker", getBotBeam());
    }

    /**
     * Sets belt motor to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        double newPercent = SmartDashboard.getNumber("Magazine Speed", percent);
        if(percent < 0) {
        percent = -1 * newPercent;
        }
        else if(percent == 0.0) {
            percent = 0;
        }
        else {
            percent = newPercent;
        }
        magMotor.set(percent);
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

    public boolean getBotBeam() {
        return botBeam.get();
    }

    public boolean getTopBeam() {
        return topBeam.get();
    }
    
}

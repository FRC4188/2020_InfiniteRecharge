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
    private final DigitalInput botBeam = new DigitalInput(2);
    private final DigitalInput topBeam = new DigitalInput(1);

    // constants
    private static final double RAMP_RATE = 0.2; // seconds

    private double ballCount = 0;
    private boolean loadedFire = false;

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        setRampRate();
        SmartDashboard.putNumber("Magazine Speed", 0.0);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
        if (ballCount >= 3) loadedFire = true;
        else loadedFire = false;
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("Magazine Encoder Position", magEncoder.getPosition());
        SmartDashboard.putNumber("M24 temp", magMotor.getMotorTemperature());
        SmartDashboard.putNumber("Ball count in magazine", getCount());
        SmartDashboard.putBoolean("Mag loaded", getLoadedFire());
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
     * Returns temperature of motor based off CANSpark ID.
     */
    public double getMotorTemperature() {
        return magMotor.getMotorTemperature();
    }

    public boolean getBotBeam() {
        return botBeam.get();
    }

    public boolean getTopBeam() {
        return topBeam.get();
    }

    public void setCount(double count) {
        ballCount = count;
    }

    public void setCount() {
        ballCount++;
    }

    public double getCount() {
        return ballCount;
    }

    public void setLoadedFire(boolean loaded) {
        loadedFire = loaded;
    }

    public boolean getLoadedFire() {
        return loadedFire;
    }
    
}

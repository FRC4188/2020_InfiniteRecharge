package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
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
    private final CANPIDController pid = new CANPIDController(magMotor);
    private final DigitalInput botBeam = new DigitalInput(0);
    public final DigitalInput topBeam = new DigitalInput(1);
    private final double ENCODER_TICKS_PER_REV = 3;
    private final double INITIAL_POSITION = 0 * ENCODER_TICKS_PER_REV;
    private static final double kP = 0.05;
    private static final double kI = 0;
    private static final double kD = 0.005;
    private static final double kI_ZONE = 0;

    // constants
    private static final double RAMP_RATE = 0.05; // seconds

    private double ballCount = 0;
    private boolean loadedFire = false;
    private boolean lsRelease = true;

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        setRampRate();
        controllerInit();
        magEncoder.setPosition(INITIAL_POSITION);
        SmartDashboard.putNumber("Magazine Speed", 0.9);
        ballCount = 0;
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        updateShuffleboard();
        if (ballCount >= 3) loadedFire = true;
        else loadedFire = false;

        if(getBotBeam() != lsRelease) {
            if(getBotBeam() == true) {
              lsRelease = true;
            }
            else {
              ballCount++;
              lsRelease = false;
            }
          }
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("Magazine position (revs)", getPosition());
        SmartDashboard.putNumber("M24 temp", magMotor.getMotorTemperature());
        SmartDashboard.putNumber("Ball count in magazine", getCount());
        SmartDashboard.putBoolean("Mag loaded", getLoadedFire());
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

    public void controllerInit() {
        magMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        pid.setP(kP);
        pid.setI(kI);
        pid.setD(kD);
        pid.setIZone(kI_ZONE);
        pid.setOutputRange(-1.0, 1.0);
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

    public double getPosition() {
        return magEncoder.getPosition() / ENCODER_TICKS_PER_REV;
    }

    public DigitalInput getTopBeamObject() {
        return topBeam;
    }

    public void autoMoveMag(double position) {
        position *= ENCODER_TICKS_PER_REV;
        if(!getBotBeam()){
            pid.setReference(magEncoder.getPosition() + position, ControlType.kPosition);
        }
    }
    
}

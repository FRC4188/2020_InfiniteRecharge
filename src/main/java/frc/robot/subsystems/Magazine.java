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
    private final DigitalInput midBeam = new DigitalInput(0);
    private final DigitalInput topBeam = new DigitalInput(1);
    // private final DigitalInput botBeam = new DigitalInput(2);
    private final CANPIDController magPid = new CANPIDController(magMotor);

    // constants
    private static final double RAMP_RATE = 0.05; // seconds
    //PID 
    private static final double kP = 1;//not a tested value
    private static final double kI = 1;//not a tested value
    private static final double kD = 1;//not a tested value
    private static final double kI_ZONE = 1;//not a tested value


    private boolean manual;

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        setRampRate();
        controllerInit();
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        magMotor.setInverted(true);
        updateShuffleboard();
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("M24 temp", magMotor.getMotorTemperature());
        // SmartDashboard.putBoolean("Bot Beam Breaker", botBeamClear());
        SmartDashboard.putBoolean("Top Beam Breaker", topBeamClear());
        SmartDashboard.putBoolean("Mid Beam Breaker", midBeamClear());
        SmartDashboard.putBoolean("Magazine manual", getManual());
    }

    /**
     * Configures gains for Spark closed loop controller.
     */
    private void controllerInit() {
        magPid.setP(kP);
        magPid.setI(kI);
        magPid.setD(kD);
        magPid.setIZone(kI_ZONE);
        //magPid.setFF(kF);
        magPid.setOutputRange(-1.0, 1.0);
    }

    /**
     * Sets belt motor to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        magMotor.set(percent);
    }

    /**
     * gets the current position of the mag (for use in setPosition())
     */
    public double getPosition(){
        return magEncoder.getPosition();
    }

    /**
     * Sets the mag motor to run to a position
     */
    public void setPosition(double pos){
        magPid.setReference(pos,ControlType.kPosition);
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

    /*public boolean botBeamClear() {
        return botBeam.get();
    }*/

    public boolean midBeamClear() {
        return midBeam.get();
    }

    public boolean topBeamClear() {
        return topBeam.get();
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean getManual() {
        return manual;
    }
    
}

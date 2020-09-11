package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating magazine function.
 */
public class Magazine extends SubsystemBase {

    // device initialization
    private final CANSparkMax magMotor = new CANSparkMax(11, MotorType.kBrushless);
    private final CANEncoder magEncoder = new CANEncoder(magMotor);
    private final CANPIDController pid = new CANPIDController(magMotor);

    private final DigitalInput midBeam = new DigitalInput(0);
    private final DigitalInput topBeam = new DigitalInput(1);
    private final DigitalInput botBeam = new DigitalInput(2);

    // Constants
    private static final double MAX_VELOCITY = 1400.0; // rpm
    private static final double MAX_ACCELERATION = 2800.0; // rpm/sec
    private static final double PPR = 42.0; // Encoder resolution.
    private static final double GEAR_RATIO = 3.0; // Gear ration from motor to output.
    private static final double TICKS_PER_REV = PPR*GEAR_RATIO; // Encoder ticks to output revolution.
    private static final double PULLEY_DIAMETER = 1.39; // Diameter of output pulley.
    private static final double PULLEY_CIRCUMFRENCE = PULLEY_DIAMETER * Math.PI; // Circumfrence of output pulley.
    private static final double TICKS_PER_INCH = TICKS_PER_REV /PULLEY_CIRCUMFRENCE; // Encoder ticks per inch of rotation on the output pulley.
    private static final double kP = 0; //4e-10;
    private static final double kI = 0; //1e-6;
    private static final double kD = 0;
    private static final double kF = 1.0 / MAX_VELOCITY;
    private static final double kI_ZONE = 0;
    private static final double RAMP_RATE = 0.3; // seconds

    private boolean manual;

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        setRampRate();

        SmartDashboard.putNumber("Mag P", kP);
        SmartDashboard.putNumber("Mag I", kI);
        SmartDashboard.putNumber("Mag D", kD);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        magMotor.setInverted(true);
        updateShuffleboard();
    }

    public void configController() {
        pid.setP(kP);
        pid.setI(kI);
        pid.setD(kD);
        pid.setFF(kF);
        pid.setIZone(kI_ZONE);
        pid.setOutputRange(-1.0, 1.0);
        pid.setSmartMotionMaxVelocity(MAX_VELOCITY, 0);
        pid.setSmartMotionMaxAccel(MAX_ACCELERATION, 0);
    }

    public void testPIDConfig( double P, double I, double D) {
        pid.setP(P);
        pid.setI(I);
        pid.setD(D);
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
        SmartDashboard.putNumber("Magazine Position", magEncoder.getPosition()/TICKS_PER_INCH);
    }

    /**
     * Sets belt motor to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        magMotor.set(percent);
    }

    /**
     * Increases the position of the motor by specified amount.
     * @param inches Inches to move magazine forward.
     */
    public void setIncrease(double inches) {
        double TickIncrease = inches * TICKS_PER_INCH;
        double SetPoint = magEncoder.getPosition() + TickIncrease;
        pid.setReference(SetPoint/10, ControlType.kPosition);
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

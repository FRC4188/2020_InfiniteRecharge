package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating magazine function.
 */
public class Magazine extends SubsystemBase {

    // device initialization
    private final CANSparkMax magMotor = new CANSparkMax(11, MotorType.kBrushless);
    private final CANEncoder magEncoder = new CANEncoder(magMotor);
    private final ProfiledPIDController magPID = new ProfiledPIDController(0.0003, 0.0, 0.000002, new Constraints(MAX_VELOCITY, MAX_ACCELERATION));
    private final SimpleMotorFeedforward magFF = new SimpleMotorFeedforward(kS, kV);

    private final DigitalInput topBeamA = new DigitalInput(0);
    private final DigitalInput topBeamB = new DigitalInput(1);
    private final DigitalInput midBeamA = new DigitalInput(3);
    private final DigitalInput midBeamB = new DigitalInput(4);
    private final DigitalInput entryBeamA = new DigitalInput(2);
    private final DigitalInput entryBeamB = new DigitalInput(5);

    // Constants
    private static final double MAX_VELOCITY = 1400.0; // rpm
    private static final double MAX_ACCELERATION = 2800.0; // rpm/sec
    private static final double PPR = 1.0; // Encoder resolution.
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
    private static final double kS = 0.06;
    private static final double kV = 1 / 4920;
    private static final double RAMP_RATE = 0.3; // seconds

    private boolean manual;

    private double reduction = 1;

    /**
     * Constructs new magazine object and configures devices.
     */
    public Magazine() {
        magMotor.setInverted(true);
        magMotor.setIdleMode(IdleMode.kBrake);
        setRampRate();
        SmartDashboard.putNumber("Set Magazine Voltage", 0.0);
        SmartDashboard.putNumber("Set Magazine Velocity", 0.0);
        SmartDashboard.putNumber("Set Magazine P", 0.0); 
        SmartDashboard.putNumber("Set Magazine D", 0.0);

        Notifier shuffle = new Notifier(() -> updateShuffleboard());
        shuffle.startPeriodic(0.1);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        //magMotor.setInverted(true);
        //updateShuffleboard();
        SmartDashboard.putNumber("Magazine Velocity", getVelocity());
    }

    /**
     * Writes values to Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("M24 temp", magMotor.getMotorTemperature());
        SmartDashboard.putBoolean("Top", (topBeamA.get() && topBeamB.get()));
        SmartDashboard.putBoolean("Mid", (midBeamA.get() && midBeamB.get()));
        SmartDashboard.putBoolean("Chn. 0", topBeamA.get());
        SmartDashboard.putBoolean("Chn. 1", topBeamB.get());
        SmartDashboard.putBoolean("Chn. 2", entryBeamA.get());
        SmartDashboard.putBoolean("Chn. 3", midBeamA.get());
        SmartDashboard.putBoolean("Chn. 4", midBeamB.get());
        SmartDashboard.putBoolean("Chn. 5", entryBeamB.get());
        SmartDashboard.putBoolean("Magazine manual", getManual());
        SmartDashboard.putNumber("Magazine Position", magEncoder.getPosition()/TICKS_PER_INCH);
    }

    /**
     * Sets belt motor to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        magMotor.set(percent * reduction);
    }

    public void setVelocity(double velocity) {
        magMotor.set(magPID.calculate(magEncoder.getVelocity(), velocity) + magFF.calculate(velocity));
    }

    public void setPIDs(double kP, double kD) {
        magPID.setPID(kP, 0, kD);
    }
    /**
     * Configures magazine motor ramp rates.
     */
    public void setRampRate() {
        magMotor.setClosedLoopRampRate(RAMP_RATE);
        magMotor.setOpenLoopRampRate(RAMP_RATE);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    /**
     * Returns magazine motor temperature in Celcius.
     */
    public double getTemp() {
        return magMotor.getMotorTemperature();
    }

    public boolean entryBeamClear() {
        return entryBeamA.get() && entryBeamB.get();
    }

    public boolean midBeamClear() {
        return (midBeamA.get() && midBeamB.get());
    }

    public boolean topBeamClear() {
        return (topBeamA.get() && topBeamB.get());
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean getManual() {
        return manual;
    }
    
    public double getPosition() {
        return magEncoder.getPosition() * TICKS_PER_INCH;
    }

    public double getVelocity() {
        return magEncoder.getVelocity();
    }
}

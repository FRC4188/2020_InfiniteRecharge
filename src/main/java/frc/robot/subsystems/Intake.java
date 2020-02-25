package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Class encapsulating intake function.
 */
public class Intake extends SubsystemBase {

    // device initialization
    private CANSparkMax intakeMotor = new CANSparkMax(11, MotorType.kBrushless);
    private CANSparkMax indexerMotor = new CANSparkMax(12, MotorType.kBrushless);
    private CANSparkMax polyRoller = new CANSparkMax(13, MotorType.kBrushless);
    private CANEncoder intakeMotorEncoder = intakeMotor.getEncoder();
    private CANEncoder indexerMotorEncoder = indexerMotor.getEncoder();
    private CANEncoder polyRollerEncoder = polyRoller.getEncoder();
    private Solenoid intakeSolenoid = new Solenoid(0);

    // constants
    private static final double RAMP_RATE = 0.5; // seconds

    // state vars
    private boolean isRaised = true;

    /**
     * Constructs new Intake object.
     */
    public Intake() {
        resetEncoders();
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
     * Prints values to ShuffleBoard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Intake Position", getIntakePosition());
        SmartDashboard.putNumber("Indexer Position", getIndexerPosition());
        SmartDashboard.putNumber("PolyRoller Position", getPolyRollerPosition());

    }

    /**
     * Spins the intake motor a given percent [-1.0, 1.0].
     */
    public void spin(double percent) {
        if (intakeSolenoid.get()) intakeMotor.set(percent);
        else intakeMotor.set(percent / 3);
        indexerMotor.set(percent);
        polyRoller.set(percent);
    }

    public void spinIntake(double percent) {
        intakeMotor.set(percent);
    }

    /**
     * Spins the indexer motors a given percent [-1.0, 1.0].
     */
    public void spinIndexer(double percent) {
        indexerMotor.set(percent);
    }

    /**
     * Spin poly rollers a given percent [-1.0, 1.0].
     */
    public void spinPolyRollers(double percent) {
        polyRoller.set(percent);
    }

    /**
     * Raises intake by firing solenoids.
     */
    public void raise() {
        intakeSolenoid.set(false);
        isRaised = false;
    }

    /**
     * Lowers intake by firing solenoid.
     */
    public void lower() {
        intakeSolenoid.set(true);
        isRaised = true;
    }

    /**
     * Returns true if the intake is currently raised.
     */
    public boolean isRaised() {
        return isRaised;
    }

    /**
     * Sets the encoder values to zero for intake, indexer, and poly roller.
     */
    public void resetEncoders() {
        intakeMotorEncoder.setPosition(0);
        indexerMotorEncoder.setPosition(0);
        polyRollerEncoder.setPosition(0);
    }

    /**
     * Configures motor ramp rates.
     */
    public void setRampRate() {
        intakeMotor.setOpenLoopRampRate(RAMP_RATE);
        indexerMotor.setOpenLoopRampRate(RAMP_RATE);
        polyRoller.setOpenLoopRampRate(RAMP_RATE);
    }

    /**
     * Returns indexer motor position in rotations.
     */
    public double getIndexerPosition() {
        return indexerMotorEncoder.getPosition();
    }

    /**
     * Returns intake motor position in rotations.
     */
    public double getIntakePosition() {
        return intakeMotorEncoder.getPosition();
    }

    /**
     * Returns polyroller motor position in rotations.
     */
    public double getPolyRollerPosition() {
        return polyRollerEncoder.getPosition();
    }

    /** 
     * Returns temperature of motor based off CANSpark ID. 
     */
    public double getMotorTemperature(int index) {
        CANSparkMax[] sparks = new CANSparkMax[] {
            intakeMotor,
            indexerMotor,
            polyRoller,
        };
        index -= 1;
        double temp = -1.0;
        try {
            temp = sparks[index - 10].getMotorTemperature();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: index " + index + " not in array of intake sparks.");
        }
        return temp;
    }
}

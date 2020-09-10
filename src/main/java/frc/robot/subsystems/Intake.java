package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
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
    private WPI_TalonSRX intakeMotor = new WPI_TalonSRX(24);
    private CANSparkMax indexerMotor = new CANSparkMax(12, MotorType.kBrushless);
    //private CANSparkMax polyRoller = new CANSparkMax(13, MotorType.kBrushless);
    private CANEncoder indexerMotorEncoder = indexerMotor.getEncoder();
    //private CANEncoder polyRollerEncoder = polyRoller.getEncoder();
    private Solenoid intakeSolenoid = new Solenoid(0);

    // constants
    private static final double RAMP_RATE = 0.5; // seconds

    double intakeSet;

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

        //ballCount = (int) SmartDashboard.getNumber("Beam Breaker", ballCount);
        if (isRaised) {
            intakeSolenoid.set(false);
        } else {
            intakeSolenoid.set(true);
        }
    }

    /**
     * Prints values to ShuffleBoard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Intake Position", getIntakePosition());
        SmartDashboard.putNumber("Indexer Position", getIndexerPosition());
        //SmartDashboard.putNumber("PolyRoller Position", getPolyRollerPosition());
        SmartDashboard.putBoolean("Intake Raised", isRaised());
        SmartDashboard.putNumber("Intake Motor Set", intakeSet);
    }

    /**
     * Spins the intake motor a given percent [-1.0, 1.0].
     */
    public void spin(double intake, double indexer) {
        this.intakeSet = intake;
        if (intakeSolenoid.get()) intakeMotor.set(intake);
        else intakeMotor.set(intake / 3);   
        indexerMotor.set(indexer);
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
        //polyRoller.set(percent);
    }

    /**
     * Raises intake by firing solenoids.
     */
    public void raise() {
        isRaised = true;
    }

    /**
     * Lowers intake by firing solenoid.
     */
    public void lower() {
        isRaised = false;
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
        intakeMotor.setSelectedSensorPosition(0);
        indexerMotorEncoder.setPosition(0);
        //polyRollerEncoder.setPosition(0);
    }

    /**
     * Configures motor ramp rates.
     */
    public void setRampRate() {
        intakeMotor.configOpenloopRamp(RAMP_RATE);
        indexerMotor.setOpenLoopRampRate(RAMP_RATE);
        //polyRoller.setOpenLoopRampRate(RAMP_RATE);
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
        return intakeMotor.getSelectedSensorPosition();
    }

    /**
     * Returns polyroller motor position in rotations.
     */
    /*public double getPolyRollerPosition() {
        return polyRollerEncoder.getPosition();
    }*/

    /**
     * Returns intake motor temperature in Celcius.
     */
    public double getIntakeTemp() {
        return intakeMotor.getTemperature();
    }

    /**
     * Returns indexer motor temperature in Celcius.
     */
    public double getIndexerTemp() {
        return indexerMotor.getMotorTemperature();
    }

    /**
     * Returns poly roller motor temperature in Celcius.
     */
    /*public double getPolyRollerTemp() {
        return polyRoller.getMotorTemperature();
    }*/

}

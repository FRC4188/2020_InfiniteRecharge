package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Coworking;
/**
 * Class encapsulating intake function.
 */
public class Intake extends SubsystemBase {

    // device initialization
    private CANSparkMax intakeMotor = new CANSparkMax(27, MotorType.kBrushless);
    private CANEncoder intakeEncoder = intakeMotor.getEncoder();
    private CANSparkMax indexerMotor = new CANSparkMax(12, MotorType.kBrushless);
    private CANEncoder indexerMotorEncoder = indexerMotor.getEncoder();
    private Solenoid intakeSolenoid = new Solenoid(0);

    // constants
    private static final double RAMP_RATE = 0.15; // seconds
    private static final double INTAKE_TIMEOUT = 20;

    double intakeSet;

    double reduction = 1;

    // state vars
    private boolean isRaised = true;

    /**
     * Constructs new Intake object.
     */
    public Intake() {
        resetEncoders();
        setRampRate();
        intakeMotor.setIdleMode(IdleMode.kCoast);

        Notifier shuffle = new Notifier(() -> updateShuffleboard());
        //shuffle.startPeriodic(0.1);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        //updateShuffleboard();

        //ballCount = (int) SmartDashboard.getNumber("Beam Breaker", ballCount);
        /*
        if (isRaised) {
            intakeSolenoid.set(false);
        } else {
            intakeSolenoid.set(true);
        }
        */

        Coworking.getInstance().intakeSpeed = getIntakeVelocity();
        Coworking.getInstance().intakeSet = getIntakeSet();
    }

    /**
     * Prints values to ShuffleBoard.
     */
    private void updateShuffleboard() {
        SmartDashboard.putNumber("Intake Position", getIntakePosition());
        SmartDashboard.putNumber("Indexer Position", getIndexerPosition());
        //SmartDashboard.putNumber("PolyRoller Position", getPolyRollerPosition());
        SmartDashboard.putBoolean("Intake Raised", isRaised());
        SmartDashboard.putNumber("Intake Motor Set", intakeSet);
        SmartDashboard.putNumber("Intake Temp", intakeMotor.getMotorTemperature());
        SmartDashboard.putNumber("Indexer Temp", indexerMotor.getMotorTemperature());
        SmartDashboard.putNumber("Intake Velocity", getIntakeVelocity());
    }

    /**
     * Spins the intake motor a given percent [-1.0, 1.0].
     */
    public void spin(double intake, double indexer) {
        this.intakeSet = intake * reduction;
        if (intakeSolenoid.get()) intakeMotor.set(intake/2);
        else intakeMotor.set(intake / 3);   
        indexerMotor.set(indexer * reduction);
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
        intakeSolenoid.set(false);
    }

    /**
     * Lowers intake by firing solenoid.
     */
    public void lower() {
        intakeSolenoid.set(true);
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
        intakeEncoder.setPosition(0);
        indexerMotorEncoder.setPosition(0);
        //polyRollerEncoder.setPosition(0);
    }

    /**
     * Configures motor ramp rates.
     */
    public void setRampRate() {
        intakeMotor.setOpenLoopRampRate(RAMP_RATE);
        indexerMotor.setOpenLoopRampRate(RAMP_RATE);
        //polyRoller.setOpenLoopRampRate(RAMP_RATE);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public double getIntakeVelocity() {
        return intakeEncoder.getVelocity();
    }

    public double getIntakeSet() {
        return intakeMotor.get();
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
        return intakeEncoder.getPosition();
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
        return intakeMotor.getMotorTemperature();
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

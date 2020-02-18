package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

public class Intake extends SubsystemBase {

    //device initialization
    private CANSparkMax intakeMotor = new CANSparkMax(11, MotorType.kBrushless);
    private CANSparkMax indexerMotor = new CANSparkMax(12, MotorType.kBrushless);
    private CANSparkMax polyRoller = new CANSparkMax(13, MotorType.kBrushless);
    private CANEncoder intakeMotorEncoder = intakeMotor.getEncoder();
    private CANEncoder indexerMotorEncoder = indexerMotor.getEncoder();
    private CANEncoder polyRollerEncoder = polyRoller.getEncoder();
    private Solenoid intakeSolenoid = new Solenoid(0);
    private CANPIDController pidC = intakeMotor.getPIDController();
    private DigitalInput intakeBB1 = new DigitalInput(0);
    private DigitalInput intakeBB2 = new DigitalInput(1);
    private DigitalInput magazineBB = new DigitalInput(2);
    private boolean lsRelease = true;
    private boolean lsRelease2 = true;
    private boolean bbRelease = false;

    
    // variables
    public static int ballCount = 0;
    private boolean intakeInverted = false;
    private boolean indexerInverted = false;

    // constants
    //private final double GEAR_RATIO = 300; //needs to be assigned
    private final double ENCODER_TO_FEET = 2.0 / 36.047279;
    private final double RAMP_RATE = 0.5; // seconds
    private final double MAX_VELOCITY = 3000; // rpm
    private final double MAX_OUT = 0.5; // percent out
    private final double kP = 5e-5;
    private final double kI = 1e-6;
    private final double kI_ZONE = 0;
    private final double kD = 0;
    private final double kF = 0;
    private final double MAX_ACCELERATION = 2500;
    private final int    SLOT_ID = 0;

    /**
     * Constructs new Intake object.
     */
    public Intake() {
      controllerInit();
      resetEncoders();
    }

    /** Runs every loop. */
    @Override
    public void periodic() {

      /**
       * Adds to ballCount if the limit switch is clicked.
       */

      if(intakeBB1.get() != lsRelease) {
        if(intakeBB1.get() == true) {
          lsRelease = true;
        }
        else {
          ballCount++;
          System.out.println(ballCount);
          lsRelease = false;
        }
      }
      else if(intakeBB2.get() != lsRelease2) {
        if(intakeBB2.get() == true) {
          lsRelease2 = true;
        }
        else {
          ballCount++;
          System.out.println(ballCount);
          lsRelease2 = false;
        }
      }
      else if(magazineBB.get() != bbRelease) {
          if(magazineBB.get() == false) {
              bbRelease = false;
          }
          else {
              ballCount++;
              System.out.println(ballCount);
              bbRelease = true;
          }
      }
      else {}

      updateShuffleboard();
    }

    private void controllerInit() {
        pidC.setP(kP);
        pidC.setI(kI);
        pidC.setIZone(kI_ZONE);
        pidC.setD(kD);
        pidC.setFF(kF);
        pidC.setOutputRange(-MAX_OUT, MAX_OUT);
        pidC.setSmartMotionMaxVelocity(MAX_VELOCITY, SLOT_ID);
        pidC.setSmartMotionMaxAccel(MAX_ACCELERATION, SLOT_ID);
        intakeMotor.setClosedLoopRampRate(RAMP_RATE);
        intakeMotor.setOpenLoopRampRate(RAMP_RATE);
        indexerMotor.setClosedLoopRampRate(RAMP_RATE);
        indexerMotor.setOpenLoopRampRate(RAMP_RATE);
        polyRoller.setClosedLoopRampRate(RAMP_RATE);
        polyRoller.setOpenLoopRampRate(RAMP_RATE);
    }

    public void resetEncoders() {
      //double init = INITIAL_ANGLE / ENCODER_TO_FEET;
      intakeMotorEncoder.setPosition(0);
      indexerMotorEncoder.setPosition(0);
      polyRollerEncoder.setPosition(0);

    }

    public void updateShuffleboard() {
        SmartDashboard.putNumber("Balls", ballCount);
        SmartDashboard.putNumber("I11 Temp", intakeMotor.getMotorTemperature());
        SmartDashboard.putNumber("I12 Temp", indexerMotor.getMotorTemperature());
        SmartDashboard.putNumber("I13 Temp", polyRoller.getMotorTemperature());
    }

    /**
     * Deploys intake solenoid.
     */
    public void setSolenoid(boolean output) {
       // intakeSolenoid.set(value);
       // intakeSolenoid.set(Value.kOff);
       intakeSolenoid.set(output);
    }

    /**
     * Spins the intake motor based on a given percent.
     * The motor only spins if the number of balls is less 
     * than 0.
     */
    public void spinIntake(double percent) {
       if(ballCount < 5) {
            intakeMotor.set(percent);
       }
       else {
          intakeMotor.set(0);
       }
    }

    /**
     * Spins the indexer motors together.
     * They only spin if the number of balls is less than
     * or equal to 0.
     */
    public void spinIndexer(double percent) {
      System.out.println("Spin index");
        indexerMotor.set(percent);
    }

    /**
     * Spin poly rollers.
     */
    public void spinPolyRollers(double percent) {
        polyRoller.set(percent);
    }

    /**
     * Inverts the intake motors.
     */
    public void setIntakeInverted(boolean isInverted) {
        if(intakeInverted) isInverted = !isInverted;
        intakeMotor.setInverted(isInverted);
    }

    /**
     * Inverts the indexer motors.
     */
    public void setIndexerInverted(boolean isInverted) {
        if(indexerInverted) isInverted = !isInverted;
        //indexer1.setInverted(isInverted);
        //indexer2.setInverted(isInverted);
    }

    /**
     * Manually changes the ball count.
     */
    public void changeBallCount(int value) {
      ballCount += value;
    }

    /*public boolean indexerInterval() {
      return (intakeBB1.get() || intakeBB2.get()) && magazineBB.get();
    }
    public boolean magazineGetter() {
      return magazineBB.get();
    }*/

    public int getBallCount() {
      return ballCount;
    }

    /**
     * getter for the indexerMotor's encoder.
     * @return
     */
    public double getIndexerMotorEncoder() {
      return indexerMotorEncoder.getPosition();
    }

    /**
     * getter for the intakeMotor's encoder.
     * @return
     */
    public double getIntakeMotorEncoder() {
      return intakeMotorEncoder.getPosition();
    }

    /**
     * getter for the pollyRoller's encoder.
     */
    public double getPollyRollerEncoder() {
      return polyRollerEncoder.getPosition();
    }

    /**
     * supposed to turn the intake a set distance measured in feet.
     * @param position how far you want the motors to turn
     * @param tolerance the tolerance of the PID loop
     */
    public void turnToPosition(double position, double tolerance) {
      position /= ENCODER_TO_FEET;
      tolerance /= ENCODER_TO_FEET;
      pidC.setSmartMotionAllowedClosedLoopError(tolerance, SLOT_ID);
      pidC.setReference(position, ControlType.kSmartMotion);
    }
}
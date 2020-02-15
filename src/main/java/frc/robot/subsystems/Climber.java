package frc.robot.subsystems;

import java.sql.Time;
import java.util.concurrent.TimeoutException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

    //constants
    private static final double GEAR_RATIO = (58/11)*(20/60);// needs to be verified.
    private static final double ENCODER_TICS_PER_REV = 2048.0;
    private static final double ENCODER_TO_REV = 1.0 / (GEAR_RATIO *ENCODER_TICS_PER_REV);
    private static final double MAX_VELOCITY = 20000.0;
    private static final double kP = 0.4;//porportion (will be * error (= at - want))
    private static final double kD = 0.0; //constant * derivtive: change in  error over time will be negative * kD
    private static final double kI = 0.0; //integral sums up error (see )
    private static final double kF = 1023 / MAX_VELOCITY;//accel
    private static final double RAMP_RATE = .2;//seconds
    private static final int TIMEOUT = 10;//ms
    private static final int CRUISE_ACCEL = 15000;
    private static final int CRUISE_VEL = 15000;
    
    //feed forward: kV = 1/ max vel

    /**
    private static final DigitalInput LIMITSWITCH1_DIGITAL_INPUT = new DigitalInput(6);//right
    private static final DigitalInput LIMITSWITCH2_DIGITAL_INPUT = new DigitalInput(7);//left    
    */

    //old limit switches
    //private static final DigitalInput LIMITSWITCH1_DIGITAL_INPUT = new DigitalInput(6);//upper right
    //private static final DigitalInput LIMITSWITCH2_DIGITAL_INPUT = new DigitalInput(7);//lower right
    //private static final DigitalInput LIMITSWITCH3_DIGITAL_INPUT = new DigitalInput(8);//upper left
    //private static final DigitalInput LIMITSWITCH4_DIGITAL_INPUT = new DigitalInput(9);//lower left

    //Pneu

    //Pneumatic
    private Solenoid ClimberSolenoid = new Solenoid(0);//needs to change
    boolean isBreakEngaged;

    //motor init
    private WPI_TalonFX climberLeftMotor = new WPI_TalonFX(31);
    private WPI_TalonFX climberRightMotor = new WPI_TalonFX(32);
    
    private double climberSpeed;
    private double velocity;

    /**
     * Creates a new Climber.
     */
    public Climber() {
      //setup encoders
      climberLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
      climberRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
      controllerInit();
      setBrake();

      climberLeftMotor.setInverted(true);

      //reset devices
      resetEncoders();

      //get SmartDashboard value for the speed
      SmartDashboard.putNumber("set climber speed", 0.0);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
      updateShuffleboard(); 
      setClimberSpeed();
    }

    /**
     * Writes values to the Shuffleboard
     */
    public void updateShuffleboard(){
      SmartDashboard.putNumber("Left climber height", getLeftPosition());
      SmartDashboard.putNumber("Right climber height", getRightPosition());
      SmartDashboard.putBoolean("Pneumatic Brakes engaged", getBrakeState());
      SmartDashboard.putNumber("Climber Speed Value", getClimberSpeed());
      SmartDashboard.putNumber("Left Velocity",getClimberLeftVel());
      SmartDashboard.putNumber("Right Velocity", getClimberRightVel());
      /**
      SmartDashboard.putBoolean("Limit Switch Right", getRightMagSwitch());
      SmartDashboard.putBoolean("Limit Switch Left", getLeftMagSwitch());
      */
      //old
      //SmartDashboard.putBoolean("LimitSwitch upper right active", getUpperRightLimitSwitch());
      //SmartDashboard.putBoolean("LimitSwitch lower right active", getLowerRightLimitSwitch());
      //SmartDashboard.putBoolean("LimitSwitch upper right active", getUpperLeftLimitSwitch());
      //SmartDashboard.putBoolean("LimitSwitch upper right active", getLowerRightLimitSwitch());
    }
    
    /**
     * Set the climber speed
     */
    public void setClimberSpeed(){
      climberSpeed = SmartDashboard.getNumber("Set climber speed", 0.0);
    }
    
    /**
     * returns the climber set speed
     */
    public double getClimberSpeed(){
      return climberSpeed;
    }

    /**
     * returns the left climber velocity
     */
    public double getClimberLeftVel(){
      return climberLeftMotor.getSelectedSensorVelocity() * ENCODER_TO_REV * 600;//sensor units for rpm
    }

    /**
     * returns the right climber velocity
     */
    public double getClimberRightVel(){
      return climberRightMotor.getSelectedSensorVelocity() * ENCODER_TO_REV * 600;//sensor units for rpm
    }

    /**
     * Config Pid loop stuff. Have Locke explain
     */
    public void controllerInit(){
      climberLeftMotor.config_kI(0, kI, TIMEOUT);
      climberLeftMotor.config_kD(0, kD, TIMEOUT);
      climberLeftMotor.config_kP(0, kP, TIMEOUT);
      climberLeftMotor.config_kF(0, kF, TIMEOUT);
      climberRightMotor.config_kI(0, kI, TIMEOUT);
      climberRightMotor.config_kD(0, kD, TIMEOUT);
      climberRightMotor.config_kP(0, kP, TIMEOUT);
      climberRightMotor.config_kF(0, kF, TIMEOUT);
    }

    /**
     * Sets both of the motors to run at a percentage of the max speed (-1.0, 1.0)
     */
    public void setSpeedPercentage(double percentage){
      climberLeftMotor.set(percentage);
      climberRightMotor.set(percentage);
    }

    /**
     * Sets the left motor to run at a percentage of the max speed (-1.0, 1.0)
     */
    public void setLeftPercentage(double percentage){
      climberLeftMotor.set(percentage);
    }

    /**
     * Sets the right motor to run at a percentage of the max speed (-1.0, 1.0)
     */
    public void setRightPercentage(double percentage){
      climberLeftMotor.set(percentage);
    }

    /**
     * set the motors to run at the given velocity in rpm.
     */
    public void setVelocity(double velocity) {
      velocity = velocity / (ENCODER_TO_REV * 600);
      climberLeftMotor.set(ControlMode.Velocity, velocity);
      climberRightMotor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Fires the break pistons to stop the climber
     */
    public void engagePneuBreak() {
      ClimberSolenoid.set(true);
    }

    /**
     * returns true if the pneu brakes are engaged
     */
    public boolean getBrakeState() {
      return ClimberSolenoid.get();
    }

    /**
       * Sets Climber motors to brake mode.
       */
      public void setBrake() {
        climberLeftMotor.setNeutralMode(NeutralMode.Brake);
        climberRightMotor.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * sets the drivetrain to coast mode
     */
    public void setCoast() {
      climberLeftMotor.setNeutralMode(NeutralMode.Coast);
      climberRightMotor.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * returns true if the right magnetic switch senses the other part
     */
    //public boolean getRightMagSwitch(){
      //return LIMITSWITCH1_DIGITAL_INPUT.get();
    //}

    /**
     * returns true if the left magnetic switch senses the other part
     */
    //public boolean getLeftMagSwitch(){
      //return LIMITSWITCH2_DIGITAL_INPUT.get();
    //}

    //old switch stuff
    /**
     * returns true if the upper right switch is active
     */
    //public boolean getUpperRightLimitSwitch(){
     // return LIMITSWITCH1_DIGITAL_INPUT.get();
    //}

    /**
     * returns true if the lower right switch is active
     */
    //public boolean getLowerRightLimitSwitch(){
     // return LIMITSWITCH2_DIGITAL_INPUT.get();
    //}

    /**
     * returns true if the upper left switch is active
     */
    //public boolean getUpperLeftLimitSwitch(){
      //return LIMITSWITCH3_DIGITAL_INPUT.get();
    //}

    /**
     * returns true if the lower left switch is active
     */
    //public boolean getLowerLeftLimitSwitch(){
      //return LIMITSWITCH4_DIGITAL_INPUT.get();
    //}

    /**
       * Resets encoder values to 0 for both sides of Climber.
       */
      public void resetEncoders() {
        climberLeftMotor.setSelectedSensorPosition(0);
        climberRightMotor.setSelectedSensorPosition(0);
    }

    /**
       * Returns left encoder position in feet.
       */
      public double getLeftPosition() {
        return climberLeftMotor.getSelectedSensorPosition() * ENCODER_TO_REV;
    }

    /**
     * Returns right encoder position in feet.
     */
    public double getRightPosition() {
        return climberRightMotor.getSelectedSensorPosition() * ENCODER_TO_REV;
    }

    /**
     * Sets the motors to run in opposite direction than before
     */
    public void setInverted(){
      climberRightMotor.setInverted(InvertType.InvertMotorOutput);
      climberLeftMotor.setInverted(InvertType.InvertMotorOutput);
    }

    /**
     * returns if the right side of the climber is up (based on Limit Switches)
     */
    public boolean isRightClimberUp(){
       //if(getUpperRightLimitSwitch() && ! getLowerRightLimitSwitch()) return true;
        return false;
    }
    
    /**
     * returns if the right side of the climber is up (based on Limit Switches)
     */
    public boolean isLeftClimberUp(){
      //if(getUpperLeftLimitSwitch() && ! getLowerLeftLimitSwitch()) return true;
      return false;
   }
   
    /**
     * returns if the right side of the climber is down (based on mag Limit Switches)
     */
    public boolean isRightClimberDown(){
      //if (getRightMagSwitch()) return true;
       return false;
   }

    /**
     * returns if the left side of the climber is down (based on mag Limit Switches)
     */
    public boolean isLeftClimberDown(){
      //if (getLeftMagSwitch()) return true;
      return false;
   }

}
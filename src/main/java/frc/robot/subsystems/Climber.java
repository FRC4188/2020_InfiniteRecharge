package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.SolenoidBase;

public class Climber extends SubsystemBase {

  //constants
  private static final double SPROKET_DIAMETER = 0.0;//in feet  NEEDS TO CHANGE
  private static final double GEAR_RATIO = (11 / 58) * (20 / 56);// needs to be verified. 
  
  //encoder
  private static final double ENCODER_TICS_PER_REV = 2048;
  private static final double ENCODER_TO_FEET = (Math.PI * SPROKET_DIAMETER) / (GEAR_RATIO * ENCODER_TICS_PER_REV);



  //Pneumatic
  boolean isBreakEngaged;

  //motor init
  private TalonFX climberLeftMotor = new TalonFX(31);//needs to change before running
  private TalonFX climberRightMotor = new TalonFX(32);

  
  /**
   * Creates a new Climber.
   */
  public Climber() {
    //setup encoders
    climberLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    //climberRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
 
    //reset devices
    resetEncoders();
    //there might be a gyro here for balance... for a veteran to add...
  }

  /**
   * Runs every loop.
   */
  @Override
  public void periodic() {
    updateShuffleboard();
  }

  /**
   * Writes values to the Shuffleboard
   */
  public void updateShuffleboard(){
    SmartDashboard.putNumber("Left climber height", getLeftPosition());
    SmartDashboard.putNumber("Right climber height", getRightPosition());
    SmartDashboard.putBoolean("Pneumatic Brakes engaged", getBrakeState());
  }
  

  /**
   * Sets the left climber motor to run at a percentage of the max speed (-1.0, 1.0)
   */
  public void setSpeedPercentage(double percentage){
    climberLeftMotor.set(ControlMode.PercentOutput, percentage);
    climberRightMotor.set(ControlMode.PercentOutput, -percentage);
  }

  /**
   * Fires the break pistons to stop the climber
   */
  public void engageBreak() {
    isBreakEngaged = true;
  }
  /**
   * returns true if the pneu brakes are engaged
   */
  public boolean getBrakeState() {
    return isBreakEngaged;
  }

  /**
     * Sets Climber motors to brake mode.
     */
    public void setBrake() {
      climberLeftMotor.setNeutralMode(NeutralMode.Brake);
      climberRightMotor.setNeutralMode(NeutralMode.Brake);
  }

  /**
   * Sets Climber motors to coast mode. Is this needed?
   */
  public void setCoast() {
      climberLeftMotor.setNeutralMode(NeutralMode.Coast);
      climberRightMotor.setNeutralMode(NeutralMode.Coast);
  }

  /**
     * Resets encoder values to 0 for both sides of Climber.
     */
    private void resetEncoders() {
      climberLeftMotor.setSelectedSensorPosition(0);
      climberRightMotor.setSelectedSensorPosition(0);
  }

  /**
     * Returns left encoder position in feet.
     */
    public double getLeftPosition() {
      return climberLeftMotor.getSelectedSensorPosition() * ENCODER_TO_FEET;
  }

  /**
   * Returns right encoder position in feet.
   */
  public double getRightPosition() {
      return climberRightMotor.getSelectedSensorPosition() * ENCODER_TO_FEET;
  }

  /**
   * Sets the motors to run in opposite direction than before
   */
  public void setInverted(){
    climberRightMotor.setInverted(InvertType.InvertMotorOutput);
    climberLeftMotor.setInverted(InvertType.InvertMotorOutput);
  }
}
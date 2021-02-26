// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.simulation.AnalogGyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.components.CSPFalcon;

public class DiffDrive extends SubsystemBase {

  private final CSPFalcon m_leftMotor = new CSPFalcon(1);
  private final CSPFalcon m_leftFollower = new CSPFalcon(2);
  private final CSPFalcon m_rightMotor = new CSPFalcon(3);
  private final CSPFalcon m_rightFollower = new CSPFalcon(4);

  private final Encoder m_leftEncoder = new Encoder(0, 1);
  private final Encoder m_rightEncoder = new Encoder(2, 3);

  private final PIDController m_leftPIDController = Constants.drive.PID;
  private final PIDController m_rightPIDController = Constants.drive.PID;

  private final AnalogGyro m_gyro = new AnalogGyro(0);

  private final DifferentialDriveKinematics m_kinematics = Constants.drive.KINEMATICS;
  private final DifferentialDriveOdometry m_odometry =
      new DifferentialDriveOdometry(m_gyro.getRotation2d());

  // Gains are for example purposes only - must be determined for your own
  // robot!
  private final SimpleMotorFeedforward m_feedforward = Constants.drive.FEEDFORWARD;

  // Simulation classes help us simulate our robot
  private final AnalogGyroSim m_gyroSim = new AnalogGyroSim(m_gyro);
  private final EncoderSim m_leftEncoderSim = new EncoderSim(m_leftEncoder);
  private final EncoderSim m_rightEncoderSim = new EncoderSim(m_rightEncoder);
  private final DifferentialDrivetrainSim m_drivetrainSimulator =
      new DifferentialDrivetrainSim(
          DCMotor.getFalcon500(2),
          Constants.drive.GEARING,
          Constants.drive.MOMENT_OF_INTERIA,
          Constants.drive.MASS,
          Constants.drive.WHEEL_RADIUS,
          Constants.drive.TRACKWIDTH,
          null);

  Field2d m_fieldSim;
          
  /** Creates a new DiffDrive. */
  public DiffDrive(Field2d m_fieldSim) {
    this.m_fieldSim = m_fieldSim;

    m_leftEncoder.setDistancePerPulse(2 * Math.PI * Constants.drive.WHEEL_RADIUS / (Constants.robot.FALCON_CPR * Constants.drive.GEARING));
    m_rightEncoder.setDistancePerPulse(2 * Math.PI * Constants.drive.WHEEL_RADIUS / (Constants.robot.FALCON_CPR * Constants.drive.GEARING));

    m_leftEncoder.reset();
    m_rightEncoder.reset();

    motorInit();

    resetGyro();
    resetOdometry(new Pose2d());

    SmartDashboard.putNumber("Drivetrain Set Voltage", 0.0);
    SmartDashboard.putNumber("Drivetrain Set Speed", 0.0);

    //Notifier shuffle = new Notifier(() -> updateShuffle());
    //shuffle.startPeriodic(0.1);
  }

  private final void motorInit() {
    m_leftFollower.follow(m_leftMotor);
    m_rightFollower.follow(m_rightMotor);

    m_rightMotor.setInverted(true);
  }

  /** Update odometry - this should be run every robot loop. */
  @Override
  public void periodic() {
    updateOdometry();
    m_fieldSim.setRobotPose(m_odometry.getPoseMeters());
    updateShuffle();
  }

  private void updateShuffle() {
    SmartDashboard.putNumber("Robot Speed", (getWheelSpeeds().leftMetersPerSecond + getWheelSpeeds().rightMetersPerSecond) / 2.0);
    SmartDashboard.putNumberArray("Motor Voltages", new double[] {m_leftMotor.get() * RobotController.getBatteryVoltage(), m_rightMotor.get() * RobotController.getBatteryVoltage()});
  }

  public void setVolts(double leftVoltage, double rightVoltage) {
    m_leftMotor.setVoltage(leftVoltage);
    m_rightMotor.setVoltage(rightVoltage);
  }

  /** Sets speeds to the drivetrain motors. */
  public void setSpeeds(DifferentialDriveWheelSpeeds speeds) {
    double leftFeedforward = m_feedforward.calculate(speeds.leftMetersPerSecond);
    double rightFeedforward = m_feedforward.calculate(speeds.rightMetersPerSecond);
    double leftOutput =
        m_leftPIDController.calculate(m_leftEncoder.getRate(), speeds.leftMetersPerSecond);
    double rightOutput =
        m_rightPIDController.calculate(m_rightEncoder.getRate(), speeds.rightMetersPerSecond);

    setVolts(leftOutput + leftFeedforward,
             rightOutput + rightFeedforward);
  }

  /** Sets the speeds to the drivetrain motors. */
  public void setSpeeds(double leftSpeed, double rightSpeed) {
    setSpeeds(new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed));
  }

  /**
   * Controls the robot using arcade drive.
   *
   * @param xSpeed the speed for the x axis
   * @param rot the rotation
   */
  @SuppressWarnings("ParameterName")
  public void drive(double xSpeed, double rot) {
    setSpeeds(m_kinematics.toWheelSpeeds(
      new ChassisSpeeds(
        xSpeed * Constants.drive.MAX_VEL,
        0,
        -rot * Constants.drive.MAX_ANGULAR_VEL)
      ));
  }

  /** Update robot odometry. */
  public void updateOdometry() {
    m_odometry.update(
        m_gyro.getRotation2d(), m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
  }

  /** Resets robot odometry. */
  public void resetOdometry(Pose2d pose) {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
    m_drivetrainSimulator.setPose(pose);
    m_odometry.resetPosition(pose, m_gyro.getRotation2d());
  }

  public void resetGyro() {
    m_gyro.reset();
  }

  /** Check the current robot pose. */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getRate(), m_rightEncoder.getRate());
  }

  public double getDrawnCurrent() {
    return m_drivetrainSimulator.getCurrentDrawAmps();
  }

  public double getAverageEncoderDistance() {
    return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance()) / 2.0;
  }

  public Encoder getLeftEncoder() {
    return m_leftEncoder;
  }

  public Encoder getRightEncoder() {
    return m_rightEncoder;
  }

  /** Update our simulation. This should be run every robot loop in simulation. */
  @Override
  public void simulationPeriodic() {
    // To update our simulation, we set motor voltage inputs, update the
    // simulation, and write the simulated positions and velocities to our
    // simulated encoder and gyro. We negate the right side so that positive
    // voltages make the right side move forward.
    m_drivetrainSimulator.setInputs(
        m_leftMotor.get() * RobotController.getInputVoltage(),
        -m_rightMotor.get() * RobotController.getInputVoltage());
    m_drivetrainSimulator.update(0.02);

    m_leftEncoderSim.setDistance(m_drivetrainSimulator.getLeftPositionMeters());
    m_leftEncoderSim.setRate(m_drivetrainSimulator.getLeftVelocityMetersPerSecond());
    m_rightEncoderSim.setDistance(m_drivetrainSimulator.getRightPositionMeters());
    m_rightEncoderSim.setRate(m_drivetrainSimulator.getRightVelocityMetersPerSecond());
    m_gyroSim.setAngle(-m_drivetrainSimulator.getHeading().getDegrees());
  }
}

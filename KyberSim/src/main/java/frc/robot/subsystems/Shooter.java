// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.LinearQuadraticRegulator;
import edu.wpi.first.wpilibj.estimator.KalmanFilter;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.system.LinearSystem;
import edu.wpi.first.wpilibj.system.LinearSystemLoop;
import edu.wpi.first.wpilibj.system.plant.DCMotor;
import edu.wpi.first.wpilibj.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.VecBuilder;
import edu.wpi.first.wpiutil.math.numbers.N1;
import frc.robot.Constants;
import frc.robot.utils.components.CSPFalcon;

public class Shooter extends SubsystemBase {
  private final CSPFalcon upperMotor = new CSPFalcon(5);
  private final CSPFalcon lowerMotor = new CSPFalcon(6);

  private final Encoder shootEncoder = new Encoder(4, 5);

  private final EncoderSim encoderSim = new EncoderSim(shootEncoder);
  private final FlywheelSim wheel = new FlywheelSim(DCMotor.getFalcon500(2), 1.0, 2.0);

  private final LinearSystem<N1, N1, N1> shooterPlant = LinearSystemId.createFlywheelSystem(
    DCMotor.getFalcon500(2), Constants.shooter.MOMENT_OF_INTERIA, Constants.shooter.GEARING);

  private final KalmanFilter<N1, N1, N1> shooterObserver = new KalmanFilter<>(
    Nat.N1(), Nat.N1(), shooterPlant, VecBuilder.fill(Constants.shooter.STATE_STANDARD_DEV), VecBuilder.fill(Constants.robot.FALCON_ENC_STANDARD_DEV), 0.02);

  private final LinearQuadraticRegulator<N1, N1, N1> shooterController = new LinearQuadraticRegulator<>(
    shooterPlant, VecBuilder.fill(Constants.shooter.QUELMS), VecBuilder.fill(Constants.shooter.RELMS), 0.02);

  private final LinearSystemLoop<N1, N1, N1> shooterLoop = new LinearSystemLoop<>(
    shooterPlant, shooterController, shooterObserver, 12.0, 0.02);
  
  private double reference = 0.0;
  
  /** Creates a new Shooter. */
  public Shooter() {
    lowerMotor.follow(upperMotor);

    shootEncoder.reset();
    shootEncoder.setDistancePerPulse(2.0 * Math.PI / Constants.robot.FALCON_CPR);
    shooterLoop.reset(VecBuilder.fill(shootEncoder.getRate()));

    SmartDashboard.putNumber("Shooter set Velocity", 0.0);
  }

  @Override
  public void periodic() {
    shooterLoop.setNextR(VecBuilder.fill(reference));
    shooterLoop.correct(VecBuilder.fill(shootEncoder.getRate()));
    shooterLoop.predict(0.02);

    double nextVoltage = shooterLoop.getU(0);
    upperMotor.setVoltage(nextVoltage);

    SmartDashboard.putNumber("Wheel RPM", wheel.getAngularVelocityRPM());
    SmartDashboard.putNumber("Encoder RPM", encoderSim.getRate());
    SmartDashboard.putNumber("Shooter Voltage", upperMotor.get() * RobotController.getInputVoltage());
  }

  @Override
  public void simulationPeriodic() {
    wheel.setInput(upperMotor.get() * RobotController.getInputVoltage());
    wheel.update(0.02);

    encoderSim.setRate(wheel.getAngularVelocityRPM());
  }

  public void setVelocity(double reference) {
    this.reference = reference;
  }

  public double getInitialVelocity() {
    return (wheel.getAngularVelocityRPM() * ((Constants.shooter.MAIN_WHEEL_CIRCUMFERENCE / Constants.shooter.MAIN_WHEEL_RATIO) + (Constants.shooter.AUX_WHEEL_CIRCUMFERENCE / Constants.shooter.AUX_WHEEL_RATIO))) / 120.0;
  }
}

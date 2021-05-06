// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.drive;
import frc.robot.Constants.shooter;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class InMotionFire extends CommandBase {
  Drivetrain drivetrain;
  Shooter shooter;
  Turret turret;
  Limelight limelight;

  double drivetrainMagnitude = 0;
  double resultingMagnitude = 0;
  double turretAngle = 0;

  /** Creates a new InMotionFire. */
  public InMotionFire(Drivetrain drivetrain, Shooter shooter, Turret turret, Limelight limelight) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivetrain = drivetrain;
    this.shooter = shooter;
    this.turret = turret;
    this.limelight = limelight;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double drivetrainMagnitude = (drivetrain.getLeftVelocity() + drivetrain.getLeftVelocity()) / 2; //meters per second
    double resultingMagnitude = (0.0762 * Math.PI * limelight.formulaRpm()) / 30; //meters per second
    double turretAngle = (turret.getPosition() > -1) ? 180 - turret.getPosition() : 180 + turret.getPosition();

    double angleA = Math.asin(Math.sin(Math.toRadians(turretAngle)) * drivetrainMagnitude / resultingMagnitude);
    double angleB = 180 - turretAngle - Math.toDegrees(angleA);
    
    double shooterMagnitude = (Math.sin(Math.toRadians(angleB)) * resultingMagnitude) / Math.sin(Math.toRadians(turretAngle)); //meters per second

    double shooterRPM = (30 * shooterMagnitude) / (0.0762 * Math.PI);

    SmartDashboard.putNumber("In Motion RPM", shooterRPM);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DiffDrive;

public class DriveSpeeds extends InstantCommand{

  DiffDrive drivetrain;
  double xVel;
  double zRot;

  /** Creates a new DriveSpeeds. */
  public DriveSpeeds(DiffDrive drivetrain, double xVel, double zRot) {
    addRequirements(drivetrain);

    this.drivetrain = drivetrain;
    this.xVel = xVel;
    this.zRot = zRot;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.drive(xVel, zRot);
  }
}

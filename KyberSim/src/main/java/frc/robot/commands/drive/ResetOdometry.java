// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DiffDrive;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ResetOdometry extends InstantCommand {

  DiffDrive drivetrain;
  Pose2d pose;

  public ResetOdometry(DiffDrive drivetrain, Pose2d pose) {
    this.drivetrain = drivetrain;
    this.pose = pose;
  }

  public ResetOdometry(DiffDrive drivetrain) {
    this(drivetrain, new Pose2d());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetOdometry(pose);
  }
}

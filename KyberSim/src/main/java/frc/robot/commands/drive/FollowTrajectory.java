// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.subsystems.DiffDrive;
import frc.robot.utils.paths.Waypoints;

public class FollowTrajectory extends RamseteCommand {
  /** Creates a new FollowTrajectory. */
  public FollowTrajectory(DiffDrive drivetrain, Trajectory trajectory) {
    super(
      trajectory,
      drivetrain::getPose,
      new RamseteController(2.0, 0.7),
      Constants.drive.KINEMATICS,
      drivetrain::setSpeeds,
      drivetrain);
  }

  public FollowTrajectory(DiffDrive drivetrain, Waypoints waypoints) {
    this(drivetrain, 
    TrajectoryGenerator.generateTrajectory(waypoints.getPoses(), Constants.drive.auto.config.setReversed(waypoints.isReversed())));
  }

  public FollowTrajectory(DiffDrive drivetrain, Waypoints waypoints, TrajectoryConfig config) {
    this(drivetrain, 
    TrajectoryGenerator.generateTrajectory(waypoints.getPoses(), config.setReversed(waypoints.isReversed())));
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import java.io.IOException;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.Waypoints;

public class FollowTrajectory extends RamseteCommand {
  /** Creates a new FollowTrajectory. */
  public FollowTrajectory(Drivetrain drivetrain, Trajectory trajectory) {
    super(trajectory, drivetrain::getPose, new RamseteController(2.0, 0.7), drivetrain.getKinematics(),
        drivetrain::setSpeeds, drivetrain);
  }

  public FollowTrajectory(Drivetrain drivetrain, Waypoints waypoints) {
    this(drivetrain, TrajectoryGenerator.generateTrajectory(waypoints.getPoses(),
        drivetrain.getTrajectoryConfig().setReversed(waypoints.isReversed())));
  }

  public FollowTrajectory(Drivetrain drivetrain, Waypoints waypoints, TrajectoryConfig config) {
    this(drivetrain,
        TrajectoryGenerator.generateTrajectory(waypoints.getPoses(), config.setReversed(waypoints.isReversed())));
  }

  public FollowTrajectory(Drivetrain drivetrain, String directory) throws IOException {
    this(drivetrain, TrajectoryUtil.fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve(directory)));
  }
}

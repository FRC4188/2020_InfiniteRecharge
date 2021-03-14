// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.Waypoints;
import frc.robot.utils.WaypointsList;

public class SkillsSlolam extends CspSequentialCommandGroup {
  /** Creates a new SkillsChallenge. */
  public SkillsSlolam(Drivetrain drivetrain) {
    addCommands(
      new FollowTrajectory(drivetrain, WaypointsList.Slolam.PATH, new TrajectoryConfig(5, 2.0)
      .addConstraint(new CentripetalAccelerationConstraint(1.5)))

    );
  }

  @Override
  public Pose2d getInitialPose() {
    return WaypointsList.Slolam.INIT_POSE;
  }
}

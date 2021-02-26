// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ResetOdometry;
import frc.robot.subsystems.DiffDrive;
import frc.robot.utils.paths.Waypoints;
import frc.robot.utils.paths.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SkillsChallenge extends SequentialCommandGroup {
  /** Creates a new SkillsChallenge. */
  public SkillsChallenge(DiffDrive drivetrain) {
    addCommands(
      new ResetOdometry(drivetrain, WaypointsList.SkillsObstacle.INIT_POSE),
      new FollowTrajectory(drivetrain, WaypointsList.SkillsObstacle.FIRSTMOVE)
    );
  }
}

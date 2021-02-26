// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ResetGyro;
import frc.robot.commands.drive.ResetOdometry;
import frc.robot.subsystems.DiffDrive;
import frc.robot.utils.paths.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MoveForward extends SequentialCommandGroup {
  /** Creates a new MoveForward. */
  public MoveForward(DiffDrive drivetrain) {
    addCommands(
      new ParallelCommandGroup(
        new ResetGyro(drivetrain),
        new ResetOdometry(drivetrain, WaypointsList.Test.INITIAL_POSE)
      ),

      new FollowTrajectory(drivetrain, WaypointsList.Test.ONE_METER)
    );
  }
}

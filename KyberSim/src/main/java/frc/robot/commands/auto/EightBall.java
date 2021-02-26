// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ResetGyro;
import frc.robot.commands.drive.ResetOdometry;
import frc.robot.subsystems.DiffDrive;
import frc.robot.utils.paths.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class EightBall extends SequentialCommandGroup {
  /** Creates a new TestAuto. */
  public EightBall(DiffDrive drivetrain) {
    addCommands(
      new ParallelCommandGroup(
        new ResetGyro(drivetrain),
        new ResetOdometry(drivetrain, WaypointsList.TrenchEightBall.INITIAL_POSE)
      ),

      new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.DOWN_TRENCH),
      new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.INTO_RENDEVOUS,
      new TrajectoryConfig(Constants.drive.MAX_VEL * 0.5, Constants.drive.MAX_ACCEL * 0.5)
      .addConstraint(Constants.drive.auto.C_ACCEL_CONSTRAINT)),
      new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.THROUGH_RENDEVOUS, Constants.drive.auto.config.setEndVelocity(0.25)),
      new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.TO_SHOOT, Constants.drive.auto.config.setStartVelocity(0.25))
    );
  }
}

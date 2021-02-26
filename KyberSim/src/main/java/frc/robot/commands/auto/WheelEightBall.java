// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ResetOdometry;
import frc.robot.subsystems.DiffDrive;
import frc.robot.utils.paths.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class WheelEightBall extends SequentialCommandGroup {
  /** Creates a new WheelEightBall. */
  public WheelEightBall(DiffDrive drivetrain) {
    addCommands(
      new ResetOdometry(drivetrain, WaypointsList.WheelEightBall.INIT_POSE),

      new FollowTrajectory(drivetrain, WaypointsList.WheelEightBall.TO_WHEEL),
      new FollowTrajectory(drivetrain, WaypointsList.WheelEightBall.TURN,
      new TrajectoryConfig(Constants.drive.MAX_VEL * 0.25, Constants.drive.MAX_ACCEL)
      .addConstraint(new CentripetalAccelerationConstraint(Constants.drive.MAX_CENTRIP))),
      new FollowTrajectory(drivetrain, WaypointsList.WheelEightBall.TO_FIRST_SHOT),
      new FollowTrajectory(drivetrain, WaypointsList.WheelEightBall.TURN_IN,
      Constants.drive.auto.config.setEndVelocity(0.25)),
      new FollowTrajectory(drivetrain, WaypointsList.WheelEightBall.THROUGH_TO_SHOOT,
      Constants.drive.auto.config.setStartVelocity(0.25))
    );
  }
}

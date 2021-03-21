// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.utils.Waypoints;
import frc.robot.utils.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class GalacticSearch extends SequentialCommandGroup {
  FollowTrajectory trajectory;

  /** Creates a new GalacticSearch. */
  public GalacticSearch(Drivetrain drivetrain, Intake intake, Limelight limelight) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    if (limelight.getPath() == 0) {
      trajectory = new FollowTrajectory(drivetrain, WaypointsList.GalacticSearch.REDA, new TrajectoryConfig(1.5, 1.0)
      .addConstraint(new CentripetalAccelerationConstraint(1.0)));
    } else if (limelight.getPath() == 1) {
      trajectory = new FollowTrajectory(drivetrain, WaypointsList.GalacticSearch.BLUEA, new TrajectoryConfig(1.5, 1.0)
      .addConstraint(new CentripetalAccelerationConstraint(1.0)));
    } else if (limelight.getPath() == 2) {
      trajectory = new FollowTrajectory(drivetrain, WaypointsList.GalacticSearch.REDB, new TrajectoryConfig(1.5, 1.0)
      .addConstraint(new CentripetalAccelerationConstraint(1.0)));
    } else if (limelight.getPath() == 3) {
      trajectory = new FollowTrajectory(drivetrain, WaypointsList.GalacticSearch.BLUEB, new TrajectoryConfig(1.5, 1.0)
      .addConstraint(new CentripetalAccelerationConstraint(1.0)));
    } else trajectory = null;
    addCommands(
      new ParallelDeadlineGroup(
        trajectory, 
        new RunCommand(() -> intake.lower()),
        new RunCommand(() -> intake.spin(-1.0, 0.0), intake))
    );
  }
}

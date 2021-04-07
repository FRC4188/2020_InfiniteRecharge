// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import java.util.List;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.Waypoints;
import frc.robot.utils.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShootingRoutine extends SequentialCommandGroup {
  /** Creates a new ShootingRoutine. */
  public ShootingRoutine(Drivetrain drivetrain) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new FollowTrajectory(drivetrain, new Waypoints(List.of(WaypointsList.SkillsPowerPort.TO_REINTRO, WaypointsList.SkillsPowerPort.TO_SHOOT), false), 
        new TrajectoryConfig(4.0, 1.5).addConstraint(new CentripetalAccelerationConstraint(1.0)))
    );
  }
}

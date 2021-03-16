// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import java.util.List;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.Waypoints;
import frc.robot.utils.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RedRoutine extends SequentialCommandGroup {
  /** Creates a new ToBlueRoutine. */
  public RedRoutine(Drivetrain drivetrain, Intake intake, Magazine magazine, Limelight limelight, Shooter shooter, Turret turret) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
    new FollowTrajectory(drivetrain, new Waypoints(List.of(drivetrain.getPose(), WaypointsList.SkillsAccuracy.TO_RED), false), 
    new TrajectoryConfig(3.0, 1.0).addConstraint(new CentripetalAccelerationConstraint(1.0))),

    new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3)
    );
  }
}

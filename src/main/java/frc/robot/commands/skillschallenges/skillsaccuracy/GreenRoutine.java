// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import java.util.List;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.hood.LowerHood;
import frc.robot.commands.hood.RaiseHood;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.skillschallenges.skillsaccuracy.GreenAutoFireQuantity;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
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
public class GreenRoutine extends SequentialCommandGroup {
  /** Creates a new ToBlueRoutine.*/
  public GreenRoutine(Intake intake, Magazine magazine, Shooter shooter, Hood hood) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelDeadlineGroup(
        new RaiseIntake(intake),
        new RaiseHood(hood)
        ),
    new GreenAutoFireQuantity(shooter, magazine, intake, 3),
    new LowerHood(hood)
    );
  }
}

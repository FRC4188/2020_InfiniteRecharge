// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.TurretToAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LTrenchSixBall extends CspSequentialCommandGroup {
  /** Creates a new LTrenchSixBall. */
  public LTrenchSixBall(Drivetrain drivetrain, Shooter shooter, Intake intake, Magazine magazine, Limelight limelight, Turret turret) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelDeadlineGroup(
        new TurretToAngle(turret, 208.0), 
        new LowerIntake(intake),
        new SpinShooter(shooter, 3100.0)),

    new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3),
    
    new ParallelDeadlineGroup(
        new FollowTrajectory(drivetrain, WaypointsList.LTrenchSixBall.DOWN_TRENCH, new TrajectoryConfig(1.25, 1.15)
        .addConstraint(new CentripetalAccelerationConstraint(1.5))),
        new AutoMagazine(magazine, intake, true, true)),


    new ParallelDeadlineGroup(
        new FollowTrajectory(drivetrain, WaypointsList.LTrenchSixBall.TO_SHOOT, new TrajectoryConfig(2.0, 2.25)
        .addConstraint(new CentripetalAccelerationConstraint(1.5))), 
        new AutoMagazine(magazine, intake, true, true),    
        new TurretToAngle(turret, 208.0),
        new SpinShooter(shooter, 3100.0)),

    new AutoMagazine(magazine, intake, true, false),

    new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3)
    );
  }
}

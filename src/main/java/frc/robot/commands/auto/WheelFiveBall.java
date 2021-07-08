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
import frc.robot.commands.intake.RaiseIntake;
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
public class WheelFiveBall extends CspSequentialCommandGroup {
  /** Creates a new WheelFiveBall. */
  public WheelFiveBall(Drivetrain drivetrain, Magazine magazine, Intake intake, Shooter shooter, Limelight limelight, Turret turret) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
        new FollowTrajectory(drivetrain, WaypointsList.WheelFiveBall.FIRST_PATH, 
          new TrajectoryConfig(3.0, 2.25)),
        new FollowTrajectory(drivetrain, WaypointsList.WheelFiveBall.SECOND_PATH, 
          new TrajectoryConfig(3.0, 2.0)),
        new FollowTrajectory(drivetrain, WaypointsList.WheelFiveBall.THIRD_PATH, 
          new TrajectoryConfig(3.0, 2.0)),
        new FollowTrajectory(drivetrain, WaypointsList.WheelFiveBall.FOURTH_PATH, 
          new TrajectoryConfig(4.0, 2.5).addConstraint(new CentripetalAccelerationConstraint(1.5)))),
        
        new LowerIntake(intake),
        new TurretToAngle(turret, 180),
        new AutoMagazine(magazine, intake, true, true),
        new SpinShooter(shooter, 3000)
        ),

      new RaiseIntake(intake),
      new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 5)
    );
  }
}

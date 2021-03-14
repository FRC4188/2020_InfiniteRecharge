// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.TurretAngle;
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
public class EightBall extends CspSequentialCommandGroup {
  /** Creates a new TestAuto. */
  public EightBall(Drivetrain drivetrain, Shooter shooter, Turret turret, Magazine magazine, Intake intake, Limelight limelight) {
    addCommands(
      new ParallelDeadlineGroup(
        new TurretAngle(turret, 180.0), 
        new SpinShooter(shooter, 3500.0),
        new LowerIntake(intake)
        ),

      new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3),

      new ParallelDeadlineGroup(
        new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.DOWN_TRENCH),
        new TurretAngle(turret, 0.0),
        new SpinShooter(shooter, 3500.0),
        new AutoMagazine(magazine, intake, true, true)
      ),

      new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.INTO_RENDEVOUS,
      new TrajectoryConfig(1.5, 1.0)
      .addConstraint(new CentripetalAccelerationConstraint(1.0))),

      new ParallelDeadlineGroup(
        new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.THROUGH_RENDEVOUS, drivetrain.getTrajectoryConfig().setEndVelocity(0.25)),
        new AutoMagazine(magazine, intake, true, true)
      ),

      new ParallelDeadlineGroup(
        new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.TO_SHOOT, drivetrain.getTrajectoryConfig().setStartVelocity(0.25)),
        new AutoMagazine(magazine, intake, true, false)
      ),

      new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 5),

      new SpinShooter(shooter, 3500.0)
    );
  }

  @Override
  public Pose2d getInitialPose() {
    return WaypointsList.TrenchEightBall.INITIAL_POSE;
  }
}

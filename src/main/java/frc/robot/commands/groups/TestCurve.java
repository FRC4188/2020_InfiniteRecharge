/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.groups;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

public class TestCurve extends CspSequentialCommandGroup {

Drivetrain drivetrain;

Trajectory curve;

  /**
   * Creates a new TestCurve.
   */
  public TestCurve(Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.drivetrain = drivetrain;

    String trajectory1JSON = "paths/output/CurveTest.wpilib.json";
        try {
        Path trajectory1Path = Filesystem.getDeployDirectory().toPath().resolve(trajectory1JSON);
        curve = TrajectoryUtil.fromPathweaverJson(trajectory1Path);
        } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectory: " + trajectory1JSON, ex.getStackTrace());
        }

        addCommands(
          new FollowTrajectory(drivetrain, curve /*WaypointsList.ONE_METER*/)
        );
  }
}

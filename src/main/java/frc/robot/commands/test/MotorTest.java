// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.test;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.TurretToAngle;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MotorTest extends SequentialCommandGroup {
  /** Creates a new MotorTest. */
  public MotorTest(Shooter shooter, Intake intake, Magazine magazine, Turret turret) {
    addCommands(
      new SpinShooter(shooter, 4000.0).withTimeout(2.5).andThen(new SpinShooter(shooter, 0.0)),

      new SpinJustIntake(intake, -0.5).withTimeout(0.75).andThen(new SpinJustIntake(intake, 0.0)),
      new SpinIndexer(intake, -0.5).withTimeout(0.75).andThen(new SpinIndexer(intake, 0.0)),
      new RunMagazine(magazine, 0.5).withTimeout(0.75).andThen(new RunMagazine(magazine, 0.0)),

      new SpinJustIntake(intake, 0.5).withTimeout(0.75).andThen(new SpinJustIntake(intake, 0.0)),
      new SpinIndexer(intake, 0.5).withTimeout(0.75).andThen(new SpinIndexer(intake, 0.0)),
      new RunMagazine(magazine, -0.5).withTimeout(0.75).andThen(new RunMagazine(magazine, 0.0)),

      new TurretToAngle(turret, 0.0),
      new TurretToAngle(turret, 180.0),
      new TurretToAngle(turret, 0.0)
      );
  }
}

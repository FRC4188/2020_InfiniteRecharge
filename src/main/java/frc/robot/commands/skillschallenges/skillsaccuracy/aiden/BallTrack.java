// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy.aiden;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class BallTrack extends CommandBase {

  private Intake intake;
  private Magazine magazine;
  private Limelight limelight;
  private Shooter shooter;
  private BooleanSupplier intaking;
  private BooleanSupplier indexing;
  private BooleanSupplier shooting;
  private BooleanSupplier reversed;

  /** Creates a new BallTrack. */
  public BallTrack(Intake intake, Magazine magazine, Shooter shooter, Limelight limelight,
  BooleanSupplier intaking, BooleanSupplier indexing, BooleanSupplier shooting, BooleanSupplier reversed) {
    addRequirements(intake, magazine);
    this.intake = intake;
    this.magazine = magazine;
    this.shooter = shooter;
    this.limelight = limelight;
    this.intaking = intaking;
    this.indexing = indexing;
    this.shooting = shooting;
    this.reversed = reversed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean take = intaking.getAsBoolean();
    boolean dex = indexing.getAsBoolean();
    boolean shoot = shooting.getAsBoolean();
    boolean rev = reversed.getAsBoolean();
    boolean ready = limelight.getIsAimed() && Math.abs(shooter.getRightVelocity() - 3100.0) < 50.0;

    double indexerPow = 0.0;
    double intakePow = 0.0;

    if (take && !rev) intakePow = -0.75;
    else if (take && rev) intakePow = 0.75;
    else intakePow = 0.0;

    if (dex && !rev) {
      if (magazine.topBeamClear() && magazine.midBeamClear()) {
        indexerPow = -1.0;
      }
      else indexerPow = 0.0;
    }
    else if (dex && rev) indexerPow = 1.0;
    else indexerPow = 0.0;

    if (shoot && !magazine.topBeamClear() && !rev) {
      if (ready) magazine.set(1.0);
      else magazine.set(0.0);
    } else if (shoot && !rev) magazine.set(0.5);
    else if (shoot && rev) magazine.set(-0.5);
    else magazine.set(0.0);

    intake.spin(intakePow, indexerPow);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.spin(0.0, 0.0);
    magazine.set(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

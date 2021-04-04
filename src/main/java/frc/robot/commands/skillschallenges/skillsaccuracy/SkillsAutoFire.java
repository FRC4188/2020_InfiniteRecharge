// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class SkillsAutoFire extends CommandBase {
  private Shooter shooter;
  private Magazine magazine;
  private Intake intake;
  private Limelight limelight;
  private Turret turret;
  private Hood hood;

  private boolean cont;
  private double setVelocity;

  private MedianFilter filter;

  /** Creates a new GreenAutoFireQuantity. */
  public SkillsAutoFire(Shooter shooter, Magazine magazine, Intake intake, Limelight limelight, Turret turret, Hood hood, boolean cont) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter, magazine, intake, limelight, turret, hood);
    this.shooter = shooter;
    this.intake = intake;
    this.magazine = magazine;
    this.turret = turret;
    this.limelight = limelight;
    this.hood = hood;
    this.cont = cont;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean isAimed = limelight.getIsAimed();
    boolean hasTarget = limelight.hasTarget();
    boolean isAroundSpeed = (Math.abs(shooter.getLeftVelocity() - setVelocity) <= 100);
    boolean isReady = isAimed && hasTarget && isAroundSpeed;
    boolean topIsClear = magazine.topBeamClear();
    boolean entryIsClear = magazine.entryBeamClear();

    shooter.setVelocity(3000);
    setVelocity = 3000;

    if (hasTarget) {
      turret.track(limelight.getHorizontalAngle() - 0.2);
    } else turret.set(0);    

    if (isReady) magazine.set(1.0);
    else magazine.set(0.0);

    if (isReady || (topIsClear && entryIsClear)) intake.spin(-0.5, -1.0);
    else intake.spin(-0.5, 0.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(!interrupted) {
      magazine.set(0);
      intake.spin(0, 0);
      shooter.setVelocity(3000);
      turret.set(0.0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !cont;
  }
}

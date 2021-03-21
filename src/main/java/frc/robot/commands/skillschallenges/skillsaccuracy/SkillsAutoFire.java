// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import edu.wpi.first.wpilibj.LinearFilter;
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
  private double lastVelocity;
  private double setVelocity;

  /** Creates a new GreenAutoFireQuantity. */
  public SkillsAutoFire(Shooter shooter, Magazine magazine, Intake intake, Limelight limelight, Turret turret, boolean cont) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter, magazine, intake);
    this.shooter = shooter;
    this.intake = intake;
    this.magazine = magazine;
    this.turret = turret;
    this.limelight = limelight;
    this.cont = cont;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean isAimed = limelight.getIsAimed();
    boolean hasTarget = limelight.hasTarget();
    boolean aroundSpeed = (Math.abs(shooter.getLeftVelocity() - setVelocity) <= 200);
    boolean isReady = isAimed && hasTarget && aroundSpeed;
    boolean topIsClear = magazine.topBeamClear();
    boolean entryIsClear = magazine.entryBeamClear();

    LinearFilter filter = LinearFilter.highPass(1, 3);

    double filteredDistance = filter.calculate(limelight.getDistance());

    boolean inGreen = (filteredDistance < 6.7 || filteredDistance > 24.0);
    boolean inYellow = (filteredDistance > 6.7 && filteredDistance < 8.5);
    boolean inBlue = (filteredDistance > 10.35 && filteredDistance < 11.5);
    boolean inRed = (filteredDistance > 14.5 && filteredDistance < 16.5);

    if (inGreen) {
      shooter.setVelocity(2000);
      setVelocity = 2000;
    } else if (inYellow) {
      shooter.setVelocity(3700);
      setVelocity = 3700;
    } else if (inBlue) {
      shooter.setVelocity(3100);
      setVelocity = 3100;
    } else if (inRed) {
      shooter.setVelocity(3300);
      setVelocity = 3300;
    }

    if (hasTarget) {
      turret.track(limelight.getHorizontalAngle());
    } else {
      turret.set(0);
    }    

    isReady = ((inGreen && !isAimed && !hasTarget && aroundSpeed)) ? true : isReady;

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
      shooter.set(2000.0);
      turret.set(0.0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !cont;
  }
}

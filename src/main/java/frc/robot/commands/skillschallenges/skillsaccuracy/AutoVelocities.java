// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class AutoVelocities extends CommandBase {
  Shooter shooter;
  Hood hood;
  Limelight limelight;
  Turret turret;
  boolean cont;

  MedianFilter filter;

  /** Creates a new AutoVelocities. */
  public AutoVelocities(Shooter shooter, Limelight limelight, Turret turret, Hood hood, boolean cont) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter, hood);
    this.shooter = shooter;
    this.hood = hood;
    this.limelight = limelight;
    this.turret = turret;
    this.cont = cont;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    filter = new MedianFilter(3);
    double filteredDistance = filter.calculate(limelight.getDistance());

    boolean hasTarget = limelight.hasTarget();

    boolean inGreen = (filteredDistance < 6.7 || filteredDistance > 24.0);
    boolean inYellow = (filteredDistance > 8 && filteredDistance < 9.5);
    boolean inBlue = (filteredDistance > 11.8 && filteredDistance < 12.9);
    boolean inRed = (filteredDistance > 15.1 && filteredDistance < 16.5);

    if (hasTarget) {
      turret.track(limelight.getHorizontalAngle());
      if (inRed) turret.track(limelight.getHorizontalAngle() - 1.0);
      else if (inYellow) turret.track(limelight.getHorizontalAngle() - 0.5);
      //else if (inBlue) turret.track(limelight.getHorizontalAngle() - 0.3);
    } else turret.set(0);    

    if (inGreen) {
      hood.raise();
      shooter.setVelocity(2000);
    } else if (inYellow) {
      shooter.setVelocity(2950);
    } else if (inBlue) {
      shooter.setVelocity(3100);
    } else if (inRed) {
      shooter.setVelocity(3350);
    }
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setVelocity(2000);
    turret.set(0.0);
    hood.lower();
    filter.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !cont;
  }
}

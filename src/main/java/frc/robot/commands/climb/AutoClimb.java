// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class AutoClimb extends CommandBase {
  Climber climber;

  boolean hasReached = false;
  /** Creates a new AutoClimb. */
  public AutoClimb(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climber);
    this.climber = climber;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (climber.getClimberBrake()) climber.engagePneuBrake(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if ((climber.getLeftPosition() > -200000 || climber.getRightPosition() > -200000) && !hasReached) {
      climber.set(-0.5);
      if (climber.getLeftPosition() == -200000 || climber.getRightPosition() == -200000) {
        hasReached = true;
        climber.set(0.0);
      }
    } else if ((climber.getRightPosition() < -5000 || climber.getLeftPosition() < 5000) && hasReached) {
      climber.set(0.5);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.set(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return climber.getLeftPosition() == 5000 || climber.getRightPosition() == 5000;
  }
}

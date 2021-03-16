// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.skillschallenges.skillsaccuracy;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class GreenAutoFireQuantity extends CommandBase {
  private Shooter shooter;
  private Magazine magazine;
  private Intake intake;

  private int quantity;

  private boolean lastTop = true;
  
  /** Creates a new GreenAutoFireQuantity. */
  public GreenAutoFireQuantity(Shooter shooter, Magazine magazine2, Intake intake, int quantity) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter, magazine2, intake);
    this.shooter = shooter;
    this.intake = intake;
    this.magazine = magazine2;
    this.quantity = quantity;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setVelocity(6000);
    boolean ready = true;
    boolean top = magazine.topBeamClear();
    boolean entry = magazine.entryBeamClear();

    if (ready) magazine.set(1.0);
    else if (top) magazine.set(0.25);
    else magazine.set(0.0);

    if (ready || (top && entry)) intake.spin(-0.5, -1.0);
    else intake.spin(-0.5, 0.0);

    if (!lastTop && top) quantity--;

    lastTop = top;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(!interrupted) {
      magazine.set(0);
      intake.spin(0,0);
      shooter.set(3500);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (quantity == 0);
  }
}

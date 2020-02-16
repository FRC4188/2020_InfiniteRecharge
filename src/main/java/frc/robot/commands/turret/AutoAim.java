package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class AutoAim extends CommandBase {

  Turret turret;
  Limelight limelight;

  /**
   * Creates a new AutoAim.
   */
  public AutoAim(Turret turret, Limelight limelight) {
    addRequirements(turret);
    this.turret = turret;
    this.limelight = limelight;
    limelight.trackTarget();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.set(-limelight.getHorizontalAngle()/47);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
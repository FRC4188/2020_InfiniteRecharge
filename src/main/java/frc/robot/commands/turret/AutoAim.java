package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class AutoAim extends CommandBase {

  Turret turret;
  Limelight limelight;
  private final double kP = 1.0 / 47;

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
    SmartDashboard.putNumber("Turret kP adjust", 0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.set(-limelight.getHorizontalAngle() * kP
        + SmartDashboard.getNumber("Turret kP adjust", 0.0) / 10.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (turret.getPosition() <= turret.getMinPosition() || turret.getPosition() >= turret.getMaxPosition());
  }
}
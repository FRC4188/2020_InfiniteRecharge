package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Magazine;

public class PIDTesting extends CommandBase {

  Magazine magazine;

  double P;
  double I;
  double D;

  /**
   * Creates a new PIDTesting.
   */
  public PIDTesting(Magazine magazine) {
    this.magazine = magazine;

    addRequirements(magazine);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    P = SmartDashboard.getNumber("Mag P", 4e-10);
    I = SmartDashboard.getNumber("Mag I", 1e-6);
    D = SmartDashboard.getNumber("Mag D", 0);

    magazine.testPIDConfig(P, I, D);

    magazine.setIncrease(5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}

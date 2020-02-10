package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ManualClimb extends CommandBase {
  Climber climber = new Climber();
  double percent;
  /**
   * Creates a new RaiseClimber.
   */
  public ManualClimb(double percent) {
    addRequirements(climber);
    this.percent = percent;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //use pilot Dpad up to raise the climber
    climber.setSpeedPercentage(percent);//could be modified. Is currently at 30% of the max falcon speed. This might be too fast. 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.setSpeedPercentage(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Solenoid;

public class Hood extends SubsystemBase {
  
  private Solenoid hoodSolenoid = new Solenoid(1);
  private Solenoid hoodSlave = new Solenoid(0);

  boolean hoodPos = false;

  public Hood() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setHood() {
    hoodSolenoid.set(hoodPos);
    hoodSlave.set(hoodPos);
    hoodPos = !hoodPos;
  }
}

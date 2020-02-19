package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hood extends SubsystemBase {
    
    private Solenoid hoodSolenoid = new Solenoid(1);

    public Hood() {
    }

    @Override
    public void periodic() {
    }

    public void setHood(boolean hoodPos) {
        hoodSolenoid.set(hoodPos);
    }
}

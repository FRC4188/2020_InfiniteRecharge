package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hood extends SubsystemBase {

    private Solenoid hoodSolenoid = new Solenoid(1);
    private boolean isRaised = false;

    public void periodic() {
        SmartDashboard.putBoolean("Hood Raised", isRaised());
    }

    /**
     * Sets hood to raised position.
     */
    public void raise() {
        hoodSolenoid.set(true);
        isRaised = true;
    }

    /**
     * Sets hood to lowered position.
     */
    public void lower() {
        hoodSolenoid.set(false);
        isRaised = false;
    }

    /**
     * Returns true if hood is in the raised position.
     */
    public boolean isRaised() {
        return isRaised;
    }

}
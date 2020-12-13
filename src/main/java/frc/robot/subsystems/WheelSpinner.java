package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WheelSpinner extends SubsystemBase {

    private Solenoid wheelSpinnerSolenoid = new Solenoid(4);
    private CANSparkMax wheelSpinnerMotor = new CANSparkMax(26, MotorType.kBrushless);
    private boolean isRaised = true;

    private double reduction = 1;

    /**
     * Runs every loop.
     */
    public void periodic() {
        //SmartDashboard.putBoolean("Wheel Spinner Raised", isRaised());
    }

    /**
     * Sets wheel spinner to a certain percent output.
     */
    public void setPercentage(double percent) {
        wheelSpinnerMotor.set(percent * reduction);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    /**
     * Sets wheel spinner to raised position.
     */
    public void raise() {
        wheelSpinnerSolenoid.set(false);
        isRaised = true;
    }

    /**
     * Sets wheel spinner to lowered position.
     */
    public void lower() {
        wheelSpinnerSolenoid.set(true);
        isRaised = false;
    }

    /**
     * Returns true if wheel spinner is in the raised position.
     */
    public boolean isRaised() {
        return isRaised;
    }

}
package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class SpinPolyRoller extends CommandBase {

    private final Intake intake;
    private final double percent;

    /**
     * Constructs new SpinPolyRoller command to spin poly rollers a given percentage.
     *
     * @param intake - Intake subsystem to use.
     * @param percent - percent output to command motors.
     */
    public SpinPolyRoller(Intake intake, double percent) {
        this.percent = percent;
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.spinPolyRollers(percent);
    }

    @Override
    public void end(boolean interrupted) {
        intake.spin(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

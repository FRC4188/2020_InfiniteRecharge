package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class LoadOne extends CommandBase {

    private final Magazine magazine;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param intake - Intake subsystem to use.
     */
    public LoadOne(Magazine magazine) {
        addRequirements(magazine);
        this.magazine = magazine;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if(magazine.topBeamClear()) {
            if (!magazine.midBeamClear()/* || magazine.botBeamClear()*/) {
                magazine.set(0.7);
            }
            else magazine.set(0);
        }
        else {
            magazine.set(0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }

}

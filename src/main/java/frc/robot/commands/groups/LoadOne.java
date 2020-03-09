package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class LoadOne extends CommandBase {

    private final Magazine magazine;
    private final Intake intake;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param intake - Intake subsystem to use.
     */
    public LoadOne(Magazine magazine, Intake intake) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        /*if (magazine.getCount() < 3) {
            if (!magazine.getBotBeam()) {
                magazine.set(0.8);
                timer++;
            }
        }*/
        if(magazine.getTopBeam()) {
            if (!magazine.getBotBeam()) {
                magazine.set(0.8);
                intake.spinIndexer(-0.1);
                intake.spinPolyRollers(0.0);
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
        magazine.setCount();
        magazine.set(0);
    }

}

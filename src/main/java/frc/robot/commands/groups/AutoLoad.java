package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;
import frc.robot.commands.groups.LoadOne;
import frc.robot.subsystems.Intake;

/**
 * Runs magazine motor at a given percentage.
 */
public class AutoLoad extends CommandBase {

    private final Magazine magazine;
    private final Intake intake;
    private final double DELTA_T = 0.02;
    private final double TIMEOUT = 5 / DELTA_T;
    private double timer;
    private double count;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param intake - Intake subsystem to use.
     */
    public AutoLoad(Magazine magazine, Intake intake) {
        addRequirements(magazine, intake);
        this.magazine = magazine;
        this.intake = intake;
    }

    @Override
    public void initialize() {
        timer = 0;
    }

    @Override
    public void execute() {
        count = magazine.getCount();
        if (magazine.getTopBeam()) {
            if (count < 3){
                if (magazine.getBotBeam()) {
                    intake.spinIndexer(0.6);
                    intake.spinPolyRollers(0.6);
                    magazine.set(0.1);
                    timer++;
                }
                else {
                    new LoadOne(magazine);
                    timer = 0;
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return count >= 3 || timer >= TIMEOUT || !magazine.getTopBeam();
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0);
        intake.spinIndexer(0);
        intake.spinPolyRollers(0);
    }

}

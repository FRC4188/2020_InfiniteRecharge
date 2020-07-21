package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class AutoShoot extends ParallelCommandGroup {

    /**
     * Runs AutoMagazine with SpinShooterFormula.
     */
    public AutoShoot(Magazine magazine, Intake intake, Limelight limelight, Shooter shooter, Turret turret) {
        addCommands(new AutoMagazine(magazine, intake, limelight, shooter, turret),
                new SpinShooterFormula(shooter, limelight));
    }

}
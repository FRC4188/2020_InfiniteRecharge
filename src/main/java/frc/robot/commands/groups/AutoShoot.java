package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class AutoShoot extends ParallelCommandGroup {

    public AutoShoot(Magazine magazine, Limelight limelight, Shooter shooter) {
        addCommands(new AutoMagazine(magazine, limelight, shooter),
                new SpinShooterFormula(shooter, limelight));
    }

}
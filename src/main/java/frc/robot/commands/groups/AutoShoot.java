package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.AutoCenterBay;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class AutoShoot extends SequentialCommandGroup{
    
    public AutoShoot(Shooter shooter, Limelight limelight, Magazine magazine, Drivetrain drivetrain){
        addCommands(new AutoCenterBay(drivetrain, limelight, 0.1), new Shoot(shooter, limelight, magazine));
    }
}
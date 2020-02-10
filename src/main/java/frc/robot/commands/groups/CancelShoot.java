package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.magazine.TurnBelt;
import frc.robot.commands.shooter.CancelShooter;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class CancelShoot extends ParallelCommandGroup{
    
    public CancelShoot(Shooter shooter, Limelight limelight, Magazine magazine){
        addCommands(new TurnBelt(magazine, 0), new CancelShooter(shooter));
    }
}
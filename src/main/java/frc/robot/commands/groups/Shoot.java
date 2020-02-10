package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.magazine.AutoBelt;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class Shoot extends ParallelCommandGroup{
    
    public Shoot(Shooter shooter, Limelight limelight, Magazine magazine){
        addCommands(new SpinShooter(shooter, limelight), new AutoBelt(magazine));
    }
}
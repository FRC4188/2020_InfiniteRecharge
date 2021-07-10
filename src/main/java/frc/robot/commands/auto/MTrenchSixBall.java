package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.TurretToAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

public class MTrenchSixBall extends CspSequentialCommandGroup {

    public MTrenchSixBall (Drivetrain drivetrain, Turret turret, Shooter shooter, Magazine magazine, Intake intake, Limelight limelight) {
        addCommands(
            new ParallelDeadlineGroup(
                new TurretToAngle(turret, 180), 
                new LowerIntake(intake),
                new SpinShooter(shooter, 3100)),

            new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3).withTimeout(5.0),
            
            new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.MTrenchSixBall.DOWN_TRENCH, new TrajectoryConfig(1.5, 1.625)
                    .addConstraint(new CentripetalAccelerationConstraint(1.325))),
                new AutoMagazine(magazine, intake, true, true)),


            new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.MTrenchSixBall.TO_SHOOT, new TrajectoryConfig(4.0, 2.25)
                    .addConstraint(new CentripetalAccelerationConstraint(1.5))), 
                new AutoMagazine(magazine, intake, true, true),    
                new TurretToAngle(turret, 180),
                new SpinShooter(shooter, 3100)),

            new AutoMagazine(magazine, intake, true, false),

            new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3)
            
            
        );
    }
}
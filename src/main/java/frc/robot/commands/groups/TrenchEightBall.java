package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

public class TrenchEightBall extends CspSequentialCommandGroup {

    public TrenchEightBall (Drivetrain drivetrain, Turret turret, Shooter shooter, Magazine magazine, Intake intake, Limelight limelight) {
        addCommands(
            new ParallelDeadlineGroup(
                new TurretAngle(turret, 180), 
                new SpinShooter(shooter, 4000),
                new LowerIntake(intake)),

            new AutoFireQuantity(shooter, magazine, intake, limelight, 3),
            
            new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.DOWN_TRENCH), 
                new AutoMagazine(magazine, intake, true, true),
                new SpinShooter(shooter, 3000)),
            new AutoMagazine(magazine, intake, true, false),

            new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.INTO_RENDEVOUS), 
                new TurretAngle(turret, 0),
                new SpinShooter(shooter, 3000)),
            
            new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.THROUGH_RENDEVOUS),
                new AutoMagazine(magazine, intake, true, true),
                new SpinShooter(shooter, 4000)),

            new AutoMagazine(magazine, intake, true, false),
            new AutoFireQuantity(shooter, magazine, intake, limelight, 5),
            new SpinShooter(shooter, 3500)
        );
    }
}
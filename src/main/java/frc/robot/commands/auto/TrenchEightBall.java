package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

public class TrenchEightBall extends CspSequentialCommandGroup {

    public TrenchEightBall (Drivetrain drivetrain, Turret turret, Shooter shooter, Magazine magazine, Intake intake, Limelight limelight) {
        addCommands(
            
            new ParallelDeadlineGroup(
                new TurretToAngle(turret, 175), 
                new SpinShooter(shooter, 3140),
                new LowerIntake(intake)),

            new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 5)
            
            /*new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.DOWN_TRENCH, new TrajectoryConfig(4.0, 2.0)
                    .addConstraint(new CentripetalAccelerationConstraint(1.5))),
                new AutoMagazine(magazine, intake, true, true)),

            new AutoMagazine(magazine, intake, true, false)*/

            /*new ParallelDeadlineGroup(
                new FollowTrajectory(drivetrain, WaypointsList.TrenchEightBall.END_RENDEVOUS, new TrajectoryConfig(2.75, 1.26)
                    .addConstraint(new CentripetalAccelerationConstraint(1.0))),
                new AutoMagazine(magazine, intake, true, true)),

            new AutoMagazine(magazine, intake, true, false),*/

            /*new ParallelDeadlineGroup(
                new TurretToAngle(turret, -29), 
                new RaiseIntake(intake),
                new SpinShooter(shooter, 3580)),
            
            new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 5)*/
        );
    }

    @Override
    public Pose2d getInitialPose() {
        return WaypointsList.TrenchEightBall.INITIAL_POSE;
    }
}
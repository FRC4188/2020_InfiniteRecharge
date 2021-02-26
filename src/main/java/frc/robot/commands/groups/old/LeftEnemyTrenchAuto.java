package frc.robot.commands.groups.old;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.shooter.AutoFire;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

/**
 * Class containing command group to run Left Enemy Trench auto.
 */
public class LeftEnemyTrenchAuto extends CspSequentialCommandGroup {

    /**
     * Constructs LeftEnemyTrenchAuto object and adds commands to group.
     */
    public LeftEnemyTrenchAuto(Drivetrain drivetrain, Magazine magazine, Shooter shooter,
            Limelight limelight, Turret turret, Intake intake) {

        addCommands(
                new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3),

                // Drives into the enemy trench, running intake at the same time.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.LEFT_TO_ENEMY_TRENCH),
                    new AutoMagazine(magazine, intake, true, true),
                    new TurretAngle(turret, 240)
                ),

                // Drives to a shooting position and revs up shooter to default rpm.
                // Spins intake slowly to make sure picked up balls don't fall out.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.ENEMY_TRENCH_TO_SHOOT),
                    new AutoMagazine(magazine, intake, true, true)
                ),

                // Auto aims, and revs up shooter to rpm.
                new ParallelRaceGroup(
                    new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 3)
                ),

                // Pick up two more balls from front of bar and turn turret.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.SHOOT_TO_BAR),
                    new AutoMagazine(magazine, intake, true, true)
                ),

                // Auto aims, and revs up shooter to rpm.
                new ParallelRaceGroup(
                    new AutoFireQuantity(shooter, turret, magazine, intake, limelight, 2)
                )
        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.LEFT_TO_ENEMY_TRENCH.getPoses().get(0);
    }

}

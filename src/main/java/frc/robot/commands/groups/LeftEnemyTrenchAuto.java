package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.SpinJustIntake;
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

                // Lowers intake.
                new LowerIntake(intake),

                // Drives into the enemy trench, running intake at the same time.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.LEFT_TO_ENEMY_TRENCH),
                    new SpinJustIntake(intake, 1.0),
                    new TurretAngle(turret, 240)
                ),

                // Drives to a shooting position and revs up shooter to default rpm.
                // Spins intake slowly to make sure picked up balls don't fall out.
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.ENEMY_TRENCH_TO_SHOOT),
                    new SpinShooter(shooter, 3500),
                    new SpinJustIntake(intake, 0.6)
                ),

                // Auto aims, and revs up shooter to rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, 0).withTimeout(0.5),
                    new SpinShooter(shooter, 3500)
                ),

                // Continues to auto aim and spin shooter to rpm.
                // Runs magazine and indexer to shoot at the port.
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3500),
                    new AutoAim(turret, limelight, 0),
                    new RunMagazine(magazine, 0.8).withTimeout(2.0),
                    new SpinIntake(intake, magazine, 1.0,1.0)
                ),

                // Pick up two more balls from front of bar and turn turret.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.SHOOT_TO_BAR),
                    new SpinIntake(intake, magazine, 1.0,1.0),
                    new TurretAngle(turret, 160.0)
                ),

                // Auto aims, and revs up shooter to rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, 0).withTimeout(0.5),
                    new SpinShooter(shooter, 3600)
                ),

                // Auto aim and spin shooter to rpm.
                // Runs magazine and indexer to shoot at the port.
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3600),
                    new AutoAim(turret, limelight, 0),
                    new RunMagazine(magazine, 0.8).withTimeout(5.0),
                    new SpinIntake(intake, magazine, 1.0,1.0)
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

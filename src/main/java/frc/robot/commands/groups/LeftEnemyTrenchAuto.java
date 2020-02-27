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
import frc.robot.commands.intake.SpinPolyRoller;
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
                    new SpinJustIntake(intake, 1),
                    new TurretAngle(turret, 240)
                ),

                /**
                 * Drives to a shooting position and revs up shooter to default rpm.
                 * Spins intake slowly to make sure picked up balls don't fall out.
                 */
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.ENEMY_TRENCH_TO_SHOOT),
                    new SpinShooter(shooter, 3600),
                    new SpinJustIntake(intake, 0.6)
                ),

                // Raises intake, auto aims, and revs up shooter to Limelight's formula rpm.
                new ParallelRaceGroup(
                    //new RaiseIntake(intake),
                    new AutoAim(turret, limelight, -3).withTimeout(0.5),
                    new SpinShooter(shooter, 3600)
                ),

                /**
                 * Continues to auto aim and spin shooter at Limelight's formula rpm.
                 * Runs magazine and indexer to shoot at the port.
                 */
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3600),
                    new AutoAim(turret, limelight, -3),
                    new RunMagazine(magazine, 0.9).withTimeout(4.5),
                    new SpinIndexer(intake, 0.5),
                    new SpinPolyRoller(intake, 0.9),
                    new SpinJustIntake(intake, 0.8)
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

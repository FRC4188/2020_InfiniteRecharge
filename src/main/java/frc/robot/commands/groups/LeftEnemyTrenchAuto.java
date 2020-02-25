package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.SpinPolyRoller;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.commands.turret.AutoAim;
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
                new LowerIntake(intake).withTimeout(0.15),

                // Drives into the enemy trench, running intake at the same time.
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.LEFT_TO_ENEMY_TRENCH),
                    new SpinIntake(intake, 1).withTimeout(2.5)
                ),

                /**
                 * Drives to a shooting position and revs up shooter to default rpm.
                 * Spins intake slowly to make sure picked up balls don't fall out.
                 */
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.ENEMY_TRENCH_TO_SHOOT),
                    new SpinShooter(shooter, 3000),
                    new SpinIntake(intake, 0.3).withTimeout(2.5)
                ),

                // Raises intake, auto aims, and revs up shooter to Limelight's formula rpm.
                new ParallelRaceGroup(
                    new RaiseIntake(intake),
                    new AutoAim(turret, limelight, -1.5).withTimeout(0.15),
                    new SpinShooterFormula(shooter, limelight)
                ),

                /**
                 * Continues to auto aim and spin shooter at Limelight's formula rpm.
                 * Runs magazine and indexer to shoot at the port.
                 */
                new ParallelRaceGroup(
                    new SpinShooterFormula(shooter, limelight),
                    new AutoAim(turret, limelight, -1.5),
                    new RunMagazine(magazine, 0.9).withTimeout(4.5),
                    new SpinIndexer(intake, 0.9),
                    new SpinPolyRoller(intake, 0.9)
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

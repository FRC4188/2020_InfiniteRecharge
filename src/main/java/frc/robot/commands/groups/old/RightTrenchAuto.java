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
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.AutoFireQuantity;
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
 * Class containing command group to run Right Trench auto.
 */
public class RightTrenchAuto extends CspSequentialCommandGroup {

    /**
     * Constructs RightTrenchAuto object and adds commands to group.
     */
    public RightTrenchAuto(Drivetrain drivetrain, Magazine magazine, Shooter shooter,
            Limelight limelight, Turret turret, Intake intake) {

        addCommands(

                // Turns turret around and revs up shooter to default rpm.
                new ParallelRaceGroup(
                    new TurretAngle(turret, 180.0),
                    new SpinShooter(shooter, 3000).withTimeout(2.0)
                ),

                // Auto aims toward the port and revs up shooter to 6000 rpm.
                new AutoFireQuantity(shooter, magazine, intake, limelight, 3),

                // Drives backward into the right-side trench, running intake at the same time.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.RIGHT_TO_BACK_TRENCH),
                    new SpinShooter(shooter, 3000.0),
                    new TurretAngle(turret, 180.0),
                    new AutoMagazine(magazine, intake, true, true)
                ),

                // Drives forward to the front of the trench, spin up to 3550.
                // Runs magazine and intake at the same time to keep balls from falling out.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.BACK_TO_FRONT_TRENCH),
                    new TurretAngle(turret, 180.0),
                    new SpinShooter(shooter, 3000)
                ),

                // Continues to auto aim and spin shooter at 3500 rpm.
                // Runs magazine and indexer to shoot balls picked up from trench.
                new AutoFireQuantity(shooter, magazine, intake, limelight, 3),

                // Pickup balls from bar, auto aims and revs up shooter to 3500 rpm.
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.FRONT_TRENCH_TO_BAR),
                    new SpinIntake(intake, magazine, 1.0,1.0),
                    new SpinShooter(shooter, 3550)
                )

        );

    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.RIGHT_TO_BACK_TRENCH.getPoses().get(0);
    }

}

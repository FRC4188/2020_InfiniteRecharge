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
import frc.robot.commands.magazine.RunMagazineNonstop;
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
                    new SpinShooter(shooter, 3400).withTimeout(1),
                    new TurretAngle(turret, 190.0)
                ),

                // Auto aims toward the port and revs up shooter to 6000 rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -3.25).withTimeout(0.5),
                    new SpinShooter(shooter, 3400)
                ),

                // Continues to auto aim and spin shooter at 6000 rpm.
                // Runs magazine to shoot pre-loaded balls.
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3400),
                    new AutoAim(turret, limelight, -3.25).withTimeout(1.5),
                    new RunMagazineNonstop(magazine, 0.8)
                ),

                // Lowers the intake.
                new LowerIntake(intake),

                // Drives backward into the right-side trench, running intake at the same time.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.RIGHT_TO_BACK_TRENCH),
                    new TurretAngle(turret, 180.0),
                    new SpinIntake(intake, magazine, 1.0)
                ),

                // Drives forward to the front of the trench.
                // Runs magazine and intake at the same time to keep balls from falling out.
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.BACK_TO_FRONT_TRENCH),
                    new SpinIntake(intake, magazine, 0.3),
                    new RunMagazine(magazine, 0.25),
                    new TurretAngle(turret, 180.0)
                ),

                // Pickup balls from bar, auto aims and revs up shooter to 3500 rpm.
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.FRONT_TRENCH_TO_BAR),
                    new SpinIntake(intake, magazine, 1.0),
                    new SpinShooter(shooter, 3500)
                ),

                new FollowTrajectory(drivetrain, WaypointsList.BAR_TO_SHOOT),

                // Continues to auto aim and spin shooter at 3500 rpm.
                // Runs magazine and indexer to shoot balls picked up from trench.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -2.0),
                    new SpinShooter(shooter, 3500),
                    new SpinJustIntake(intake, 1.0),
                    new SpinIndexer(intake, 1.0),
                    new SpinPolyRoller(intake, -1.0),
                    new RunMagazineNonstop(magazine, 0.8).withTimeout(4.5)
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

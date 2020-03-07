package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.intake.SpinPolyRoller;
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
                    new TurretAngle(turret, 195)
                ),

                // Auto aims toward the port and revs up shooter to 6000 rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -2.0).withTimeout(0.5),
                    new SpinShooter(shooter, 3400)
                ),

                /* 
                 * Continues to auto aim and spin shooter at 6000 rpm.
                 * Runs magazine to shoot pre-loaded balls. 
                 */
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3400),
                    new AutoAim(turret, limelight, -2.0),
                    new RunMagazineNonstop(magazine, 0.8).withTimeout(1.5)
                ),

                // Lowers the intake.
                new LowerIntake(intake),

                // Drives backward into the right-side trench, running intake at the same time.
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.RIGHT_TO_BACK_TRENCH),
                    new SpinJustIntake(intake, 1),
                    new SpinPolyRoller(intake, 1),
                    new SpinIndexer(intake, 1)
                ),

                /**
                 * Drives forward to the front of the trench.
                 * Runs magazine and intake at the same time to keep balls from falling out.
                 */
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.BACK_TO_FRONT_TRENCH),
                    new SpinJustIntake(intake, 1),
                    new SpinPolyRoller(intake, 1),
                    new SpinIndexer(intake, 1),
                    new TurretAngle(turret, 167.0)
                ),

                // Auto aims and revs up shooter to 3500 rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -2.0).withTimeout(0.25),
                    new SpinShooter(shooter, 3400)
                ),

                /**
                 * Continues to auto aim and spin shooter at 3500 rpm.
                 * Runs magazine and indexer to shoot balls picked up from trench.
                 * Raises intake.
                 */
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -2.0),
                    new SpinShooter(shooter, 3400),
                    new SpinIndexer(intake, 0.8),
                    new SpinPolyRoller(intake, 0.8),
                    new RunMagazineNonstop(magazine, 0.8).withTimeout(4.5)
                ),

                new RaiseIntake(intake)

                /*
                // May add this to take the robot to the bar in the future.
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.FRONT_TRENCH_TO_BAR)
                )*/

        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.RIGHT_TO_BACK_TRENCH.getPoses().get(0);
    }

}

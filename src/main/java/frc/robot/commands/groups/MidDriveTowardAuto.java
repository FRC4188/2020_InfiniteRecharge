package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

/**
 * Class containing command group to run Mid Drive Toward auto.
 */
public class MidDriveTowardAuto extends CspSequentialCommandGroup {

    /**
     * Constructs MidDriveTowardAuto object and adds commands to group.
     */
    public MidDriveTowardAuto(Drivetrain drivetrain, Magazine magazine, Shooter shooter,
            Limelight limelight, Turret turret) {

        addCommands(

                // Turns turret around and revs up shooter to default rpm.
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3000),
                    new TurretAngle(turret, 180)
                ),

                // Auto aims toward the port and revs up shooter to 6000 rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -1.5).withTimeout(0.15),
                    new SpinShooter(shooter, 6000)
                ),

                // Continues to auto aim and spin shooter at 6000 rpm.
                // Runs magazine to shoot pre-loaded balls.
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 6000),
                    new AutoAim(turret, limelight, -1.5),
                    new RunMagazine(magazine, 0.8).withTimeout(1.5)
                ),

                // Drives toward the port to move out of the way for other robots' paths.
                new FollowTrajectory(drivetrain, WaypointsList.MID_DRIVE_TOWARD)

        );

    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.MID_DRIVE_TOWARD.getPoses().get(0);
    }

}

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
 * Class containing command group to run Mid Drive Away auto.
 */
public class MidDriveAwayAuto extends CspSequentialCommandGroup {

    /**
     * Constructs MidDriveAwayAuto object and adds commands to group.
     */
    public MidDriveAwayAuto(Drivetrain drivetrain, Magazine magazine, Shooter shooter,
            Limelight limelight, Turret turret) {
        addCommands(
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 3000),
                    new TurretAngle(turret, 180)
                ),
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -1.5).withTimeout(0.15),
                    new SpinShooter(shooter, 6000)
                ),
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 6000),
                    new AutoAim(turret, limelight, -1.5),
                    new RunMagazine(magazine, 0.8).withTimeout(1.5)
                ),
                new FollowTrajectory(drivetrain, WaypointsList.MID_DRIVE_AWAY)
        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.MID_DRIVE_AWAY.getPoses().get(0);
    }

}

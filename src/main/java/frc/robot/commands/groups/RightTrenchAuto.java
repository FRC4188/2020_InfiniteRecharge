package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.TurretToAngle;
import frc.robot.subsystems.Drivetrain;
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
            Limelight limelight, Turret turret) {
        addCommands(
                new ParallelCommandGroup(
                        new SpinShooter(shooter, 3000),
                        new TurretToAngle(turret, 180.0)
                ),
                new SpinShooterFormula(shooter, limelight),
                new AutoAim(turret, limelight),
                new RunMagazine(magazine, 1.0).withTimeout(1.5),
                new FollowTrajectory(drivetrain, WaypointsList.RIGHT_TO_BACK_TRENCH),
                new FollowTrajectory(drivetrain, WaypointsList.BACK_TO_FRONT_TRENCH),
                new FollowTrajectory(drivetrain, WaypointsList.FRONT_TRENCH_TO_BAR)
        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.RIGHT_TO_BACK_TRENCH.getPoses().get(0);
    }

}

package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
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
 * Class containing command group to run Mid Drive Toward auto.
 */
public class MidToLeftBarAuto extends CspSequentialCommandGroup {

    /**
     * Constructs MidToTrenchAuto object and adds commands to group.
     */
    public MidToLeftBarAuto(Drivetrain drivetrain, Magazine magazine, Shooter shooter,
            Limelight limelight, Turret turret, Intake intake) {
        addCommands(
                // Turns turret around and revs up shooter to default rpm.
                /*new ParallelRaceGroup(
                    new SpinShooter(shooter, 3000),
                    new TurretAngle(turret, 180)
                ),

                // Auto aims toward the port and revs up shooter to 6000 rpm.
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -1.5).withTimeout(0.15),
                    new SpinShooter(shooter, 6000)
                ),

                /* 
                 * Continues to auto aim and spin shooter at 6000 rpm.
                 * Runs magazine to shoot pre-loaded bal
                 * ls. 
                 */
                /*new ParallelRaceGroup(
                    new SpinShooter(shooter, 6000),
                    new AutoAim(turret, limelight, -1.5),
                    new RunMagazine(magazine, 0.8).withTimeout(1.5)
                )*/

                new LowerIntake(intake),
                
                new ParallelDeadlineGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.MID_TO_LEFT_BAR), 
                    new TurretAngle(turret, 165),
                    new SpinIntake(intake, magazine, 1.0, 0.2, -0.9),
                    new SpinShooter(shooter, 3500)
                ),

                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -3.5).withTimeout(0.6),
                    new SpinShooter(shooter, 3500)
                ),

                new ParallelRaceGroup(
                    new AutoAim(turret, limelight, -3.5),
                    new SpinShooter(shooter, 3500),
                    new SpinIntake(intake, magazine, 0.9, 0.9, 0.9),
                    new RunMagazineNonstop(magazine, 0.8).withTimeout(5)
                )

        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.MID_TO_LEFT_BAR.getPoses().get(0);
    }

}

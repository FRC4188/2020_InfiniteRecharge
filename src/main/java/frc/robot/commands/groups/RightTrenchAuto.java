package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.TurretToAngle;
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
                new ParallelRaceGroup(
                        new SpinShooter(shooter, 3000),
                        new TurretToAngle(turret, 180.0)
                ),
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight).withTimeout(0.5),
                    new SpinShooter(shooter, 6000)
                ),
                new ParallelRaceGroup(
                    new SpinShooter(shooter, 6000),
                    new AutoAim(turret, limelight),
                    new RunMagazine(magazine, 0.8).withTimeout(2.5),
                    new SpinIndexer(intake, 0.8)
                ),
                new LowerIntake(intake).withTimeout(0.3),
                new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.RIGHT_TO_BACK_TRENCH),
                    new SpinIntake(intake, 1)
                ),
                new FollowTrajectory(drivetrain, WaypointsList.BACK_TO_FRONT_TRENCH),
                new ParallelRaceGroup(
                    new AutoAim(turret, limelight).withTimeout(0.5),
                    new SpinShooterFormula(shooter, limelight)
                ),
                new ParallelRaceGroup(
                    new RaiseIntake(intake),
                    new SpinShooterFormula(shooter, limelight),
                    new SpinIndexer(intake, 0.8),
                    new RunMagazine(magazine, 0.8).withTimeout(2.5)
                )
                /*new ParallelRaceGroup(
                    new FollowTrajectory(drivetrain, WaypointsList.FRONT_TRENCH_TO_BAR),
                    new RaiseIntake(intake)
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

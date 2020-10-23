package frc.robot.commands.groups;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.SpinJustIntake;
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

/**
 * Class containing command group to run Mid Drive Toward auto.
 */
public class PathWeaverTest extends CspSequentialCommandGroup {

    Drivetrain drivetrain;
    Magazine magazine;
    Shooter shooter;
    Limelight limelight;
    Turret turret;

    Trajectory trajectory1;
    Trajectory trajectory2;
    Trajectory trajectory3;

    /**
     * Constructs MidDriveTowardAuto object and adds commands to group.
     */
    public PathWeaverTest(Drivetrain drivetrain, Magazine magazine, Shooter shooter, Limelight limelight, Turret turret, Intake intake) {
        this.drivetrain = drivetrain;
        this.magazine = magazine;
        this.shooter = shooter;
        this.limelight = limelight;
        this.turret = turret;

        String trajectory1JSON = "paths/output/Test1.wpilib.json";
        try {
        Path trajectory1Path = Filesystem.getDeployDirectory().toPath().resolve(trajectory1JSON);
        trajectory1 = TrajectoryUtil.fromPathweaverJson(trajectory1Path);
        } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectory: " + trajectory1JSON, ex.getStackTrace());
        }

        String trajectory2JSON = "paths/output/Test2.wpilib.json";
        try {
        Path trajectory2Path = Filesystem.getDeployDirectory().toPath().resolve(trajectory2JSON);
        trajectory2 = TrajectoryUtil.fromPathweaverJson(trajectory2Path);
        } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectory: " + trajectory1JSON, ex.getStackTrace());
        }

        String trajectory3JSON = "paths/output/Test3.wpilib.json";
        try {
        Path trajectory3Path = Filesystem.getDeployDirectory().toPath().resolve(trajectory3JSON);
        trajectory3 = TrajectoryUtil.fromPathweaverJson(trajectory3Path);
        } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectory: " + trajectory3JSON, ex.getStackTrace());
        }

        addCommands(
                new FollowTrajectory(drivetrain, trajectory1),
                new FollowTrajectory(drivetrain, trajectory2),
                new FollowTrajectory(drivetrain, trajectory3)
        );
    }
}

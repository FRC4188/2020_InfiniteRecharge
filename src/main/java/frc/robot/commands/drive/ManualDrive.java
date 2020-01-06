package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;

/**
 * Manually controls drivetrain using pilot controller.
 */
public class ManualDrive extends CommandBase {

    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Drivetrain drivetrain;

    public ManualDrive(Drivetrain d) {
        drivetrain = d;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double xSpeed = RobotContainer.getInstance().getPilotY(Hand.kLeft);
        double zRotation = RobotContainer.getInstance().getPilotX(Hand.kRight);
        drivetrain.arcade(xSpeed, zRotation);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
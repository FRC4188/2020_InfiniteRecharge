package frc.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.commands.drive.PureSet;
import frc.robot.subsystems.Drivetrain;

/**
 * The VM is configured to automatically run this class, and to call the functions
 * corresponding to each mode, as described in the TimedRobot documentation.
 */
public class Robot extends TimedRobot {

    private Command autonomousCommand;
    private RobotContainer robotContainer;

    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();
        // Creates UsbCamera and MjpegServer [1] and connects them
        CameraServer.getInstance().startAutomaticCapture();

        // Creates the CvSink and connects it to the UsbCamera
        CvSink cvSink = CameraServer.getInstance().getVideo();

        // Creates the CvSource and MjpegServer [2] and connects them
        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        robotContainer.getTempManager().run();


    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutoCommand();
        robotContainer.resetSubsystems();
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
        Drivetrain drivetrain = new Drivetrain();
        drivetrain.setCoast();
    }

    @Override
    public void testPeriodic() {
    }

}

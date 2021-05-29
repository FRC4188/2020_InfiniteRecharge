package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.robot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation.
 */
public class Robot extends TimedRobot {

    private Command autonomousCommand;
    private RobotContainer robotContainer;

    private PowerDistributionPanel pdp = new PowerDistributionPanel();

    @Override
    public void robotInit() {
        try {
            robotContainer = new RobotContainer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LiveWindow.disableAllTelemetry();

        Notifier logData = new Notifier(() -> updateShuffle());
        logData.startPeriodic(0.1);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        robotContainer.getTempManager().run();
        
        //robotContainer.getBrownoutProtection().run();
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
        robotContainer.getTestCommand().schedule();
    }

    @Override
    public void testPeriodic() {
    }

    private void updateShuffle() {
        SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());
        SmartDashboard.putNumber("Input Voltage", RobotController.getInputVoltage());
        SmartDashboard.putNumber("Channel 1", pdp.getCurrent(0));
        SmartDashboard.putNumber("Channel 2", pdp.getCurrent(1));
        SmartDashboard.putNumber("Channel 3", pdp.getCurrent(2));
        SmartDashboard.putNumber("Channel 4", pdp.getCurrent(3));
        SmartDashboard.putNumber("Channel 5", pdp.getCurrent(4));
        SmartDashboard.putNumber("Channel 6", pdp.getCurrent(5));
        SmartDashboard.putNumber("Channel 7", pdp.getCurrent(6));
        SmartDashboard.putNumber("Channel 8", pdp.getCurrent(7));
        SmartDashboard.putNumber("Channel 9", pdp.getCurrent(8));
        SmartDashboard.putNumber("Channel 10", pdp.getCurrent(9));
        SmartDashboard.putNumber("Channel 11", pdp.getCurrent(10));
        SmartDashboard.putNumber("Channel 12", pdp.getCurrent(11));
        SmartDashboard.putNumber("Channel 13", pdp.getCurrent(12));
        SmartDashboard.putNumber("Channel 14", pdp.getCurrent(13));
        SmartDashboard.putNumber("Channel 15", pdp.getCurrent(14));
        SmartDashboard.putNumber("Channel 16", pdp.getCurrent(15));
    }
}

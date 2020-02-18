package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Climber;
import frc.robot.utils.TempManager;

/**
 * The VM is configured to automatically run this class, and to call the functions
 * corresponding to each mode, as described in the TimedRobot documentation.
 */
public class Robot extends TimedRobot {

    private Command autonomousCommand;
    private RobotContainer robotContainer;
    private final PowerDistributionPanel pdp = new PowerDistributionPanel();
    private Compressor pcm = new Compressor();
    private TempManager tempManager = new TempManager();
    // private Climber climber = new Climber();

    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();
        pdp.clearStickyFaults();
        pcm.clearAllPCMStickyFaults();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        //tempManager.run();
        //SmartDashboard.putNumber("Climber Left pos", climber.getLeftPosition());
        //SmartDashboard.putNumber("Climber Right pos", climber.getRightPosition());

    }

    @Override
    public void disabledInit() {
        robotContainer.resetSubsystems();
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = null;
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
    }

    @Override
    public void testPeriodic() {
    }
}

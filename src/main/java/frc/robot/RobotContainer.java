package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.subsystems.Drivetrain;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();

    // controller initialization
    private final XboxController pilot = new XboxController(0);
    private final XboxController copilot = new XboxController(1);

    // button mappings for logitech controller
    private final class Controller {
        static final int A = 1;
        static final int B = 2;
        static final int X = 3;
        static final int Y = 4;
        static final int LB = 5;
        static final int RB = 6;
        static final int BACK = 7;
        static final int START = 8;
        static final int LS = 9;
        static final int RS = 10;
        static final int DPAD_UP = 0;
        static final int DPAD_RIGHT = 90;
        static final int DPAD_DOWN = 180;
        static final int DPAD_LEFT = 270;
        static final double DEADBAND = 0.08;
    }

    // button initialization
    private JoystickButton pilotA = new JoystickButton(pilot, Controller.A);
    private JoystickButton pilotB = new JoystickButton(pilot, Controller.B);
    private JoystickButton pilotX = new JoystickButton(pilot, Controller.X);
    private JoystickButton pilotY = new JoystickButton(pilot, Controller.Y);
    private JoystickButton pilotLb = new JoystickButton(pilot, Controller.LB);
    private JoystickButton pilotRb = new JoystickButton(pilot, Controller.RB);
    private JoystickButton pilotBack = new JoystickButton(pilot, Controller.BACK);
    private JoystickButton pilotStart = new JoystickButton(pilot, Controller.START);
    private JoystickButton pilotLS = new JoystickButton(pilot, Controller.LS);
    private JoystickButton pilotRS = new JoystickButton(pilot, Controller.RS);
    private POVButton pilotDpadUp = new POVButton(pilot, Controller.DPAD_UP);
    private POVButton pilotDpadRight = new POVButton(pilot, Controller.DPAD_RIGHT);
    private POVButton pilotDpadDown = new POVButton(pilot, Controller.DPAD_DOWN);
    private POVButton pilotDpadLeft = new POVButton(pilot, Controller.DPAD_LEFT);
    private JoystickButton copilotA = new JoystickButton(copilot, Controller.A);
    private JoystickButton copilotB = new JoystickButton(copilot, Controller.B);
    private JoystickButton copilotX = new JoystickButton(copilot, Controller.X);
    private JoystickButton copilotY = new JoystickButton(copilot, Controller.Y);
    private JoystickButton copilotLb = new JoystickButton(copilot, Controller.LB);
    private JoystickButton copilotRb = new JoystickButton(copilot, Controller.RB);
    private JoystickButton copilotBack = new JoystickButton(copilot, Controller.BACK);
    private JoystickButton copilotStart = new JoystickButton(copilot, Controller.START);
    private JoystickButton copilotLS = new JoystickButton(copilot, Controller.LS);
    private JoystickButton copilotRS = new JoystickButton(copilot, Controller.RS);
    private POVButton copilotDpadUp = new POVButton(copilot, Controller.DPAD_UP);
    private POVButton copilotDpadRight = new POVButton(copilot, Controller.DPAD_RIGHT);
    private POVButton copilotDpadDown = new POVButton(copilot, Controller.DPAD_DOWN);
    private POVButton copilotDpadLeft = new POVButton(copilot, Controller.DPAD_LEFT);

    /**
     * Initializes robot subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        setDefaultCommands();
        configureButtonBindings();
    }

    /**
     * Sets the default command for each subsystem, if applicable.
     */
    private void setDefaultCommands() {
        drivetrain.setDefaultCommand(new ManualDrive(drivetrain,
                () -> getPilotY(Hand.kLeft),
                () -> getPilotX(Hand.kRight)));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
    }

    /**
     * Returns y axis of Joystick on pilot controller.
     */
    public double getPilotY(Hand hand) {
        if (Math.abs(pilot.getY(hand)) < Controller.DEADBAND) return 0;
        else return -pilot.getY(hand);
    }

    /**
     * Returns x axis of Joystick on pilot controller.
     */
    public double getPilotX(Hand hand) {
        if (Math.abs(pilot.getX(hand)) < Controller.DEADBAND) return 0;
        else return pilot.getX(hand);
    }

    /**
     * Returns trigger axis on pilot controller.
     */
    public double getPilotTrigger(Hand hand) {
        return pilot.getTriggerAxis(hand);
    }

    /**
     * Returns y axis of Joystick on copilot controller.
     */
    public double getCopilotY(Hand hand) {
        if (Math.abs(copilot.getY(hand)) < Controller.DEADBAND) return 0;
        else return -copilot.getY(hand);
    }

    /**
     * Returns x axis of Joystick on copilot controller.
     */
    public double getCopilotX(Hand hand) {
        if (Math.abs(copilot.getX(hand)) < Controller.DEADBAND) return 0;
        else return copilot.getX(hand);
    }

    /**
     * Returns trigger axis on copilot controller.
     */
    public double getCopilotTrigger(Hand hand) {
        return copilot.getTriggerAxis(hand);
    }

}

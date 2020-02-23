package frc.robot.utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * Subclass of XboxController to handle joystick scaling, deadbands, and button initialization.
 */
public class ButtonBox extends Joystick {

    public ButtonBox(int port) {
        super(port);
        // TODO Auto-generated constructor stub
    }

    private static final double DEADBAND = 0.1;

    /**
     * Class containing button mappings for Logitech F310.
     */
    private final class Controller {
        static final int button1 = 1;
        static final int button2 = 2;
        static final int button3 = 3;
        static final int button4 = 4;
        static final int button5 = 5;
        static final int button6 = 6;
        static final int button7 = 7;
    }

    // button initialization
    private final JoystickButton button1 = new JoystickButton(this, Controller.button1);
    private final JoystickButton button2 = new JoystickButton(this, Controller.button2);
    private final JoystickButton button3 = new JoystickButton(this, Controller.button3);
    private final JoystickButton button4 = new JoystickButton(this, Controller.button4);
    private final JoystickButton button5 = new JoystickButton(this, Controller.button5);
    private final JoystickButton button6 = new JoystickButton(this, Controller.button6);
    private final JoystickButton button7 = new JoystickButton(this, Controller.button7);

    /**
     * Returns the JoystickButton object for the A button.
     */
    public JoystickButton getButton1Obj() {
        return button1;
    }

    /**
     * Returns the JoystickButton object for the B button.
     */
    public JoystickButton getButton2Obj() {
        return button2;
    }

    /**
     * Returns the JoystickButton object for the X button.
     */
    public JoystickButton getButton3Obj() {
        return button3;
    }

    /**
     * Returns the JoystickButton object for the Y button.
     */
    public JoystickButton getButton4Obj() {
        return button4;
    }

    /**
     * Returns the JoystickButton object for the left bumper button.
     */
    public JoystickButton getButton5Obj() {
        return button5;
    }

    /**
     * Returns the JoystickButton object for the right bumper button.
     */
    public JoystickButton getButton6Obj() {
        return button6;
    }

    /**
     * Returns the JoystickButton object for the back button.
     */
    public JoystickButton getButton7Obj() {
        return button7;
    }

}

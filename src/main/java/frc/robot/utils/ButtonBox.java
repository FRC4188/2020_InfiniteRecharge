package frc.robot.utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * Subclass of XboxController to handle joystick scaling, deadbands, and button initialization.
 */
public class ButtonBox extends Joystick {

    public ButtonBox(int port) {
        super(port);
    }

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
        static final int button8 = 8;
        static final int button9 = 9;
    }

    // button initialization
    private final JoystickButton button1 = new JoystickButton(this, Controller.button1);
    private final JoystickButton button2 = new JoystickButton(this, Controller.button2);
    private final JoystickButton button3 = new JoystickButton(this, Controller.button3);
    private final JoystickButton button4 = new JoystickButton(this, Controller.button4);
    private final JoystickButton button5 = new JoystickButton(this, Controller.button5);
    private final JoystickButton button6 = new JoystickButton(this, Controller.button6);
    private final JoystickButton button7 = new JoystickButton(this, Controller.button7);
    private final JoystickButton button8 = new JoystickButton(this, Controller.button8);
    private final JoystickButton button9 = new JoystickButton(this, Controller.button9);

    /**
     * Returns the JoystickButton object for the first button.
     */
    public JoystickButton getButton1Obj() {
        return button1;
    }

    /**
     * Returns the JoystickButton object for the second button.
     */
    public JoystickButton getButton2Obj() {
        return button2;
    }

    /**
     * Returns the JoystickButton object for the third button.
     */
    public JoystickButton getButton3Obj() {
        return button3;
    }

    /**
     * Returns the JoystickButton object for the fourth button.
     */
    public JoystickButton getButton4Obj() {
        return button4;
    }

    /**
     * Returns the JoystickButton object for the fifth button.
     */
    public JoystickButton getButton5Obj() {
        return button5;
    }

    /**
     * Returns the JoystickButton object for the sixth button.
     */
    public JoystickButton getButton6Obj() {
        return button6;
    }

    /**
     * Returns the JoystickButton object for the seventh button.
     */
    public JoystickButton getButton7Obj() {
        return button7;
    }

    /**
     * Returns the JoystickButton object for the eighth button.
     */
    public JoystickButton getButton8Obj() {
        return button8;
    }

    /**
     * Returns the JoystickButton object for the ninth button.
     */
    public JoystickButton getButton9Obj() {
        return button9;
    }

}

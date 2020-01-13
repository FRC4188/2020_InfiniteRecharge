package frc.robot.utils;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * Subclass of XboxController to handle joystick scaling, deadbands, and button initialization.
 */
public class CspController extends XboxController {

    private static final double DEADBAND = 0.08;

    /**
     * Class containing button mappings for Logitech F310.
     */
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
    }

    // button initialization
    private final JoystickButton aButton = new JoystickButton(this, Controller.A);
    private final JoystickButton bButton = new JoystickButton(this, Controller.B);
    private final JoystickButton xButton = new JoystickButton(this, Controller.X);
    private final JoystickButton yButton = new JoystickButton(this, Controller.Y);
    private final JoystickButton leftBumperButton = new JoystickButton(this, Controller.LB);
    private final JoystickButton rightBumperButton = new JoystickButton(this, Controller.RB);
    private final JoystickButton backButton = new JoystickButton(this, Controller.BACK);
    private final JoystickButton startButton = new JoystickButton(this, Controller.START);
    private final JoystickButton leftStickButton = new JoystickButton(this, Controller.LS);
    private final JoystickButton rightStickButton = new JoystickButton(this, Controller.RS);
    private final POVButton dpadUpButton = new POVButton(this, Controller.DPAD_UP);
    private final POVButton dpadRightButton = new POVButton(this, Controller.DPAD_RIGHT);
    private final POVButton dpadDownButton = new POVButton(this, Controller.DPAD_DOWN);
    private final POVButton dpadLeftButton = new POVButton(this, Controller.DPAD_LEFT);

    /**
     * Options to scale joystick input.
     */
    public enum Scaling {
        LINEAR,
        SQUARED,
        CUBED
    }

    /**
     * Constructs an instance of CspController on the specified port.
     */
    public CspController(int port) {
        super(port);
    }

    /**
     * Converts joystick input to output based on scaling and deadband.
     */
    private double getOutput(double input, Scaling scale) {
        if (Math.abs(input) > DEADBAND) {
            if (scale == Scaling.SQUARED) return Math.signum(input) * Math.pow(input, 2);
            else if (scale == Scaling.CUBED) return Math.pow(input, 3);
            else return input;
        } else {
            return input;
        }
    }

    /**
     * Returns the Y value of the joystick for a given hand.
     */
    @Override
    public double getY(Hand hand) {
        return -1 * getOutput(super.getY(hand), Scaling.SQUARED);
    }

    /**
     * Returns the Y value of the joystick for a given hand and scaling.
     */
    public double getY(Hand hand, Scaling scale) {
        return -1 * getOutput(super.getY(hand), scale);
    }

    /**
     * Returns the Y value of the joystick for a given hand.
     */
    @Override
    public double getX(Hand hand) {
        return getOutput(super.getX(hand), Scaling.SQUARED);
    }

    /**
     * Returns the Y value of the joystick for a given hand and scaling.
     */
    public double getX(Hand hand, Scaling scale) {
        return getOutput(super.getX(hand), scale);
    }


    /**
     * Returns the JoystickButton object for the A button.
     */
    public JoystickButton getAButtonObj() {
        return aButton;
    }

    /**
     * Returns the JoystickButton object for the B button.
     */
    public JoystickButton getBButtonObj() {
        return bButton;
    }

    /**
     * Returns the JoystickButton object for the X button.
     */
    public JoystickButton getXButtonObj() {
        return xButton;
    }

    /**
     * Returns the JoystickButton object for the Y button.
     */
    public JoystickButton getYButtonObj() {
        return yButton;
    }

    /**
     * Returns the JoystickButton object for the left bumper button.
     */
    public JoystickButton getLbButtonObj() {
        return leftBumperButton;
    }

    /**
     * Returns the JoystickButton object for the right bumper button.
     */
    public JoystickButton getRbButtonObj() {
        return rightBumperButton;
    }

    /**
     * Returns the JoystickButton object for the back button.
     */
    public JoystickButton getBackButtonObj() {
        return backButton;
    }

    /**
     * Returns the JoystickButton object for the start button.
     */
    public JoystickButton getStartButtonObj() {
        return startButton;
    }

    /**
     * Returns the JoystickButton object for the left stick button.
     */
    public JoystickButton getLsButtonObj() {
        return leftStickButton;
    }

    /**
     * Returns the JoystickButton object for the right stick button.
     */
    public JoystickButton getRsButtonObj() {
        return rightStickButton;
    }

    /**
     * Returns the POVButton object for the D-pad up button.
     */
    public POVButton getDpadUpButtonObj() {
        return dpadUpButton;
    }

    /**
     * Returns the POVButton object for the D-pad right button.
     */
    public POVButton getDpadRightButtonObj() {
        return dpadRightButton;
    }

    /**
     * Returns the POVButton object for the D-pad down button.
     */
    public POVButton getDpadDownButtonObj() {
        return dpadDownButton;
    }

    /**
     * Returns the POVButton object for the D-pad left button.
     */
    public POVButton getDpadLeftButtonObj() {
        return dpadLeftButton;
    }

}
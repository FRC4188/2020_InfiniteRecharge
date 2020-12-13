package frc.robot.utils;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Subclass of XboxController to handle joystick scaling, deadbands, and button initialization.
 */
public class CspController extends XboxController {

    private static final double DEADBAND = 0.15;
    private static final double TRIGGER_THRESHOLD = 0.6;

    private boolean EmergencyPower;

    /**
     * Class containing button mappings for Logitech F310.
     */
    private final class F310 {
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
            if (EmergencyPower) return input; // Hope this does not backfire...
            if (scale == Scaling.SQUARED) return Math.signum(input) * Math.pow(input, 2);
            else if (scale == Scaling.CUBED) return Math.pow(input, 3);
            else return input;
        } else {
            return 0;
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
        return -getOutput(super.getX(hand), Scaling.SQUARED);
    }

    /**
     * Returns the Y value of the joystick for a given hand and scaling.
     */
    public double getX(Hand hand, Scaling scale) {
        return -getOutput(super.getX(hand), scale);
    }


    /**
     * Returns the JoystickButton object for the A button.
     */
    public JoystickButton getAButtonObj() {
        return new JoystickButton(this, F310.A);
    }

    /**
     * Returns the JoystickButton object for the B button.
     */
    public JoystickButton getBButtonObj() {
        return new JoystickButton(this, F310.B);
    }

    /**
     * Returns the JoystickButton object for the X button.
     */
    public JoystickButton getXButtonObj() {
        return new JoystickButton(this, F310.X);
    }

    /**
     * Returns the JoystickButton object for the Y button.
     */
    public JoystickButton getYButtonObj() {
        return new JoystickButton(this, F310.Y);
    }

    /**
     * Returns the JoystickButton object for the left bumper button.
     */
    public JoystickButton getLbButtonObj() {
        return new JoystickButton(this, F310.LB);
    }

    /**
     * Returns the JoystickButton object for the right bumper button.
     */
    public JoystickButton getRbButtonObj() {
        return new JoystickButton(this, F310.RB);
    }

    /**
     * Returns the JoystickButton object for the back button.
     */
    public JoystickButton getBackButtonObj() {
        return new JoystickButton(this, F310.BACK);
    }

    /**
     * Returns the JoystickButton object for the start button.
     */
    public JoystickButton getStartButtonObj() {
        return new JoystickButton(this, F310.START);
    }

    /**
     * Returns the JoystickButton object for the left stick button.
     */
    public JoystickButton getLsButtonObj() {
        return new JoystickButton(this, F310.LS);
    }

    /**
     * Returns the JoystickButton object for the right stick button.
     */
    public JoystickButton getRsButtonObj() {
        return new JoystickButton(this, F310.RS);
    }

    /**
     * Returns the POVButton object for the D-pad up button.
     */
    public POVButton getDpadUpButtonObj() {
        return new POVButton(this, F310.DPAD_UP);
    }

    /**
     * Returns the POVButton object for the D-pad right button.
     */
    public POVButton getDpadRightButtonObj() {
        return new POVButton(this, F310.DPAD_RIGHT);
    }

    /**
     * Returns the POVButton object for the D-pad down button.
     */
    public POVButton getDpadDownButtonObj() {
        return new POVButton(this, F310.DPAD_DOWN);
    }

    /**
     * Returns the POVButton object for the D-pad left button.
     */
    public POVButton getDpadLeftButtonObj() {
        return new POVButton(this, F310.DPAD_LEFT);
    }

    /**
     * Returns the Trigger object for the left trigger.
     */
    public Trigger getLtButtonObj() {
        return new Trigger(() -> super.getTriggerAxis(Hand.kLeft) > TRIGGER_THRESHOLD);
    }

    /**
     * Returns the Trigger object for the right trigger.
     */
    public Trigger getRtButtonObj() {
        return new Trigger(() -> super.getTriggerAxis(Hand.kRight) > TRIGGER_THRESHOLD);
    }

    public boolean getEmergencyPower() {
        return EmergencyPower;
    }

    public void setEmergencyPower(boolean EmergencyPower) {
        this.EmergencyPower = EmergencyPower;
    }

}

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.*;

public class WheelSpinner extends SubsystemBase {

    private Solenoid wheelSpinnerSolenoid = new Solenoid(4);
    private CANSparkMax wheelSpinnerMotor = new CANSparkMax(26, MotorType.kBrushless);
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 ColorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch colorMatch = new ColorMatch();

    private final Color kBLUE = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGREEN = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRED = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYELLOW = ColorMatch.makeColor(0.361, 0.524, 0.113);

    colorMatch.addColorMatch(kBLUE);
    colorMatch.addColorMatch(kGREEN);
    colorMatch.addColorMatch(kRED);
    colorMatch.addColorMatch(kYELLOW);


    int proximity;
    double IR;
    Color detectedColor;

    enum cColor{
        RED,
        YELLOW,
        BLUE,
        GREEN
    }

    Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

    private boolean isRaised = true;

    public void periodic(){
        IR = ColorSensor.getIR;
        proximity = ColorSensor.getProximity();

        ColorMatchResult match = colorMatch.matchClosestColor(detectedColor);


        SmartDashboard.putNumber("IR", IR);
        SmartDashboard.putNumber("Proximity", proximity);
        SmartDashboard.putBoolean("Wheel Spinner Raised", isRaised());

    }

    /**
     * Sets wheel spinner to a certain percent output.
     */
    public void setPercentage(double percent) {
        wheelSpinnerMotor.set(percent);
    }

    /**
     * Sets wheel spinner to raised position.
     */
    public void raise() {
        wheelSpinnerSolenoid.set(false);
        isRaised = true;
    }

    /**
     * Sets wheel spinner to lowered position.
     */
    public void lower() {
        wheelSpinnerSolenoid.set(true);
        isRaised = false;
    }

    /**
     * Returns true if wheel spinner is in the raised position.
     */
    public boolean isRaised() {
        return isRaised;
    }

    /**
     * Runs the color wheel a certain number of rotations
     */
    public  void runRotations(int rotations) {

        int colorCounts = rotations * 2;

        Color set;
        Color dColor;
        Color cColor;

        boolean onColor = true;

        if (match.color == Color.BLUE) set = Color.BLUE;
        else if (match.color == Color.RED) set = Color.RED;
        else if (match.color == Color.GREEN) set = Color.GREEN;
        else if (match.color == Color.YELLOW) set = Color.YELLOW;

        setPercentage(1);

        while (progress > 0) {
            if (set != dColor) {
                if (set == cColor) colorCounts -= 1;
            }
        }

        setPercentage(0);

    }

    /**
     * Sets the Color Wheel to a certain color.
     */
    public void runColor(Color set){
        if (set == Color.RED || set == Color.GREEN) set = colors[index(colors, set) + 2];
        else if (set == Color.BLUE) set = Color.RED;
        else if (set == Color.YELLOW) set = Color.GREEN;

        Color color;

        if (match.color == BLUE) color = Color.BLUE;
        else if (match.color == Color.RED) color = Color.RED;
        else if (match.color == Color.GREEN) color = Color.GREEN;
        else if (match.color == Color.YELLOW) color = Color.YELLOW;

        if (color != set) setPercentage(1);

        while (color != set) {
            if (match.color == Color.BLUE) color = Color.BLUE;
            else if (match.color == Color.RED) color = Color.RED;
            else if (match.color == Color.GREEN) color = Color.GREEN;
            else if (match.color == Color.YELLOW) color = Color.YELLOW;
        }

        if (color == set) setPercentage(0);

    }

    /**
     * Runs a certain number of rotations and ctops on a certain color.
     */
    public void runBoth(int rotations, Color color) {
        runRotations(rotations);
        runColor(color);
    }

    public static int index(Color arr[], Color t) {
        if (arr = null) return -1;

        int len = arr.length;
        int i = 0;

        while (i < len) {
            if (arr[i] == t) return i;
            else i++;
        return -1;
        }
    }
}
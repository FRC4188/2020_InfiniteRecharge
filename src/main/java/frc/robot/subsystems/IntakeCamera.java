package frc.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class IntakeCamera {

    NetworkTable grip;
    NetworkTableEntry ballx;
    NetworkTableEntry bally;
    NetworkTableEntry balla;

    double SIZE_THRESHOLD = 60.0;

    public IntakeCamera() {
        grip = NetworkTableInstance.getDefault().getTable("GRIP/Balls");
        ballx = grip.getEntry("x");
        bally = grip.getEntry("y");
        balla = grip.getEntry("size");

        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setFPS(30);
        camera.setExposureManual(45);
        camera.setResolution(320, 240);
    }

    /**
     * Method to get the X values of balls in the camera's view as an array.
     */
    public double[] getXs() {
        // If the ballx entry has no value, default to an empty double array.
        return ballx.getDoubleArray(new double[0]);
    }

    /**
     * Method to get the Y values of balls in the camera's view as an array.
     */
    public double[] getYs() {
        // If the bally entry has no value, default to an empty double array.
        return bally.getDoubleArray(new double[0]);
    }

    /**
     * MMethod to get the area values of the balls in the image.
     */
    public double[] getAs() {
        // If the balla entry has no value, default to an empty double array.
        return balla.getDoubleArray(new double[0]);
    }

    /**
     * Method to return the X angle of the closest ball.
     * @param cameraAngle The mounting angle of the camera.
     */
    public double getCloseX(double cameraAngle) {
        // If there are no balls, return angle 0 (so that the robot remains in the same orientation).
        if (getXs().length == 0) return 0.0;
        else {
            // The ball with the highest Y value is the closest because the closer the ball is, the
            // lower it is in the frame, and the pixels count from the top.
            double[] yArray = getYs();
            double[] xArray = getXs();
            double[] aArray = getAs();

            boolean taken = false;
            int index = 0;
            double max = 0.0;
            // Iterate through the Y values and whenever a Y value is the highest seen so far, save the index.
            for (int i = 0; i < yArray.length; i++) {

                // First filter out x values without corresponding y values (sometimes a ball enters/leaves
                // view only one value makes it through NetworkTables) and then filters out balls that take
                // up too much area (those are already in the intake).
                if (yArray[i] > max && i < xArray.length && i < aArray.length) {
                    if (aArray[i] < SIZE_THRESHOLD) {
                        index = i;
                        max = yArray[i];
                        taken = true;
                    }
                }
            }

            // Select the X value of the closest ball and transform its X value to an angle
            // (using known FOV and mounting angle of the camera).
            return taken ? (xArray[index] - 151.0) * (53.0 / 320.0) + cameraAngle : 0.0;
        }
    }
}
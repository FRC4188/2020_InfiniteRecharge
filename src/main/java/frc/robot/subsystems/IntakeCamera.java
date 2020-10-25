package frc.robot.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeCamera extends SubsystemBase {

    public void IntakeCamera() {
        // Creates UsbCamera and MjpegServer [1] and connects them
        CameraServer.getInstance().startAutomaticCapture();

        // Creates the CvSink and connects it to the UsbCamera
        CvSink cvSink = CameraServer.getInstance().getVideo();
        
        // Creates the CvSource and MjpegServer [2] and connects them
        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
    }

    /**
     * Runs every loop.
     */
    public void periodic() {
    }
}